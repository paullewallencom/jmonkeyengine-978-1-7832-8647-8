/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.endless;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author reden
 */
public class EndlessAppState extends AbstractAppState{

    private EndlessWorldControl worldControl;
    private Node worldNode;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        worldNode = new Node("World");
        Material m = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");

        ((SimpleApplication)app).getRootNode().attachChild(worldNode);
        
        worldControl = new EndlessWorldControl();
        
        worldControl.setMaterial(m);
        worldNode.addControl(worldControl);
        worldControl.setCamera(app.getCamera());
        
        app.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_UP));
        app.getInputManager().addMapping("Back", new KeyTrigger(KeyInput.KEY_DOWN));
        app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        
        app.getInputManager().addListener(worldControl, "Forward", "Back", "Left", "Right");
        
        
    }


    @Override
    public void update(float tpf) {
        super.update(tpf);
    }
    
    
}
