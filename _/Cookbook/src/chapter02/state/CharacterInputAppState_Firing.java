/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.state;

import chapter05.TestAiControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;
import chapter02.CharacterInputTest_Firing;
import chapter02.control.GameCharacterControl;
import chapter02.control.GameCharacterControl_Firing;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rickard
 */
public class CharacterInputAppState_Firing extends AbstractAppState implements AnalogListener, ActionListener{
    
    private Application app;
    private InputManager inputManager;
    private GameCharacterControl_Firing character;
    private float sensitivity = 5000;
    
    /**
     * new
     */
    
    List<Geometry> targets = new ArrayList<Geometry>();
    
    /**
     * end new
     */

    public enum InputMapping{
        RotateLeft,
        RotateRight,
        LookUp,
        LookDown,
        StrafeLeft,
        StrafeRight,
        MoveForward,
        MoveBackward,
        Fire;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        this.inputManager = app.getInputManager();
        addInputMappings();
    }
    
    private void addInputMappings(){
        inputManager.addMapping(InputMapping.RotateLeft.name(), new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(InputMapping.RotateRight.name(), new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(InputMapping.LookUp.name(), new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(InputMapping.LookDown.name(), new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(InputMapping.StrafeLeft.name(), new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(InputMapping.StrafeRight.name(), new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(InputMapping.MoveForward.name(), new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(InputMapping.MoveBackward.name(), new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(KeyInput.KEY_DOWN));
        /**
         * new
         */
        inputManager.addMapping(InputMapping.Fire.name(), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        /**
         * end new
         */
        for (InputMapping i : InputMapping.values()) {
            inputManager.addListener(this, i.name());
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        for (InputMapping i : InputMapping.values()) {
            if (inputManager.hasMapping(i.name())) {
                inputManager.deleteMapping(i.name());
            }
        }
        inputManager.removeListener(this);
    }
    
    public void onAnalog(String name, float value, float tpf) {
        if(character != null){
            character.onAnalog(name, value * sensitivity, tpf);
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        
        if(character != null){
            /**
            * new
            */
           if (name.equals("Fire")) {
               if (isPressed && character.getCooldown() == 0f){
                   fire();
               }
           } else {
           /**
            * end new
            */
            character.onAction(name, isPressed, tpf);
           }
        }
    }
    
    public void setCharacter(GameCharacterControl_Firing character){
        this.character = character;
    }
    
    /**
     * new
     */
    
    public void setTargets(List<Geometry> targets){
        this.targets = targets;
    }
    
    public void fire(){
        if(character != null){
            Ray r = new Ray(app.getCamera().getLocation(), app.getCamera().getDirection());
            
            CollisionResults collRes = new CollisionResults();
            for(Geometry g: targets){
                g.collideWith(r, collRes);
            }
            if(collRes.size() > 0){
                System.out.println("hit");
            }
            character.onFire();
        }
    }
    /**
     * end new
     */
    
}
