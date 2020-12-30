/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.state;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author reden
 */
public class RetreatState extends AIState {

    private float fleeTimer = 0f;
    private Vector3f moveTarget;

    public void stateEnter() {
        fleeTimer = 10f;
    }

    public void stateExit() {
        aiControl.move(Vector3f.ZERO, false);
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f worldTranslation = this.spatial.getWorldTranslation();
        if (fleeTimer > 0f && aiControl.getTarget() != null) {
            if (moveTarget == null || worldTranslation.distance(moveTarget) < 1f) {
                moveTarget = worldTranslation.subtract(aiControl.getTarget().getWorldTranslation());
                moveTarget.addLocal(worldTranslation);
            }
            fleeTimer -= tpf;
            Vector3f direction = moveTarget.subtract(worldTranslation).normalizeLocal();
            aiControl.move(direction, true);
        } else {
            this.setEnabled(false);
            this.spatial.getControl(PatrolState.class).setEnabled(true);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
