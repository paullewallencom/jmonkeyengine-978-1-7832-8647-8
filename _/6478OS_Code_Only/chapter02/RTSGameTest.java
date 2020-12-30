/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import chapter02.state.RTSCameraAppState;
import chapter01.LoadScene;

/**
 *
 * @author Rickard
 */
public class RTSGameTest extends LoadScene{

    public static void main(String[] args) {
        RTSGameTest app = new RTSGameTest();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        getFlyByCamera().setEnabled(false);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));
        
//        Spatial scene = assetManager.loadModel("Scenes/TestScene.j3o");
//        rootNode.attachChild(scene);
        
        RTSCameraAppState appState = new RTSCameraAppState();
        
        /**
         * 
         */
        Spatial terrain = ((Node)rootNode.getChild(0)).getChild("terrain-TestScene");
        terrain.addControl(new RigidBodyControl(0));
        appState.setTerrain(terrain);
        stateManager.attach(appState);
        /**
         * 
         */
    }
    
}
