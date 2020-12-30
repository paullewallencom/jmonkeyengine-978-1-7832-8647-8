/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.control;

import chapter05.TestAiControl;
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
public class AIControl_Hearing extends AbstractControl{

    private GameCharacterControl physicsCharacter;
    private Spatial target;
    boolean forward = false, backward = false, leftRotate = false, rightRotate = false;
    private Vector3f viewDirection = new Vector3f(0, 0, 1);
    
    private List<Spatial> targetableObjects = new ArrayList<Spatial>();
    
    public enum State{
        Idle,
        Follow;
    }
    private State state = State.Idle;
 
    @Override
    protected void controlUpdate(float tpf) {
        switch(state){
            case Idle:
                if(!targetableObjects.isEmpty() && sense()){
                    state = State.Follow;
                }
                break;
            case Follow:
                if(target != null){
                    Vector3f dirToTarget = target.getWorldTranslation().subtract(spatial.getWorldTranslation());
                    dirToTarget.y = 0;
                    dirToTarget.normalizeLocal();
                    viewDirection.set(dirToTarget);
                    
                    float distance = target.getWorldTranslation().distance(spatial.getWorldTranslation());
                    if (distance > 20f){
                        state = State.Idle;
                        target = null;
                        physicsCharacter.onAction("Stop", true, tpf);
                    } else if(distance > 5f){
                        physicsCharacter.onAction("MoveForward", true, tpf);
                    } else if (distance < 3f){
                        physicsCharacter.onAction("MoveBackward", true, tpf);
                    }
                } else {
                    state = State.Idle;
                    physicsCharacter.onAction("Stop", true, tpf);
                }
                break;
        }
        
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
    }

    public Spatial getTarget() {
        return target;
    }

    public void setTarget(Spatial target) {
        this.target = target;
    }
    
    public void setTargetList(List<Spatial> objects){
        targetableObjects = objects;
    }
    
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    
    private float hearingRange = 10f;
    
    private boolean sense(){
        boolean foundTarget = false;
        for(Spatial s: targetableObjects){
            if(s.getControl(SoundEmitterControl.class) != null){
                float distance = s.getWorldTranslation().distance(spatial.getWorldTranslation());
                float noiseEmitted = s.getControl(SoundEmitterControl.class).getNoiseEmitted();
                float distanceFactor = 1f - Math.min(distance, hearingRange) / hearingRange;
//                System.out.println(distanceFactor);
                float soundHeard = distanceFactor * noiseEmitted;
                if(soundHeard > 0.25f){
                    target = s;
                    foundTarget = true;
                    break;
                }
            }
        }
        return foundTarget;
    }
    
}
