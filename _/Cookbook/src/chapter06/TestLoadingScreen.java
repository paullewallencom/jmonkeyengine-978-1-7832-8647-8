/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

import com.jme3.app.SimpleApplication;
import chapter06.controller.LoadingScreenController;
import chapter06.state.NiftyAppState;

/**
 *
 * @author reden
 */
public class TestLoadingScreen extends SimpleApplication{

    private boolean loadingScreenOn;
    private NiftyAppState niftyState;
    
    public static void main(String[] args){
        TestLoadingScreen test = new TestLoadingScreen();
        test.start();
    }
    
    @Override
    public void simpleInitApp() {
        niftyState = new NiftyAppState();
        stateManager.attach(niftyState);
        
        flyCam.setEnabled(false);
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        if(!loadingScreenOn){
            ((LoadingScreenController)niftyState.getNifty().getScreen("loadingScreen").getScreenController()).setLoadingText("Loading Test Scene");
            ((LoadingScreenController)niftyState.getNifty().getScreen("loadingScreen").getScreenController()).setLoadingImage("Interface/Image/loadingScreen.png");
            niftyState.gotoScreen("loadingScreen");
            loadingScreenOn = true;
        }
    }
 
    
}
