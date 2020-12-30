/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05;

import chapter05.control.AIControl;
import chapter05.control.AIControl2;
import chapter05.control.AIControl_SM;
import chapter04.control.AnimationManagerControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import chapter02.control.GameCharacterControl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import jme3test.bullet.PhysicsTestHelper;

public class TestAiControlSM extends SimpleApplication{

    private BulletAppState bulletAppState;
    public static Material lineMat;
    
    public static void main(String[] args) {
        TestAiControlSM app = new TestAiControlSM();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        lineMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        // activate physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // init a physics test scene
        PhysicsTestHelper.createPhysicsTestWorldSoccer(rootNode, assetManager, bulletAppState.getPhysicsSpace());
        PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace());


        // Load model, attach to character node
        final Node aiCharacter = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        aiCharacter.setLocalScale(1.50f);
        GameCharacterControl physicsCharacter = new GameCharacterControl(0.3f, 2.5f, 8f);
        aiCharacter.addControl(physicsCharacter);
        getPhysicsSpace().add(physicsCharacter);
        rootNode.attachChild(aiCharacter);
        aiCharacter.addControl(new AIControl_SM());
        aiCharacter.addControl(new AnimationManagerControl());
        
        // Add character node to the rootNode
//        rootNode.attachChild(characterNode);
        
        CameraNode camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.CameraToSpatial);

        Geometry g = new Geometry("", new Box(1,1,1));
        g.setModelBound(new BoundingSphere(5f, Vector3f.ZERO));
        g.updateModelBound();
        g.setMaterial(lineMat);
        camNode.attachChild(g);
        getFlyByCamera().setMoveSpeed(25);
        rootNode.attachChild(camNode);
        
        final List<Spatial> targets = new ArrayList<Spatial>();
        targets.add(g);
        aiCharacter.getControl(AIControl_SM.class).setTargetList(targets);
    }
    
    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }
}
