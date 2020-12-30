/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.control;

import com.jme3.effect.ParticleEmitter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class WeaponControl extends AbstractControl{

    private ParticleEmitter muzzleFlash;
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        
        if(spatial instanceof Node){
            muzzleFlash = (ParticleEmitter) ((Node)spatial).getChild("MuzzleFlash");
        }
    }
    
    public void onFire(){
        if(muzzleFlash != null){
            muzzleFlash.emitAllParticles();
        }
    }
}
