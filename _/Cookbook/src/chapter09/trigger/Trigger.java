/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author reden
 */
public class Trigger implements ScriptObject{
    
    private boolean enabled;
    private float delay;
    private boolean triggered;
    private float timer;

    protected HashMap<String, ScriptObject> targets = new HashMap<String, ScriptObject>();
    
    public void onTrigger(){
        if(enabled){
            Collection<ScriptObject> connections = targets.values();
        
            for(ScriptObject scriptObject: connections){
                scriptObject.trigger();
            }
        }
        triggered = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void update(float tpf) {
        if(!enabled){
            return;
        } else if(triggered && delay > 0){
            timer += tpf;
            if(timer >= delay){
                onTrigger();
            }
        } else if(triggered){
            onTrigger();
        }
    }

    public void trigger() {
        if(!enabled){
            return;
        } else {
            timer = 0;
            triggered = true;
        }
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }
    
    
    
}
