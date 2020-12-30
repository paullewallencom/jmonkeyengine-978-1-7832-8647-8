/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05;

import chapter05.control.AIControl_SM;
import chapter05.control.NavMeshNavigationControl;
import chapter04.control.AnimationManagerControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import chapter02.control.GameCharacterControl;

/**
 *
 * @author reden
 */
public class TestNavMesh extends SimpleApplication {
    
    private BulletAppState bulletAppState;
    
    public static void main(String[] args) {
        TestNavMesh app = new TestNavMesh();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        getFlyByCamera().setMoveSpeed(45f);
        cam.setLocation(new Vector3f(20, 20, 20));
        cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        
        Node scene = setupWorld();
        
//        DirectionalLight l = new DirectionalLight();
//        rootNode.addLight(l);
        setupCharacter(scene);
    }
    
    private Node setupWorld(){
        Node scene = (Node) assetManager.loadModel("Scenes/TestScene_NavMesh.j3o");
        rootNode.attachChild(scene);

        FilterPostProcessor processor = (FilterPostProcessor) assetManager.loadAsset("Effects/Water.j3f");
        viewPort.addProcessor(processor);
        
        Spatial terrain = scene.getChild("terrain-TestScene");
        terrain.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().addAll(terrain);
        return scene;
    }

    private void setupCharacter(Node scene){
        // Load model, attach to character node
        Node aiCharacter = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");

        GameCharacterControl physicsCharacter = new GameCharacterControl(0.3f, 2.5f, 8f);
        aiCharacter.addControl(physicsCharacter);
        bulletAppState.getPhysicsSpace().add(physicsCharacter);
        aiCharacter.setLocalTranslation(0, 10, 0);
        scene.attachChild(aiCharacter);
        aiCharacter.addControl(new NavMeshNavigationControl((Node) scene));
        aiCharacter.getControl(NavMeshNavigationControl.class).moveTo(new Vector3f(-40, 0, 10));
        
    }
}
