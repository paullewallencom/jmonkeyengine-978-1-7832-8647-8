/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger;

/**
 *
 * @author reden
 */
public interface ScriptObject {
    
    void update(float tpf);
    
    void trigger();
    
    void onTrigger();
}
