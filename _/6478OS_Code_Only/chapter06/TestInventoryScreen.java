/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import chapter06.state.NiftyAppState;

/**
 *
 * @author reden
 */
public class TestInventoryScreen extends SimpleApplication{

    private boolean inventoryScreenOn;
    private NiftyAppState niftyState;
    
    public static void main(String[] args){
        TestInventoryScreen test = new TestInventoryScreen();
        test.start();
    }
    
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.White);
        niftyState = new NiftyAppState();
        stateManager.attach(niftyState);
        
        flyCam.setEnabled(false);
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        if(!inventoryScreenOn){
            niftyState.gotoScreen("inventoryScreen");
            inventoryScreenOn = true;
        }
    }
}
