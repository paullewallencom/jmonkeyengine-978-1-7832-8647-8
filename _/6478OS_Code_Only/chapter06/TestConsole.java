/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

import com.jme3.app.SimpleApplication;
import de.lessvoid.nifty.tools.Color;
import chapter06.controller.NiftyController;
import chapter06.state.NiftyAppState;

/**
 *
 * @author reden
 */
public class TestConsole extends SimpleApplication{

    public static void main(String[] args){
        TestConsole test = new TestConsole();
        test.start();
    }
    
    boolean output = false;
    NiftyAppState appState;
    @Override
    public void simpleInitApp() {
        appState = new NiftyAppState();
        stateManager.attach(appState);
        
        flyCam.setEnabled(false);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        if(!output){
            ((NiftyController)appState.getNifty().getScreen("main").getScreenController()).outputToConsole("HELLO");
            output = true;
        }
    }
 
    
    
}
