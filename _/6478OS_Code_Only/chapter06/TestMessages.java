/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.tools.Color;
import chapter06.controller.DialogScreenController;
import chapter06.controller.MainScreenController;
import chapter06.controller.NiftyController;
import chapter06.state.NiftyAppState;

/**
 *
 * @author reden
 */
public class TestMessages extends SimpleApplication implements ActionListener{

    public static void main(String[] args){
        TestMessages test = new TestMessages();
        test.start();
    }
    
    boolean output = false;
    NiftyAppState appState;
    @Override
    public void simpleInitApp() {
        appState = new NiftyAppState();
        stateManager.attach(appState);
        
        inputManager.addMapping("AddMessage", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(this, "AddMessage");
        
        inputManager.addMapping("AddWindow", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addListener(this, "AddWindow");
        
        flyCam.setEnabled(false);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
       
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("AddMessage") && !isPressed){
            ((MainScreenController)appState.getNifty().getScreen("main").getScreenController()).addMessage("Lorem Ipsum Dolor");
        } else if(name.equals("AddWindow") && !isPressed){
            ((MainScreenController)appState.getNifty().getScreen("main").getScreenController()).showMessageWindow("All your base are belong to us");
        }
    }
}
