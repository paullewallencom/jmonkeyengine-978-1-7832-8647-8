/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.endless;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author reden
 */
public class TestEndlessWorld extends SimpleApplication{

    public static void main(String[] args){
        TestEndlessWorld testEndlessWorld = new TestEndlessWorld();
        testEndlessWorld.start();
    }
    
    @Override
    public void simpleInitApp() {
//        flyCam.setMoveSpeed(0);
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(0, 15, 0));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
//        EndlessAppState appState = new EndlessAppState();
//        
//        stateManager.attach(appState);
        
        setup();
        
    }
    
    private void setup(){
        Node worldNode = new Node("World");
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        rootNode.attachChild(worldNode);
        
        EndlessWorldControl worldControl = new EndlessWorldControl();
        
        worldControl.setMaterial(m);
        worldNode.addControl(worldControl);
        worldControl.setCamera(cam);
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(worldControl, "Forward", "Back", "Left", "Right");
    }
    
}
