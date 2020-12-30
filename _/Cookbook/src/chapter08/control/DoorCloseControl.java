/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08.control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class DoorCloseControl extends AbstractControl{

    private HingeJoint joint;
    
    private float timeOpen;
    
    public DoorCloseControl(){
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        float angle = joint.getHingeAngle();
        if(angle > 0.1f || angle < -0.1f){
            timeOpen += tpf;
        } else {
            timeOpen = 0f;
        }
        
        if(timeOpen > 5){
            float speed = angle > 0 ? -0.9f : 0.9f;
            joint.enableMotor(true, speed, 0.1f);
            spatial.getControl(RigidBodyControl.class).activate();
        } else {
            joint.enableMotor(true, 0, 1);
        }
    }
    
    public void setHingeJoint(HingeJoint joint){
        this.joint = joint;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
