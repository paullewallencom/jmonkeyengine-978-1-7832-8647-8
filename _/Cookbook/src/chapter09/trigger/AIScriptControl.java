/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger;

import chapter05.control.AIControl;
import chapter05.control.AIControl_SM;
import chapter05.state.AIState;
import com.jme3.scene.Spatial;

/**
 *
 * @author reden
 */
public class AIScriptControl implements ScriptObject{
    
    private AIControl_SM ai;
    private Class<AIState> targetState;
    private Spatial target;
    private boolean enabled;

    public void update(float tpf) {
    }

    public void trigger() {
        if(enabled){
            onTrigger();
        }
    }

    public void onTrigger() {
        if(targetState != null){
            ai.setState(targetState);
        }
        if(target != null){
            ai.setTarget(target);
        }
    }
    
    
}
