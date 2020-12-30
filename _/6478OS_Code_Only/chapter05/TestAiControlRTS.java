/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05;

import chapter05.control.AIControl_RTS;
import chapter05.control.AIControl_SM;
import chapter05.control.NavMeshNavigationControl;
import chapter05.state.AIAppState;
import chapter05.state.GatherFoodState;
import chapter05.state.GatherWoodState;
import chapter04.control.AnimationManagerControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.Terrain;
import chapter02.control.GameCharacterControl;

/**
 *
 * @author reden
 */
public class TestAiControlRTS extends SimpleApplication {
    
    private BulletAppState bulletAppState;
    private AIAppState aiAppState;
    
    public static void main(String[] args) {
        TestAiControlRTS app = new TestAiControlRTS();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        getFlyByCamera().setMoveSpeed(45f);
        cam.setLocation(new Vector3f(20, 20, 20));
        cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        
        
        
        aiAppState = new AIAppState();
        stateManager.attach(aiAppState);
//        DirectionalLight l = new DirectionalLight();
//        rootNode.addLight(l);
        Node scene = setupWorld();
        for(int i = 0; i < 3; i++){
            addCharacter(scene);
        }
        
    }
    
    private Node setupWorld(){
        Node scene = (Node) assetManager.loadModel("Scenes/TestScene_Clean.j3o");
        rootNode.attachChild(scene);

        FilterPostProcessor processor = (FilterPostProcessor) assetManager.loadAsset("Effects/Water.j3f");
        viewPort.addProcessor(processor);
        
        Spatial terrain = scene.getChild("terrain-TestScene");
        terrain.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().addAll(terrain);
        
        Geometry food = new Geometry("Food", new Box(1,2,1));
        Material m = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Green);
        food.setMaterial(m);
        scene.attachChild(food);
        aiAppState.setResource(GatherFoodState.class, food);
        
        float height = ((Terrain)terrain).getHeight(new Vector2f(10, 10));
        food.setLocalTranslation(10, height, 10);
        
        Geometry wood = new Geometry("Wood", new Box(1,2,1));
        Material m2 = m.clone();
        m2.setColor("Color", ColorRGBA.Brown);
        wood.setMaterial(m2);
        scene.attachChild(wood);
        aiAppState.setResource(GatherWoodState.class, wood);
        
        height = ((Terrain)terrain).getHeight(new Vector2f(-10, -10));
        wood.setLocalTranslation(-10, height, -10);
        return scene;
    }

    private void addCharacter(Node scene){
        // Load model, attach to character node
        Node aiCharacter = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");

        GameCharacterControl physicsCharacter = new GameCharacterControl(0.3f, 2.5f, 8f);
        aiCharacter.addControl(physicsCharacter);
        bulletAppState.getPhysicsSpace().add(physicsCharacter);
        aiCharacter.setLocalTranslation(0, 10, 0);
        scene.attachChild(aiCharacter);
        AIControl_RTS aiControl = new AIControl_RTS();
        aiCharacter.addControl(aiControl);
        aiAppState.addWorker(aiControl);
    }
}
