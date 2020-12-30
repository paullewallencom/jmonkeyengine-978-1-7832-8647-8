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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rickard
 */
public class AIControl extends AbstractControl{

    private BetterCharacterControl physicsCharacter;
    private Spatial target;
    boolean forward = false, backward = false, leftRotate = false, rightRotate = false;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
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
                forward = false;
                backward = false;
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
                    } else if(distance > 5f){
                        forward = true;
                        backward = false;
                    } else if (distance < 3f){
                        forward = false;
                        backward = true;
                    } else {
                        forward = false;
                        backward = false;
                    }
                }
                break;
        }
        
        Vector3f modelForwardDir = spatial.getWorldRotation().mult(Vector3f.UNIT_Z);
        walkDirection.set(0, 0, 0);
        if (forward) {
            walkDirection.addLocal(modelForwardDir.mult(3));
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(3));
        }
        physicsCharacter.setWalkDirection(walkDirection);

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

    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        physicsCharacter = spatial.getControl(BetterCharacterControl.class);
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
    
    
    private float sightRange = 10f;
    private float width = FastMath.QUARTER_PI;
    private int height = 50;
    private Geometry[] sightLines = new Geometry[30];
    private boolean debug = true;
   
}
