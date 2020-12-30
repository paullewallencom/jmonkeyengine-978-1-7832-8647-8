/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04;

import chapter04.control.AnimationChannelsControl;
import chapter04.control.AnimationChannelsControl.Animation;
import chapter04.control.AnimationChannelsControl.Channel;
import chapter04.control.EyeTrackingControl;
import chapter04.control.LeaningControl;
import chapter04.state.CharacterInputAnimationAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;

/**
 *
 * @author Rickard
 */
public class TestLeaning extends SimpleApplication{

    private Spatial jamie;
    
    private CameraControl camControl = new CameraControl();
    private CameraNode camNode = new CameraNode();
    
    public static void main(String[] args) {
        TestLeaning main = new TestLeaning();
        
        main.start();
    }
    
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.White);
        /**
         * Setting up
         */
        stateManager.detach(stateManager.getState(FlyCamAppState.class));
        
        jamie = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        jamie.move(0, 0, 5);
//        jamie.rotate(0, FastMath.PI, 0);
        
        AnimationChannelsControl animControl = new AnimationChannelsControl();
        jamie.addControl(animControl);
        animControl.setAnimation(Animation.Idle);
        
        rootNode.attachChild(jamie);
        
        DirectionalLight dirLight = new DirectionalLight();
        dirLight.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f));
        rootNode.addLight(dirLight);
        
        camControl.setCamera(cam);
        camControl.setControlDir(CameraControl.ControlDirection.CameraToSpatial);
        camNode.addControl(camControl);
        rootNode.attachChild(camNode);
        
        CharacterInputAnimationAppState appState = new CharacterInputAnimationAppState();
        
        stateManager.attach(appState);
        
        /**
         * Leaning control
         */
        
        LeaningControl lc = new LeaningControl();
        jamie.addControl(lc);
        appState.addActionListener(lc);
        appState.addAnalogListener(lc);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
    }
   
}
