/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.common.message;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author reden
 */
@Serializable()
public class PlayerActionMessage extends PlayerMessage{
    
    private String action;
    private boolean pressed;
    private float floatValue;
    
    public PlayerActionMessage(){
        setReliable(true);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }
}
