/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04;

import chapter04.control.AdvAnimationManagerControl;
import chapter04.control.AdvAnimationManagerIK;
import chapter04.control.IKFeetControl;
import chapter04.state.CharacterInputAnimationAppState;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionGroupListener;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.KinematicRagdollControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import chapter02.control.ChaseCamCharacter;
import jme3test.bullet.PhysicsTestHelper;

/**
 *
 * @author Rickard
 */
public class TestIK extends SimpleApplication {

    private BulletAppState bulletAppState;
    private Vector3f normalGravity = new Vector3f(0, -9.81f, 0);
    
    Node playerNode;
    
    ChaseCamCharacter charControl;
    KinematicRagdollControl ikControl;
    IKFeetControl feetControl;
    AdvAnimationManagerControl animControl;
    
    public static void main(String[] args) {
        TestIK app = new TestIK();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        
        stateManager.attach(bulletAppState);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        Node scene = (Node) assetManager.loadModel("Scenes/TestScene_Clean.j3o");
        Spatial terrain = scene.getChild("terrain-TestScene");
        
        terrain.addControl(new RigidBodyControl(0));
        
        bulletAppState.getPhysicsSpace().addAll(terrain);
        terrain.getControl(RigidBodyControl.class).setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        
        rootNode.attachChild(scene);
//        PhysicsTestHelper.createPhysicsTestWorldSoccer(rootNode, assetManager, bulletAppState.getPhysicsSpace());
        playerNode = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        playerNode.setLocalTranslation(-50, 4.15f, -50);
        charControl = new ChaseCamCharacter(0.3f, 1.0f, 8f);
        charControl.setGravity(normalGravity);
        charControl.setCamera(cam);
        
        charControl.getPhysicsRigidBody().setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        charControl.getPhysicsRigidBody().setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
//        playerNode.getChild("JaimeGeom-geom-1").move(0, -0.5f, 0);
        playerNode.getChild("JaimeGeom-geom-1").rotate(0, FastMath.PI, 0);
        
        playerNode.addControl(charControl);
//        bulletAppState.getPhysicsSpace().add(charControl);
        
        ChaseCamera chaseCam = new ChaseCamera(cam, playerNode, inputManager);
        chaseCam.setDragToRotate(false);
        chaseCam.setSmoothMotion(true);
        chaseCam.setLookAtOffset(new Vector3f(0, 1f, 0));
        chaseCam.setDefaultDistance(7f);
        chaseCam.setMaxDistance(8f);
        chaseCam.setMinDistance(2f);
        
        chaseCam.setTrailingSensitivity(50);
        chaseCam.setChasingSensitivity(10);
        chaseCam.setRotationSpeed(10);


        CharacterInputAnimationAppState appState = new CharacterInputAnimationAppState();
        appState.addActionListener(charControl);
        appState.addAnalogListener(charControl);
        appState.setChaseCamera(chaseCam);
        stateManager.attach(appState);
        scene.attachChild(playerNode);
        
        inputManager.setCursorVisible(false);
        
        animControl = new AdvAnimationManagerControl("animation/resources/animations-jaime.properties");
        playerNode.addControl(animControl);
        appState.addActionListener(animControl);
        appState.addAnalogListener(animControl);
        
        ikControl = new KinematicRagdollControl(0.5f);
        
        playerNode.addControl(ikControl);
        ikControl.getPhysicsRigidBody().addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        bulletAppState.getPhysicsSpace().add(ikControl);
        
        feetControl = new IKFeetControl();
        
        feetControl.setWorld(terrain);
        playerNode.addControl(feetControl);
        feetControl.setEnabled(false);
        bulletAppState.setDebugEnabled(true);
    }

    

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        if(charControl.getVelocity().length() < 0.3){
            
            if(!feetControl.isEnabled()){
                animControl.setAnimation(AdvAnimationManagerControl.Animation.Idle);
                feetControl.setEnabled(true);
            }
            
        } else if(feetControl.isEnabled()){
                feetControl.setEnabled(false);
                
            }
    }
    
    
}
