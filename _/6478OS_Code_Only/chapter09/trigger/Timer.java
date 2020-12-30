/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author reden
 */
public class Timer implements ScriptObject{
    private boolean enabled = true;
    private boolean running;
    private float time;
    private float lastTime = -1f;
    private float maxTime = -1f;
    private HashMap<Float, TimerEvent> timerEvents = new HashMap<Float, TimerEvent>();

    public void update(float tpf) {
        if(enabled && running){
            time += tpf;
            Iterator<Float> it = timerEvents.keySet().iterator();
            while(it.hasNext()){
                float t = it.next();
                if(t > lastTime && t <= time){
                    TimerEvent event = timerEvents.get(t);
                    if(event != null){
                        event.call();
                    }
                } else if(t < lastTime){
                    continue;
                }
            }
            if(time > maxTime){
                running = false;
            }
            lastTime = time;
        }
    }

    public void trigger() {
        if(enabled){
            onTrigger();
        }
    }

    public void onTrigger() {
        time = 0;
        running = true;
    }

    public void addTimerEvent(float time, TimerEvent callback){
        timerEvents.put(time, callback);
        if(time > maxTime ){
            maxTime = time;
        }
    }

    public boolean isStopped(){
        return running;
    }

    public interface TimerEvent{
        public Object[] call();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    
}
