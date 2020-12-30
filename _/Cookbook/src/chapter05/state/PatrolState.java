/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.state;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author reden
 */
public class PatrolState extends AIState{
    
    private Vector3f moveTarget;

    public void stateEnter() {
    }

    public void stateExit() {
        aiControl.move(Vector3f.ZERO, false);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(aiControl.getTarget() != null){
            this.setEnabled(false);
            
            /**
             * Cover seeking
             */
            if(spatial.getControl(SeekCoverState.class) != null){
                spatial.getControl(SeekCoverState.class).setEnabled(true);
            } 
            /**
             * / Cover seeking
             */
            else {
                Vector3f direction = aiControl.getTarget().getWorldTranslation().subtract(spatial.getWorldTranslation());
                this.spatial.getControl(BetterCharacterControl.class).setViewDirection(direction);
                this.spatial.getControl(AttackState.class).setEnabled(true);
            }
            
        } else if(moveTarget == null || this.spatial.getWorldTranslation().distance(moveTarget) < 1f){
            float x = (FastMath.nextRandomFloat() - 0.5f) * 2f;
            moveTarget = new Vector3f(x, 0, (1f - FastMath.abs(x)) - 0.5f).multLocal(5f);
            moveTarget.addLocal(this.spatial.getWorldTranslation());
        } else {
            Vector3f direction = moveTarget.subtract(this.spatial.getWorldTranslation()).normalizeLocal();
            aiControl.move(direction, true);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
