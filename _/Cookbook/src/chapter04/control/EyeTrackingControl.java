/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04.control;

import com.jme3.animation.Bone;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author Rickard
 */
public class EyeTrackingControl extends AbstractControl {

    private Bone leftEye;
    private Bone rightEye;
    
    private Spatial lookAtObject;
    private Vector3f focusPoint = new Vector3f();
    
    Quaternion offsetQuat = new Quaternion(-1f, 0.0f, 0.00f, 0);
    float flickerTime = 0f;
    float flickerAmount = 0.2f;
    private Vector3f flickerDirection = new Vector3f();
    

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        
        leftEye = ((SkeletonControl)spatial.getControl(SkeletonControl.class)).getSkeleton().getBone("eye.L");
        rightEye = ((SkeletonControl)spatial.getControl(SkeletonControl.class)).getSkeleton().getBone("eye.R");
        
    }
    
    
    
    @Override
    protected void controlUpdate(float tpf) {
        
        if(enabled && lookAtObject != null){
            leftEye.setUserControl(true);
            rightEye.setUserControl(true);
            focusPoint.set(lookAtObject.getWorldTranslation().subtract(getSpatial().getWorldTranslation()));

            Vector3f eyePos = leftEye.getModelSpacePosition();
            
            Vector3f direction = eyePos.subtract(focusPoint).negateLocal();
            direction.y = -direction.y * 2;
            direction.x= direction.x * 2;
            
            flickerTime += tpf * FastMath.nextRandomFloat();
            if(flickerTime > 0.5f){
                flickerTime = 0;
                flickerDirection.set(FastMath.nextRandomFloat() * flickerAmount, FastMath.nextRandomFloat() * flickerAmount, 0);
            }
            direction.addLocal(flickerDirection);
            
            Quaternion q = new Quaternion();
            q.lookAt(direction, Vector3f.UNIT_Y);
            q.addLocal(offsetQuat);
            q.normalizeLocal();
            leftEye.setUserTransformsWorld(leftEye.getModelSpacePosition(), q);
            rightEye.setUserTransformsWorld(rightEye.getModelSpacePosition(), q);
            leftEye.setUserControl(false);
            rightEye.setUserControl(false);
        } else if (lookAtObject == null){
            setEnabled(false);
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        EyeTrackingControl control = new EyeTrackingControl();

        return control;
    }
    
    public void setLookAtObject(Spatial focusObject){
        this.lookAtObject = focusObject;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    
}
