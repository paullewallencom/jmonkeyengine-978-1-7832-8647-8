/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.control;

import chapter05.TestAiControl;
import chapter05.state.AIAppState;
import chapter05.state.AIState;
import chapter05.state.AIStateRTS;
import chapter05.state.AttackState;
import chapter05.state.GatherFoodState;
import chapter05.state.GatherWoodState;
import chapter05.state.PatrolState;
import chapter05.state.RetreatState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Line;
import chapter02.control.GameCharacterControl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rickard
 */
public class AIControl_RTS extends AbstractControl{

    private GameCharacterControl physicsCharacter;
    private Spatial target;
    boolean forward = false, backward = false, leftRotate = false, rightRotate = false;
    private Vector3f viewDirection = new Vector3f(0, 0, 1);
    
    private AIStateRTS currentState;
    private AIAppState aiManager;
    
    @Override
    protected void controlUpdate(float tpf) {
        if (leftRotate) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
        } else if (rightRotate) {
            Quaternion rotateR = new Quaternion().fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateR.multLocal(viewDirection);
        }
        physicsCharacter.setViewDirection(viewDirection);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        physicsCharacter = spatial.getControl(GameCharacterControl.class);
        
        this.spatial.addControl(new GatherFoodState());
        this.spatial.addControl(new GatherWoodState());
        this.spatial.getControl(GatherFoodState.class).setEnabled(false);
        this.spatial.getControl(GatherWoodState.class).setEnabled(false);
    }

    public Spatial getTarget() {
        return target;
    }

    public void setTarget(Spatial target) {
        this.target = target;
    }
    
    public void move(Vector3f direction, boolean move){
        if(move){
            viewDirection.set(direction);
        }
        physicsCharacter.onAction("MoveForward", move, 1);
    }

    public AIStateRTS getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Class<? extends AIStateRTS> newState) {
        AIStateRTS state = spatial.getControl(newState);
        if(state == null){
            return; // ai doesn't have this state
        }
        if(this.currentState != null && this.currentState.getClass() != newState){
            this.currentState.setEnabled(false);
        }
        this.currentState = state;
        this.currentState.setEnabled(true);
    }

    public AIAppState getAiManager() {
        return aiManager;
    }

    public void setAiManager(AIAppState aiManager) {
        this.aiManager = aiManager;
    }

}
