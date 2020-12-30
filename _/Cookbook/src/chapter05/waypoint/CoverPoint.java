/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.waypoint;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class CoverPoint extends AbstractControl{

    private boolean crouching;
    private Vector3f coverDirection = new Vector3f();
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public Vector3f getCoverDirection() {
        return coverDirection;
    }

    public void setCoverDirection(Vector3f coverDirection) {
        this.coverDirection = coverDirection;
    }
    
    
}
