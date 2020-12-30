/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04.control;

import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.bullet.control.KinematicRagdollControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author reden
 */
public class IKFeetControl extends AbstractControl{

    private Spatial world;
    private Skeleton skeleton;
    
    private Vector3f leftFootPos;
    private Vector3f rightFootPos;
    private Vector3f leftToePos;
    private Vector3f rightToePos;
    
    private Bone leftFoot;
    private Bone rightFoot;
    private Bone leftToe;
    private Bone rightToe;
    
    float legLength;
    float pelvisHeight;
    float footHeight;
    
    private List<Bone> targets = new ArrayList<Bone>();

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        skeleton = spatial.getControl(SkeletonControl.class).getSkeleton();
//        leftFoot = skeleton.getBone("foot.L");
//        rightFoot = skeleton.getBone("foot.R");
//        leftToe = skeleton.getBone("toe.L");
//        rightToe = skeleton.getBone("toe.R");
        targets.add(skeleton.getBone("foot.L"));
        targets.add(skeleton.getBone("foot.R"));
        targets.add(skeleton.getBone("toe.L"));
        targets.add(skeleton.getBone("toe.R"));
        
        legLength = skeleton.getBone("pelvis").getLocalPosition().distance(skeleton.getBone("thigh.L").getLocalPosition()) + 
                skeleton.getBone("thigh.L").getLocalPosition().distance(skeleton.getBone("shin.L").getLocalPosition()) + 
                skeleton.getBone("shin.L").getLocalPosition().distance(skeleton.getBone("foot.L").getLocalPosition());
        
//        spatial.getControl(KinematicRagdollControl.class).setJointLimit("thigh.R", 0, 0, 0, 0, 0, 0);
//        spatial.getControl(KinematicRagdollControl.class).setJointLimit("thigh.L", FastMath.PI, -FastMath.PI, FastMath.PI, -FastMath.PI,  0, 0);
//        spatial.getControl(KinematicRagdollControl.class).setJointLimit("thigh.R", FastMath.PI, -FastMath.PI, FastMath.PI, -FastMath.PI,  0, 0);
        spatial.getControl(KinematicRagdollControl.class).setJointLimit("shin.L", FastMath.PI, 0, 0, 0, 0, 0);
        spatial.getControl(KinematicRagdollControl.class).setJointLimit("shin.R", 0, -FastMath.PI, 0, 0, 0, 0);
//        spatial.getControl(KinematicRagdollControl.class).setJointLimit("foot.L", FastMath.PI, 0, 0, 0, 0, 0);
//        spatial.getControl(KinematicRagdollControl.class).setJointLimit("foot.R", 0, -FastMath.PI, 0, 0, 0, 0);
        
        setupJaime(spatial.getControl(KinematicRagdollControl.class));
        spatial.getControl(KinematicRagdollControl.class).setIKThreshold(0.01f);
        spatial.getControl(KinematicRagdollControl.class).setLimbDampening(0.9f);
        spatial.getControl(KinematicRagdollControl.class).setIkRotSpeed(18);
        sampleTargetPositions();
    }
    
    
    @Override
    protected void controlUpdate(float tpf) {
        if(enabled){
            
        }
    }
    
    private CollisionResult contactPointForBone(Vector3f bonePosition, float offset){
        Ray ray = new Ray();
        ray.setOrigin(bonePosition.add(0, offset, 0));
        ray.setDirection(Vector3f.UNIT_Y);
        CollisionResults collRes = new CollisionResults();
        world.collideWith(ray, collRes);
        
        if(collRes.size() > 0){
            return collRes.getClosestCollision();
        } else {
            return null;
        }
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }

    public Spatial getWorld() {
        return world;
    }

    public void setWorld(Spatial world) {
        this.world = world;
    }
    
    public void sampleTargetPositions(){
//        footHeight = 0f;
//        pelvisHeight = skeleton.getBone("pelvis").getModelSpacePosition().add(spatial.getWorldTranslation()).getY();
        float offset = -1.9f;
        for(Bone bone: targets){
            Vector3f targetPosition = bone.getModelSpacePosition().add(spatial.getWorldTranslation());
            CollisionResult closestResult = contactPointForBone(targetPosition, offset);
            if(closestResult != null){
                spatial.getControl(KinematicRagdollControl.class).setIKTarget(bone, closestResult.getContactPoint().addLocal(0, 0.05f, 0), 2);
            }
        }
        spatial.getControl(KinematicRagdollControl.class).setIKMode();
        
//        leftFootPos = leftFoot.getModelSpacePosition().add(spatial.getWorldTranslation());
//        rightFootPos = rightFoot.getModelSpacePosition().add(spatial.getWorldTranslation());
//        leftToePos = leftToe.getModelSpacePosition().add(spatial.getWorldTranslation());
//        rightToePos = rightToe.getModelSpacePosition().add(spatial.getWorldTranslation());
        
//            CollisionResult closestLeftFoot = contactPointForBone(leftFootPos, offset);
//            CollisionResult closestRightFoot = contactPointForBone(rightFootPos, offset);
//            CollisionResult closestLeftToe = contactPointForBone(leftToePos, offset);
//            CollisionResult closestRightToe = contactPointForBone(rightToePos, offset);
//            if(closestLeftFoot != null && closestRightFoot != null){
//                System.out.println("actual " + leftFootPos + " " + rightFootPos);
//                System.out.println("closest " + closestLeftFoot.getContactPoint() + " " + closestRightFoot.getContactPoint());
//                 if(footHeight == 0f){
//                     footHeight = Math.min(closestLeftFoot.getContactPoint().getY(), closestRightFoot.getContactPoint().getY());
//                 }
//
//                float distance = pelvisHeight - footHeight;
//                float diff = distance - legLength;
//
////                spatial.getControl(KinematicRagdollControl.class).setIKTarget(skeleton.getBone("hips"), skeleton.getBone("hips").getModelSpacePosition().add(spatial.getWorldTranslation()).add(0,diff, 0), 2);
//                spatial.getControl(KinematicRagdollControl.class).setIKTarget(leftFoot, closestLeftFoot.getContactPoint().addLocal(0, 0.05f, 0), 2);
//                spatial.getControl(KinematicRagdollControl.class).setIKTarget(rightFoot, closestRightFoot.getContactPoint().addLocal(0, 0.05f, 0), 2);
//                spatial.getControl(KinematicRagdollControl.class).setIKTarget(leftToe, closestLeftToe.getContactPoint(), 1);
//                spatial.getControl(KinematicRagdollControl.class).setIKTarget(rightToe, closestRightToe.getContactPoint(), 1);
//                spatial.getControl(KinematicRagdollControl.class).setIKMode();
//            }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(enabled){
            sampleTargetPositions();
        } else {
            spatial.getControl(KinematicRagdollControl.class).removeAllIKTargets();
            spatial.getControl(KinematicRagdollControl.class).setKinematicMode();
        }
    }
    
    private void setupJaime(KinematicRagdollControl ragdoll) {
        ragdoll.addBoneName("toe.R");
        ragdoll.addBoneName("foot.R");
        ragdoll.addBoneName("shin.R");
        ragdoll.addBoneName("thigh.R");
        ragdoll.addBoneName("toe.L");
        ragdoll.addBoneName("foot.L");
        ragdoll.addBoneName("shin.L");
        ragdoll.addBoneName("thigh.L");
        ragdoll.addBoneName("pelvis");
        ragdoll.addBoneName("hips");
        ragdoll.addBoneName("Root");
    }
}
