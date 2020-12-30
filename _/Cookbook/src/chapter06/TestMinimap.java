/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import de.lessvoid.nifty.tools.Color;
import chapter06.controller.DialogScreenController;
import chapter06.controller.GameScreenController;
import chapter06.controller.NiftyController;
import chapter06.state.NiftyAppState;

/**
 *
 * @author reden
 */
public class TestMinimap extends SimpleApplication{

    public static void main(String[] args){
        TestMinimap test = new TestMinimap();
        test.start();
    }
    
    private boolean minimapRendered;
    private boolean gameScreen;
    NiftyAppState appState;
    Node scene;
    @Override
    public void simpleInitApp() {
        appState = new NiftyAppState();
        stateManager.attach(appState);
        
        scene = (Node) assetManager.loadModel("Scenes/TestScene.j3o");
        rootNode.attachChild(scene);
        
        flyCam.setEnabled(false);
    }

    private float playerLocationX;
    private float playerLocationY;
    private float time;
    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        
        if(!gameScreen){
            appState.getNifty().gotoScreen("gameScreen");
            gameScreen = true;
        }else if(!minimapRendered ){
            ((GameScreenController)appState.getNifty().getScreen("gameScreen").getScreenController()).createMinimap(scene);
            
            minimapRendered = true;
        }
        
        playerLocationX = FastMath.sin(time);
        playerLocationY = FastMath.cos(time);
        time = (time + tpf) % FastMath.TWO_PI;
        
        ((GameScreenController)appState.getNifty().getScreen("gameScreen").getScreenController()).updatePlayerPosition((int) (128 + playerLocationX * 30f), (int)(128 + playerLocationY * 30f));
    }
}
