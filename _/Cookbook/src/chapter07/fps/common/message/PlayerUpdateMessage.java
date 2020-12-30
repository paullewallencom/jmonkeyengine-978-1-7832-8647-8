/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.common.message;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author reden
 */
@Serializable()
public class PlayerUpdateMessage extends PlayerMessage{
    
    private Vector3f position;
    private Quaternion lookDirection;
    private float yaw;
    /**
     * Visibility
     */
    private boolean visible = false; // change this depending on whether running with visibility check on or not
    /**
     * /Visibility
     */
    
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Quaternion getLookDirection() {
        return lookDirection;
    }

    public void setLookDirection(Quaternion lookDirection) {
        this.lookDirection = lookDirection;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    
    /**
     * /Visibility
     */
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    /**
     * Visibility
     */
    

}
