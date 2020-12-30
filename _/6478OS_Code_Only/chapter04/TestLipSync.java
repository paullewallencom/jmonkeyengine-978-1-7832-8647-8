/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04;

import chapter04.control.AnimationChannelsControl;
import chapter04.control.AnimationChannelsControl.Animation;
import chapter04.control.AnimationChannelsControl.Channel;
import chapter04.control.EyeTrackingControl;
import chapter04.control.ExpressionsControl;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimationFactory;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.system.AppSettings;

/**
 *
 * @author Rickard
 */
public class TestLipSync extends SimpleApplication implements ActionListener{

    private Spatial jaime;
    
    private CameraControl camControl = new CameraControl();
    private CameraNode camNode = new CameraNode();
    Cinematic cinematicHello;
    
    public static void main(String[] args) {
        TestLipSync main = new TestLipSync();
        AppSettings s = new AppSettings(false);
        s.setFrameRate(60);
        main.settings = s;
        main.start();
    }
    
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.White);
        jaime = assetManager.loadModel("Models/Jaime/Jaime_Phoneme.j3o");
        jaime.addControl(new ExpressionsControl());
        jaime.addControl(new EyeTrackingControl());
        
        rootNode.attachChild(jaime);

        DirectionalLight dirLight = new DirectionalLight();
        dirLight.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f));
        rootNode.addLight(dirLight);
        
        setupKeys();
        
        camControl.setCamera(cam);
        camControl.setControlDir(CameraControl.ControlDirection.CameraToSpatial);
        camNode.addControl(camControl);
        rootNode.attachChild(camNode);
        jaime.getControl(EyeTrackingControl.class).setLookAtObject(camNode);
        flyCam.setMoveSpeed(40f);
                
        setupHelloCinematic();
    }


    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Anim1") && isPressed) {
            cinematicHello.play();
            
        } else if (name.equals("Anim2") && isPressed) {
        }
    }
    
    private void setupKeys() {
        inputManager.addMapping("Anim1",
                new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Anim2",
                new KeyTrigger(KeyInput.KEY_2));
        
        
        inputManager.addListener(this, "Anim1", "Anim2");
    }
    
    
    
    public void setupHelloCinematic() {
        cinematicHello = new Cinematic((Node)jaime, 1.5f);
        stateManager.attach(cinematicHello);

        cinematicHello.addCinematicEvent(0.0f, new AnimationEvent(jaime, "Expression_HAPPY", LoopMode.Cycle, 2, 0.2f));
        cinematicHello.addCinematicEvent(0.0f, new AnimationEvent(jaime, "Phoneme_RESET", LoopMode.DontLoop, 1, 0.0f));
        cinematicHello.addCinematicEvent(0.0f, new AnimationEvent(jaime, "Phoneme_EEE", LoopMode.DontLoop, 1, 0.2f));
        cinematicHello.addCinematicEvent(0.2f, new AnimationEvent(jaime, "Phoneme_LUH", LoopMode.DontLoop, 1, 0.1f));
        cinematicHello.addCinematicEvent(0.3f, new AnimationEvent(jaime, "Phoneme_OOOH", LoopMode.DontLoop, 1, 0.1f));
        cinematicHello.addCinematicEvent(0.7f, new AnimationEvent(jaime, "Phoneme_RESET", LoopMode.DontLoop, 1, 0.2f));
//        cinematicHello.addCinematicEvent(1.0f, new AnimationEvent(jaime, "Expression_NEUTRAL", LoopMode.Cycle, 2, 0.4f));
        cinematicHello.fitDuration();
        cinematicHello.setSpeed(1.0f);
        cinematicHello.setLoopMode(LoopMode.DontLoop);
    }
}
