/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import chapter02.control.ChaseCamCharacter;
import chapter02.control.GameCharacterControl;
import chapter02.state.CharacterInputAppState;
import chapter02.state.CharacterInputAppStateChase;
import jme3test.bullet.PhysicsTestHelper;

/**
 *
 * @author Rickard
 */
public class ChaseCameraTest extends SimpleApplication {

    private BulletAppState bulletAppState;
    private Vector3f normalGravity = new Vector3f(0, -9.81f, 0);
    
    private Node coverStructures = new Node("Cover");
    
    public static void main(String[] args) {
        ChaseCameraTest app = new ChaseCameraTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        
        stateManager.attach(bulletAppState);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        PhysicsTestHelper.createPhysicsTestWorldSoccer(rootNode, assetManager, bulletAppState.getPhysicsSpace());

        Node playerNode = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        
        ChaseCamCharacter charControl = new ChaseCamCharacter(0.5f, 2.5f, 8f);
        charControl.setGravity(normalGravity);
        charControl.setCamera(cam);
        
        ChaseCamera chaseCam = new ChaseCamera(cam, playerNode, inputManager);
        chaseCam.setDragToRotate(false);
        chaseCam.setSmoothMotion(true);
        chaseCam.setLookAtOffset(new Vector3f(0, 1f, 0));
        chaseCam.setDefaultDistance(7f);
        chaseCam.setMaxDistance(8f);
        chaseCam.setMinDistance(6f);
        
        chaseCam.setTrailingSensitivity(50);
        chaseCam.setChasingSensitivity(10);
        chaseCam.setRotationSpeed(10);
//        chaseCam.setDragToRotate(true);
//        chaseCam.setToggleRotationTrigger();

        playerNode.addControl(charControl);
        bulletAppState.getPhysicsSpace().add(charControl);

        CharacterInputAppStateChase appState = new CharacterInputAppStateChase();
        appState.setCharacter(charControl);
        appState.setChaseCamera(chaseCam);
        stateManager.attach(appState);
        rootNode.attachChild(playerNode);
        
        inputManager.setCursorVisible(false);
        
        /**
         * Cover
         */
        createCoverStructures();
        charControl.setStructures(coverStructures);
        /**
         * 
         */
    }
    
    
    private void createCoverStructures(){
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Blue);
        Box box = new Box(2, 0.6f, 0.25f);
        Geometry boxGeometry = new Geometry("Box", box);
        boxGeometry.setMaterial(material);
        boxGeometry.setLocalTranslation(8, 0.3f, -2);
        boxGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
        coverStructures.attachChild(boxGeometry);
        bulletAppState.getPhysicsSpace().add(boxGeometry);
        
        Box box2 = new Box(0.5f, 1f, 0.25f);
        Geometry boxGeometry2 = new Geometry("Box", box2);
        boxGeometry2.setMaterial(material);
        boxGeometry2.setLocalTranslation(10.25f, 0.5f, -2);
        boxGeometry2.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
        coverStructures.attachChild(boxGeometry2);
        bulletAppState.getPhysicsSpace().add(boxGeometry2);
        
        rootNode.attachChild(coverStructures);
    }
    
    private void setupCamera(){
        
    }
}
