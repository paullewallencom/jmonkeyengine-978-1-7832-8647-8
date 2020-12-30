/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.server.object;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import chapter07.fps.common.message.PlayerActionMessage;
import chapter07.fps.common.message.PlayerMessage;
import chapter07.fps.common.object.NetworkedPlayerControl;
import static chapter07.fps.common.object.NetworkedPlayerControl.MOVE_SPEED;

/**
 *
 * @author reden
 */
public class ServerPlayerControl extends NetworkedPlayerControl{

    boolean forward = false, backward = false, leftRotate = false, rightRotate = false, leftStrafe = false, rightStrafe = false;
    private Node level;
   
    /**
     * Visibility
     */
    private List<Integer> visiblePlayers = new ArrayList<Integer>();
    
    /**
     * /Visibility
     */
    
    /**
     * Physics
     */
    private boolean usePhysics;
    private BetterCharacterControl physicsCharacter;
    /**
     * /Physics
     */

    @Override
    public void onMessageReceived(PlayerMessage message) {
        if(message instanceof PlayerActionMessage){
            String action = ((PlayerActionMessage) message).getAction();
            
            boolean value = ((PlayerActionMessage) message).isPressed();
            float floatValue = ((PlayerActionMessage) message).getFloatValue();
            if (action.equals("StrafeLeft")) {
                leftStrafe = value;
            } else if (action.equals("StrafeRight")) {
                rightStrafe = value;
            } else if (action.equals("MoveForward")) {
                forward = value;
            } else if (action.equals("MoveBackward")) {
                backward = value;
            } else if (action.equals("RotateLeft")) {
                rotate(floatValue);
            } else if (action.equals("RotateRight")) {
                rotate(-floatValue);
            } else if(action.equals("LookUp")){
                lookUpDown(floatValue);
            } else if (action.equals("LookDown")){
                lookUpDown(-floatValue);
            }
        }
    }

    protected void rotate(float value){
        Quaternion rotate = new Quaternion().fromAngleAxis(FastMath.PI * value, Vector3f.UNIT_Y);
        tempRotation.multLocal(rotate);
    }
    
    protected void lookUpDown(float value){
        yaw += value;
        yaw = FastMath.clamp(yaw, -FastMath.HALF_PI, FastMath.HALF_PI);
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f walkDirection = new Vector3f();
        Vector3f modelForwardDir = spatial.getWorldRotation().mult(Vector3f.UNIT_Z);
        Vector3f modelLeftDir = spatial.getWorldRotation().mult(Vector3f.UNIT_X);
        walkDirection.set(0, 0, 0);
        if (forward) {
            walkDirection.addLocal(modelForwardDir.mult(MOVE_SPEED * tpf));
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(MOVE_SPEED * tpf));
        }
        if (leftStrafe) {
            walkDirection.addLocal(modelLeftDir.mult(MOVE_SPEED * tpf));
        } else if (rightStrafe) {
            walkDirection.addLocal(modelLeftDir.negate().multLocal(MOVE_SPEED * tpf));
        }
        
        /**
         * Physics
         */
        if(usePhysics){
            physicsCharacter.setWalkDirection(walkDirection.multLocal(50));
            physicsCharacter.setViewDirection(tempRotation.getRotationColumn(2));
        } else {
            
        /**
         * /Physics
         */
            
            spatial.move(walkDirection);
            spatial.setLocalRotation(tempRotation);
            spatial.getLocalTranslation().setY(checkHeight());
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    private static final Vector3f DOWN_VECTOR = new Vector3f(0f, -1f, 0f);
    
    private float checkHeight(){
        Ray ray = new Ray(spatial.getWorldTranslation().add(0, 0.1f, 0), DOWN_VECTOR);
        CollisionResults results = new CollisionResults();
        level.collideWith(ray, results);
        Vector3f contactPoint = results.getClosestCollision().getContactPoint();
        return contactPoint != null ? contactPoint.y : 0;
    }
    
    public void setLevel(Node level){
        this.level = level;
    }
      
    /**
     * Visibility
     */
    public boolean addVisiblePlayer(Integer playerId){
        return visiblePlayers.add(playerId);
    }
    
    public boolean removeVisiblePlayer(Integer playerId){
        return visiblePlayers.remove(playerId);
    }
    
    /**
     * /Visibility
     */
    
    public BetterCharacterControl getPhysicsCharacter(){
        return physicsCharacter;
    }
    
    @Override
    public void setSpatial(Spatial s){
        super.setSpatial(s);
        
        /**
         * Physics
         */
        if(spatial.getControl(BetterCharacterControl.class) != null){
            usePhysics = true;
            physicsCharacter = spatial.getControl(BetterCharacterControl.class);
        }
        
        /**
         * /Physics
         */
        
    }
}
