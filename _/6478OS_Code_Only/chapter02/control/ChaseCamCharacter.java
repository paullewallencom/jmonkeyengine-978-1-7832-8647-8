/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.control;

import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;

/**
 *
 * @author Rickard
 */
public class ChaseCamCharacter extends GameCharacterControl{
    
    private Camera cam;
    private Vector3f modelForwardDir;
    private Vector3f modelLeftDir;
    
    /**
     * Cover
     */
    private float playerWidth = 0.1f;
    private boolean inCover;
    private boolean hasLowCover;
    private boolean hasHighCover;
    private float lowHeight = 0.5f;
    private float highHeight = 1.5f;
    private Node structures;
    /**
     * 
     */
    
    public ChaseCamCharacter(float radius, float height, float mass) {
        super(radius, height, mass);
    }
    
    @Override
    public void update(float tpf){
        super.update(tpf);
        if(!forward && !backward && !leftStrafe && !rightStrafe && !inCover){
            modelForwardDir = cam.getRotation().mult(Vector3f.UNIT_Z).multLocal(1, 0, 1);
            modelLeftDir = cam.getRotation().mult(Vector3f.UNIT_X);
        } else if(inCover){
            modelForwardDir = spatial.getWorldRotation().mult(Vector3f.UNIT_Z);
            modelLeftDir = spatial.getWorldRotation().mult(Vector3f.UNIT_X);
        }
        
        walkDirection.set(0, 0, 0);
        if (forward) {
            walkDirection.addLocal(modelForwardDir.mult(moveSpeed));
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(moveSpeed));
        }
        if (leftStrafe) {
            walkDirection.addLocal(modelLeftDir.mult(moveSpeed));
        } else if (rightStrafe) {
            walkDirection.addLocal(modelLeftDir.negate().multLocal(moveSpeed));
        }
        if(walkDirection.length() > 0){
            if(inCover){
                checkCover(spatial.getWorldTranslation().add(walkDirection.multLocal(0.2f).mult(0.1f)));
                if(!hasLowCover && !hasHighCover){
                    walkDirection.set(Vector3f.ZERO);
                }
            } else {
                viewDirection.set(walkDirection);
            }
        }
        
        setViewDirection(viewDirection.normalizeLocal());
        setWalkDirection(walkDirection);
    }
    
    @Override
    public void setCamera(Camera cam){
        this.cam = cam;
//        camNode = new CameraNode("CamNode", cam);
//        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
//        head.attachChild(camNode);
//        camNode.setLocalTranslation(new Vector3f(0, 5, -5));
//        camNode.lookAt(head.getLocalTranslation(), Vector3f.UNIT_Y);
    }
    
    /**
     * Cover
     */
   
    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if(binding.equals("ToggleCover") && value){
            if(inCover){
                inCover = false;
            } else {
                checkCover(spatial.getWorldTranslation());
                if(hasLowCover || hasHighCover){
                    inCover = true;
                }
            }
        } else if(inCover){
            if (binding.equals("StrafeLeft")) {
                leftStrafe = value;
            } else if (binding.equals("StrafeRight")) {
                rightStrafe = value;
            }
        } else {
            if (binding.equals("StrafeLeft")) {
                leftStrafe = value;
            } else if (binding.equals("StrafeRight")) {
                rightStrafe = value;
            } else if (binding.equals("MoveForward")) {
                forward = value;
            } else if (binding.equals("MoveBackward")) {
                backward = value;
            } else if (binding.equals("Jump")) {
                jump();
            }
        }
    }
     
    public void setStructures(Node structures){
        this.structures = structures;
    }
    
    public void checkCover(Vector3f position){
        Ray ray = new Ray();
        ray.setDirection(viewDirection);
        ray.setLimit(0.8f);

        int lowCollisions = 0;
        int highCollisions = 0;
        
        CollisionResults collRes = new CollisionResults();
        
        Vector3f leftDir = spatial.getWorldRotation().getRotationColumn(0).mult(playerWidth);
        leftDir.setY(lowHeight);
        for(int i = -1; i < 2; i++){
            leftDir.multLocal(i, 1, i);
            ray.setOrigin(position.add(leftDir));
            structures.collideWith(ray, collRes);
            if(collRes.size() > 0){
                lowCollisions++;
            }
            collRes.clear();
        }
        
        if(lowCollisions == 3){
            leftDir.setY(highHeight);
            for(int i = -1; i < 2; i++){
                leftDir.multLocal(i, 1, i);
                ray.setOrigin(position.add(leftDir));
                structures.collideWith(ray, collRes);
                if(collRes.size() > 0){
                    highCollisions++;
                }
                collRes.clear();
            }
        
            ray.setOrigin(spatial.getWorldTranslation().add(0, 0.5f, 0));
            structures.collideWith(ray, collRes);

            Triangle t = new Triangle();
            collRes.getClosestCollision().getTriangle(t);
//            t.calculateNormal();
            viewDirection.set(t.getNormal().negate());//alignWithCover(t.getNormal());
            if(highCollisions == 3){
                hasHighCover = true;
            } else {
                hasLowCover = true;
            }
            
        } else {
            hasHighCover = false;
            hasLowCover = false;
        }
    }
  
    /**
     * 
     */
}
