/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.control;

import chapter05.TestAiControl;
import chapter05.state.AIState;
import chapter05.state.AttackState;
import chapter05.state.PatrolState;
import chapter05.state.RetreatState;
import chapter05.state.SeekCoverState;
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
public class AIControl_SM extends AbstractControl{

    private GameCharacterControl physicsCharacter;
    private Spatial target;
    boolean forward = false, backward = false, leftRotate = false, rightRotate = false;
    private Vector3f viewDirection = new Vector3f(0, 0, 1);
    
    private List<Spatial> targetableObjects = new ArrayList<Spatial>();

    
    @Override
    protected void controlUpdate(float tpf) {
        if(!targetableObjects.isEmpty()){
            sense();
        } else {
            target = null;
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
        
        this.spatial.addControl(new PatrolState());
        this.spatial.addControl(new AttackState());
        this.spatial.addControl(new RetreatState());
        /**
         * Cover
         */
        this.spatial.addControl(new SeekCoverState());
        this.spatial.getControl(SeekCoverState.class).setEnabled(false);
        /**
         * /Cover
         */
        this.spatial.getControl(PatrolState.class).setEnabled(true);
        this.spatial.getControl(AttackState.class).setEnabled(false);
        this.spatial.getControl(RetreatState.class).setEnabled(false);
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
    
    private float sightRange = 10f;
    private float angle = FastMath.QUARTER_PI;
    private int height = 50;
    private Geometry[] sightLines = new Geometry[30];
    private boolean debug = false;
    
    private boolean sense(){
        target = null;
        Quaternion aimDirection = new Quaternion();
        Vector3f rayDirection = new Vector3f();
        int i = 0;
        boolean foundTarget = false;
        for(float x = -angle; x < angle; x+= FastMath.QUARTER_PI * 0.1f){
            if(debug && sightLines[i] != null){
                ((Node)getSpatial().getParent()).detachChild(sightLines[i]);
            }
            rayDirection.set(viewDirection);
            aimDirection.fromAngleAxis(x, Vector3f.UNIT_Y);
            aimDirection.multLocal(rayDirection);
            Ray ray = new Ray(spatial.getWorldTranslation().add(0, 1f, 0), rayDirection);
            ray.setLimit(sightRange);
            CollisionResults col = new CollisionResults();
            for(Spatial s: targetableObjects){
                s.collideWith(ray, col);
            }
            
            if(col.size() > 0){
                target = col.getClosestCollision().getGeometry();
                foundTarget = true;
                break;
            }
            
            if(debug){
                Geometry line = makeDebugLine(ray);
                sightLines[i++] = line;
                ((Node)getSpatial().getParent()).attachChild(line);
            }
        }
        return foundTarget;
    }
    
    private Geometry makeDebugLine(Ray r){
        Line l = new Line(r.getOrigin(), r.getOrigin().add(r.getDirection().mult(sightRange)));
        Geometry line = new Geometry("", l);
        line.setMaterial(TestAiControl.lineMat);
        return line;
    }
    
    public void move(Vector3f direction, boolean move){
        if(move){
            viewDirection.set(direction);
        }
        physicsCharacter.onAction("MoveForward", move, 1);
    }
    
    public void setState(Class<AIState> state){
        if(spatial.getControl(state) != null){
            spatial.getControl(state).setEnabled(true);
        }
    }
}
