/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08.control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class ThrusterControl extends AbstractControl{

    private Spatial thruster;
    
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        thruster = ((Node)getSpatial()).getChild("Thruster");
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void fireBooster(){
        Vector3f direction = spatial.getWorldTranslation().subtract(thruster.getWorldTranslation());
        getSpatial().getControl(RigidBodyControl.class).applyImpulse(direction, direction.negate());
    }
}
