/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import chapter06.controller.DialogScreenController;
import chapter06.controller.LoadingScreenController;
import chapter06.state.NiftyAppState;

/**
 *
 * @author reden
 */
public class TestDialogScreen extends SimpleApplication{

    private boolean dialogScreenOn;
    private NiftyAppState niftyState;
    
    public static void main(String[] args){
        TestDialogScreen test = new TestDialogScreen();
        test.start();
    }
    
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.Gray);
        niftyState = new NiftyAppState();
        stateManager.attach(niftyState);
        
        flyCam.setEnabled(false);
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        if(!dialogScreenOn){
            niftyState.gotoScreen("dialogScreen");
            ((DialogScreenController)niftyState.getNifty().getScreen("dialogScreen").getScreenController()).setDialogNode(new DialogNode());
            dialogScreenOn = true;
        }
    }
 
    
}
