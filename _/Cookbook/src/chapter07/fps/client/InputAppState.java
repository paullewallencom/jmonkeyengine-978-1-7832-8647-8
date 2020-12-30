/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.client;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import chapter02.control.GameCharacterControl;
import chapter07.fps.common.message.PlayerActionMessage;

/**
 *
 * @author Rickard
 */
public class InputAppState extends AbstractAppState implements AnalogListener, ActionListener{
    
    private Application app;
    private InputManager inputManager;
    private FPSClient client;
    private float sensitivity = 5000;

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
    
    private String[] mappingNames = new String[]{InputMapping.RotateLeft.name(), InputMapping.RotateRight.name(), InputMapping.LookUp.name(), InputMapping.LookDown.name(), InputMapping.StrafeLeft.name(), InputMapping.StrafeRight.name(), InputMapping.MoveForward.name(), InputMapping.MoveBackward.name(), InputMapping.Fire.name()};

    public FPSClient getClient() {
        return client;
    }

    public void setClient(FPSClient client) {
        this.client = client;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        this.inputManager = app.getInputManager();
        addInputMappings();
        assignJoysticks();
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
        inputManager.addMapping(InputMapping.Fire.name(), new KeyTrigger(KeyInput.KEY_SPACE), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(this, mappingNames);
        
    }
    
    private void assignJoysticks(){
        Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null){
            for( Joystick j : joysticks ) {
                for(JoystickAxis axis : j.getAxes()){
                    if(axis == j.getXAxis()){
                        axis.assignAxis(InputMapping.StrafeRight.name(), InputMapping.StrafeLeft.name());
                    } else if ( axis == j.getYAxis()){
                        axis.assignAxis(InputMapping.MoveBackward.name(), InputMapping.MoveForward.name());
                    } else if (axis.getLogicalId().equals("ry")){
                        axis.assignAxis(InputMapping.LookDown.name(), InputMapping.LookUp.name());
                    } else if(axis.getLogicalId().equals("rx")){
                        axis.assignAxis(InputMapping.RotateRight.name(), InputMapping.RotateLeft.name());
                    }
                }
                
                for(JoystickButton button : j.getButtons()){
                    button.assignButton("Fire");
                }
            }

            
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
//        System.out.println(name + isPressed);
        InputMapping input = InputMapping.valueOf(name);
        switch(input){
            case RotateLeft:
            case RotateRight:
            case LookDown:
            case LookUp:
                PlayerActionMessage action = new PlayerActionMessage();
                action.setAction(name);
                action.setFloatValue(value);
                action.setPlayerId(client.getThisPlayer().getId());
                client.send(action);
                break;
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {
//        System.out.println(name + isPressed);
        InputMapping input = InputMapping.valueOf(name);
        switch(input){
            default:
                PlayerActionMessage action = new PlayerActionMessage();
                action.setAction(name);
                action.setPressed(isPressed);
                action.setPlayerId(client.getThisPlayer().getId());
                client.send(action);
                break;
        }
    }
    
}
