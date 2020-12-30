/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08.control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.SixDofJoint;
import com.jme3.bullet.joints.motors.RotationalLimitMotor;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class BalanceControl2 extends AbstractControl{
    
    private SixDofJoint joint;
    private RotationalLimitMotor motorX;
    
    @Override
    protected void controlUpdate(float tpf) {
        PhysicsRigidBody bodyA;
        bodyA = joint.getBodyA();
        
        float[] anglesA = new float[3];
        bodyA.getPhysicsRotation().toAngles(anglesA);
        float x = anglesA[0];
        if(x > 0.01){
            motorX.setEnableMotor(true);
            motorX.setTargetVelocity(x*1.1f);
            motorX.setMaxMotorForce(13.5f);
        } 
        else if (x < -0.01f){
            motorX.setEnableMotor(true);
            motorX.setTargetVelocity(x*1.1f );
            motorX.setMaxMotorForce(13.5f);
        } 
        else {
            motorX.setTargetVelocity(0);
            motorX.setMaxMotorForce(0);
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void setJoint(SixDofJoint joint){
        this.joint = joint;
        motorX = joint.getRotationalLimitMotor(0);
        motorX.setBounce(0.0f);
        motorX.setDamping(0.07f);
    }
}
