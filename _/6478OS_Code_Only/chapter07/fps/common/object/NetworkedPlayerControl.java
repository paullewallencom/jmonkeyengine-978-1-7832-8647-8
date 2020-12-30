/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.common.object;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import chapter07.fps.common.message.PlayerMessage;

/**
 *
 * @author reden
 */
public abstract class NetworkedPlayerControl extends AbstractControl{
    
    protected int id;
    public final static int MOVE_SPEED = 3;
    protected Vector3f tempLocation = new Vector3f();
    protected Quaternion tempRotation = new Quaternion();
    protected float yaw;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void onMessageReceived(PlayerMessage message);
    
    
    public float getYaw(){
        return yaw;
    }
}
