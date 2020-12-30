/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08.control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class ArrowFacingControl extends AbstractControl{

    private Vector3f direction;
    
    @Override
    protected void controlUpdate(float tpf) {
        direction = spatial.getControl(RigidBodyControl.class).getLinearVelocity().normalize();
        spatial.rotateUpTo(direction);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
