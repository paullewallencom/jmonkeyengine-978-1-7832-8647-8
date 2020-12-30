/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

import com.jme3.app.SimpleApplication;
import chapter06.state.NiftyAppState;

/**
 *
 * @author reden
 */
public class TestRTSScreen extends SimpleApplication{

    private boolean rtsScreen;
    private NiftyAppState niftyState;
    
    public static void main(String[] args){
        TestRTSScreen test = new TestRTSScreen();
        test.start();
    }
    
    @Override
    public void simpleInitApp() {
        niftyState = new NiftyAppState();
        stateManager.attach(niftyState);
        
        flyCam.setEnabled(false);
    }

    @Override
    public void update() {
        super.update(); //To change body of generated methods, choose Tools | Templates.
        if(!rtsScreen){
            niftyState.gotoScreen("rtsScreen");
            rtsScreen = true;
        }
    }
 
    
}
