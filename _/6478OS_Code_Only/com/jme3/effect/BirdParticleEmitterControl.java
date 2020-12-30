/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.effect;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author Rickard
 */
public class BirdParticleEmitterControl extends AbstractControl{
    
    @Override
    public void update(float tpf){
        super.update(tpf);
        if(spatial != null && spatial instanceof ParticleEmitter){
            ((ParticleEmitter)spatial).getParticleInfluencer().update(tpf);
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return new BirdParticleEmitterControl();
    }
}
