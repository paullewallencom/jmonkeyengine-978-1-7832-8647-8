/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter09.trigger;

import com.jme3.effect.ParticleEmitter;
import java.util.HashMap;

/**
 *
 * @author Rickard
 */
public class PlayEffect implements ScriptObject{

    private boolean emitAllParticles;

    private ParticleEmitter effect;
    
    private boolean enabled = true;

    public void update(float tpf) {
    }

    public void setEffect(ParticleEmitter effect, boolean emitAll){
        this.effect = effect;
        this.emitAllParticles = emitAll;
    }

    public void trigger() {
        if(enabled){
            onTrigger();
        }
    }

    public void onTrigger() {
        if(effect != null){
            effect.setEnabled(true);
            if(emitAllParticles){
                effect.emitAllParticles();
            }
        }
    }
    
    public void stop(){
        if(effect != null){
            effect.setEnabled(false);
        }
    }
}
