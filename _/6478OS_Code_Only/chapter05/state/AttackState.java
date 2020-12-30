/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.state;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author reden
 */
public class AttackState extends AIState{

    private float fireDistance = 10f;
    private int clip;
    private int ammo;
    private float fireCooldown = 2f;
    
    public void stateEnter() {
        clip = 5;
        ammo = 10;
    }

    public void stateExit() {
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(clip == 0){
            if(ammo == 0){
                this.setEnabled(false);
                this.spatial.getControl(RetreatState.class).setEnabled(true);
            } else {
                clip += 5;
                ammo -= 5;
                fireCooldown = 5f;
                System.out.println("Reload");
            }
        } else if(aiControl.getTarget() == null){
            this.setEnabled(false);
            this.spatial.getControl(PatrolState.class).setEnabled(true);
        } else if(fireCooldown <= 0f && aiControl.getSpatial().getWorldTranslation().distance(aiControl.getTarget().getWorldTranslation()) < fireDistance){
            clip--;
            fireCooldown = 2f;
            System.out.println("Fire");
        } else if(fireCooldown > 0f){
            fireCooldown -= tpf;
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
