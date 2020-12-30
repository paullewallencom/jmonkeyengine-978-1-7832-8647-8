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
import com.jme3.animation.Bone;
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
public class TestEyeTracking extends SimpleApplication{

    private Spatial jaime;
    
    private CameraControl camControl = new CameraControl();
    private CameraNode camNode = new CameraNode();
    
    public static void main(String[] args) {
        TestEyeTracking main = new TestEyeTracking();
        AppSettings s = new AppSettings(false);
        s.setFrameRate(60);
        main.settings = s;
        main.start();
    }
    
    @Override
    public void simpleInitApp() {
        jaime = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        jaime.addControl(new EyeTrackingControl());
        
        rootNode.attachChild(jaime);
        
        
        DirectionalLight dirLight = new DirectionalLight();
        dirLight.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f));
        rootNode.addLight(dirLight);
        
        camControl.setCamera(cam);
        camControl.setControlDir(CameraControl.ControlDirection.CameraToSpatial);
        camNode.addControl(camControl);
        rootNode.attachChild(camNode);
        jaime.getControl(EyeTrackingControl.class).setLookAtObject(camNode);
        flyCam.setMoveSpeed(20f);
    }


}
