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
public class TestOptionsMenu extends SimpleApplication{

    public static void main(String[] args){
        TestOptionsMenu test = new TestOptionsMenu();
        test.start();
    }
    
    @Override
    public void simpleInitApp() {
        
        stateManager.attach(new NiftyAppState());
        
        flyCam.setEnabled(false);
    }
 
}
