/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.effect.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import java.io.IOException;

/**
 *
 * @author Rickard
 */
public class BirdParticleInfluencer extends DefaultParticleInfluencer{
    
    private int maxImages = 1;
    private float animationFps = 10f;
    private float time = 0f;
    private int increaseFrames;
    
    @Override
    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
        super.influenceParticle(particle, emitterShape);
        particle.velocity.setY(0);
        particle.velocity.normalizeLocal();
    }

    @Override
    public void influenceRealtime(Particle particle, float tpf) {
        super.influenceRealtime(particle, tpf);
        if(increaseFrames > 0){
             particle.imageIndex = (particle.imageIndex + increaseFrames) % maxImages;
        }
//        particle.imageIndex = currentImage;
    }
    
    @Override
    public void update(float tpf){
        super.update(tpf);
        float interval = 1f / animationFps;
        time += tpf;
        increaseFrames = 0;
        while (time > interval){
            increaseFrames++;
            time -= interval;
        }
    }

    public int getMaxImages() {
        return maxImages;
    }

    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    public float getAnimationFps() {
        return animationFps;
    }

    public void setAnimationFps(float animationFps) {
        this.animationFps = animationFps;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(initialVelocity, "initialVelocity", Vector3f.ZERO);
        oc.write(velocityVariation, "variation", 0.2f);
        oc.write(maxImages, "maxImages", 1);
        oc.write(animationFps, "animationFps", 10f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        // NOTE: In previous versions of jME3, initialVelocity was called startVelocity
        if (ic.getSavableVersion(DefaultParticleInfluencer.class) == 0){
            initialVelocity = (Vector3f) ic.readSavable("startVelocity", Vector3f.ZERO.clone());
        }else{
            initialVelocity = (Vector3f) ic.readSavable("initialVelocity", Vector3f.ZERO.clone());
        }       
        velocityVariation = ic.readFloat("variation", 0.2f);
        maxImages = ic.readInt("maxImages", 1);
        animationFps = ic.readFloat("animationFps", 10f);
    }

    @Override
    public ParticleInfluencer clone() {
            BirdParticleInfluencer clone = (BirdParticleInfluencer) super.clone();
            clone.initialVelocity = initialVelocity.clone();
            clone.maxImages = maxImages;
            clone.animationFps = animationFps;
            return clone;
    }
}
