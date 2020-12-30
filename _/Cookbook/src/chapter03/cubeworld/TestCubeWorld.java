/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.cubeworld;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author reden
 */
public class TestCubeWorld extends SimpleApplication{

    public static void main(String[] args){
        TestCubeWorld testWorld = new TestCubeWorld();
        testWorld.start();
    }
    
    @Override
    public void simpleInitApp() {
        
        
        flyCam.setMoveSpeed(50);
        
        viewPort.setBackgroundColor(ColorRGBA.White);
        
        
        CubeWorldAppState cubeState = new CubeWorldAppState();
        stateManager.attach(cubeState);
    }
    
    
    
    
    
    

    
    
}
