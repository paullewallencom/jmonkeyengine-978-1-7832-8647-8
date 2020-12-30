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
public class TestIK2 extends SimpleApplication {

    private BulletAppState bulletAppState;
    private Vector3f normalGravity = new Vector3f(0, -9.81f, 0);
    
    Node playerNode;
    
    ChaseCamCharacter charControl;
    KinematicRagdollControl ikControl;
    IKFeetControl feetControl;
    AdvAnimationManagerControl animControl;
    
    public static void main(String[] args) {
        TestIK2 app = new TestIK2();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        getFlyByCamera().setMoveSpeed(40f);
        cam.setLocation(new Vector3f(-55, 4.5f, -55));
        cam.lookAt(new Vector3f(-50, 4.15f, -50), Vector3f.UNIT_Y);
        Node scene = (Node) assetManager.loadModel("Scenes/TestScene_Clean.j3o");
        Spatial terrain = scene.getChild("terrain-TestScene");
        
        terrain.addControl(new RigidBodyControl(0));
        
        bulletAppState.getPhysicsSpace().addAll(terrain);
        rootNode.attachChild(scene);
        
        playerNode = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        playerNode.setLocalTranslation(-50, 4.15f, -50);
        playerNode.getChild("JaimeGeom-geom-1").rotate(0, FastMath.PI, 0);
        scene.attachChild(playerNode);
        
        inputManager.setCursorVisible(false);
        
        
        ikControl = new KinematicRagdollControl(0.5f);
        playerNode.addControl(ikControl);
        bulletAppState.getPhysicsSpace().add(ikControl);
        
        feetControl = new IKFeetControl();
        feetControl.setWorld(terrain);
        playerNode.addControl(feetControl);
    }

}
