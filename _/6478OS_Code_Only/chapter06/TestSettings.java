/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.tools.Color;
import chapter06.controller.DialogScreenController;
import chapter06.controller.NiftyController;
import chapter06.state.NiftyAppState;

/**
 *
 * @author reden
 */
public class TestSettings extends SimpleApplication{

    private boolean settingsScreenOn;
    
    public static void main(String[] args){
        TestSettings test = new TestSettings();
        test.start();
    }
    
    boolean output = false;
    NiftyAppState appState;
    @Override
    public void simpleInitApp() {
        appState = new NiftyAppState();
        stateManager.attach(appState);
        
        createTempMappings();
        
        flyCam.setEnabled(false);
    }
    
    
    private void createTempMappings(){
        // temp input manager mappings;
        inputManager.addMapping("MoveForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("MoveBackward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        
    }
    

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        if(!settingsScreenOn){
            appState.gotoScreen("settings");
            settingsScreenOn = true;
        }
    }
}
