/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.state;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import chapter02.control.ChaseCamCharacter;
import chapter02.control.GameCharacterControl;

/**
 *
 * @author Rickard
 */
public class CharacterInputAppStateChase extends AbstractAppState implements AnalogListener, ActionListener{
    
    private Application app;
    private InputManager inputManager;
    private ChaseCamCharacter character;
    private float sensitivity = 5000;
    
    /**
     * 
     */
    private ChaseCamera chaseCam;
    /**
     * 
     */

    public enum InputMapping{
        ToggleCover,
        RotateLeft,
        RotateRight,
        LookUp,
        LookDown,
        StrafeLeft,
        StrafeRight,
        MoveForward,
        MoveBackward;
    }
    
    private String[] mappingNames = new String[]{InputMapping.ToggleCover.name(), InputMapping.RotateLeft.name(), InputMapping.RotateRight.name(), InputMapping.LookUp.name(), InputMapping.LookDown.name(), InputMapping.StrafeLeft.name(),
        InputMapping.StrafeRight.name(), InputMapping.MoveForward.name(), InputMapping.MoveBackward.name(), ChaseCamera.ChaseCamUp, ChaseCamera.ChaseCamDown, ChaseCamera.ChaseCamMoveLeft, ChaseCamera.ChaseCamMoveRight};
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        this.inputManager = app.getInputManager();
        addInputMappings();
        assignJoysticks();
    }
    
    private void addInputMappings(){
        inputManager.addMapping(InputMapping.ToggleCover.name(), new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping(InputMapping.StrafeLeft.name(), new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(InputMapping.StrafeRight.name(), new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(InputMapping.MoveForward.name(), new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(InputMapping.MoveBackward.name(), new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(KeyInput.KEY_DOWN));
        
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
                        axis.assignAxis(ChaseCamera.ChaseCamUp, ChaseCamera.ChaseCamDown);
                    } else if(axis.getLogicalId().equals("rx")){
                        axis.assignAxis(ChaseCamera.ChaseCamMoveRight, ChaseCamera.ChaseCamMoveLeft);
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
        if(character != null){
            character.onAnalog(name, value * sensitivity, tpf);
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if(character != null){
//            
//            if(name.equals(InputMapping.ToggleCover.name()) && !character.isInCover() && isPressed){
//                character.checkCover(structures);
//            } else {
                character.onAction(name, isPressed, tpf);
//            }
            
        }
    }
    
    public void setCharacter(GameCharacterControl character){
        this.character = (ChaseCamCharacter) character;
    }
    
    /**
     * 
     */
    
    public void setChaseCamera(ChaseCamera cam){
        this.chaseCam = cam;
    }
    
    /**
     * 
     */
    
    /**
     * Cover
     */

//    private void checkCover(){
//        CollisionResults collRes = new CollisionResults();
//
//        Spatial player = character.getSpatial();
//        Vector3f viewDirection = character.getViewDirection();
//        Vector3f left = player.getWorldRotation().getRotationColumn(0).mult(playerWidth);
//        left.setY(0.5f);
//
//        Ray ray = new Ray();
//        ray.setDirection(viewDirection);
//        ray.setLimit(0.6f);
//
//        int collisions = 0;
//        for(int i = -1; i < 2; i++){
//            left.multLocal(i, 1, i);
//            ray.setOrigin(player.getWorldTranslation().add(left));
//            structures.collideWith(ray, collRes);
//            if(collRes.size() > 0){
//                collisions++;
//            }
//            collRes.clear();
//        }
//        System.out.println("collisions" + collisions);
//        if(collisions == 3){
//            ray.setOrigin(player.getWorldTranslation().add(0, 0.5f, 0));
//            structures.collideWith(ray, collRes);
//
//            Triangle t = new Triangle();
//            collRes.getClosestCollision().getTriangle(t);
//            t.calculateNormal();
//            this.character.alignWithCover(t.getNormal());
//        }
//    }
    
//    private void toggleCamera(){
//        if(character.isInCover()){
//            chaseCam.setDefaultDistance(2f);
//            chaseCam.setMaxDistance(2.5f);
//            chaseCam.setMinDistance(1.5f);
//            chaseCam.setChasingSensitivity(50);
//            chaseCam.setTrailingSensitivity(50);
//            chaseCam.setMaxVerticalRotation(FastMath.QUARTER_PI);
//            chaseCam.setMinVerticalRotation(FastMath.QUARTER_PI * 0.5f);
//            chaseCam.setTrailingEnabled(true);
//            
//        } else {
//            chaseCam.setDragToRotate(false);
//            chaseCam.setSmoothMotion(true);
//            chaseCam.setLookAtOffset(new Vector3f(0, 1f, 0));
//            chaseCam.setDefaultDistance(7f);
//            chaseCam.setMaxDistance(8f);
//            chaseCam.setMinDistance(6f);
//            chaseCam.setTrailingSensitivity(5);
//            chaseCam.setChasingSensitivity(10);
//            chaseCam.setRotationSpeed(10);
//        }
//    }
    /**
     * 
     */
    
    
    // there's more: restrict movement along edge
}
