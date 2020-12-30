/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04;

import chapter04.control.AnimationChannelsControl;
import chapter04.control.AnimationChannelsControl.Animation;
import chapter04.control.AnimationChannelsControl.Channel;
import chapter04.control.EyeTrackingControl;
import com.jme3.app.SimpleApplication;
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

/**
 *
 * @author Rickard
 */
public class TestAnimManager extends SimpleApplication implements ActionListener{

    private Spatial jamie;
    
    private CameraControl camControl = new CameraControl();
    private CameraNode camNode = new CameraNode();
    
    public static void main(String[] args) {
        TestAnimManager main = new TestAnimManager();
        
        main.start();
    }
    
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.White);
        jamie = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        jamie.addControl(new AnimationChannelsControl());
        
        rootNode.attachChild(jamie);
        
        DirectionalLight dirLight = new DirectionalLight();
        dirLight.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f));
        rootNode.addLight(dirLight);
        
        setupKeys();
        getFlyByCamera().setMoveSpeed(25f);
        camControl.setCamera(cam);
        camControl.setControlDir(CameraControl.ControlDirection.CameraToSpatial);
        camNode.addControl(camControl);
        rootNode.attachChild(camNode);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
    }
    
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Anim1") && isPressed) {
            jamie.getControl(AnimationChannelsControl.class).setAnimation(Animation.Walk, Channel.All);
        } else if (name.equals("Anim2") && isPressed) {
            jamie.getControl(AnimationChannelsControl.class).setAnimation(Animation.SideKick, Channel.Lower);
        } else if (name.equals("Anim3") && isPressed) {
            jamie.getControl(AnimationChannelsControl.class).setAnimation(Animation.Punches, Channel.Upper);
        } else if (name.equals("Anim4") && isPressed) {
            jamie.getControl(AnimationChannelsControl.class).setAnimation(Animation.JumpStart, Channel.Upper);
        }
    }
    
    private void setupKeys() {
        inputManager.addMapping("Anim1",
                new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Anim2",
                new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Anim3",
                new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Anim4",
                new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Anim5",
                new KeyTrigger(KeyInput.KEY_5));
        
        inputManager.addListener(this, "Anim1", "Anim2", "Anim3", "Anim4", "Anim5");
    }
}
