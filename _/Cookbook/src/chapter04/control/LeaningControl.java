/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04.control;

import com.jme3.animation.Bone;
import com.jme3.animation.SkeletonControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class LeaningControl extends AbstractControl implements ActionListener, AnalogListener{

    private float leanValue;
    private float maxLean = FastMath.QUARTER_PI;
    
    private boolean leanLeft, leanRight, leanFree;
    
    private Bone leaningBone;
    private Quaternion boneRotation;

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        
        Bone spine = spatial.getControl(SkeletonControl.class).getSkeleton().getBone("spine");
        if(spine != null){
            leaningBone = spine;
            boneRotation = leaningBone.getLocalRotation();
        }
    }
    
    
    
    @Override
    protected void controlUpdate(float tpf) {
        
        if(leanLeft && leanValue < maxLean){
            leanValue+= 0.5f * tpf;
        } else if(!leanFree && leanValue > 0f){
            leanValue-= 0.5f * tpf;
        }
        if(leanRight && leanValue > -maxLean){
            leanValue-= 0.5f * tpf;
        } else if(!leanFree && leanValue < 0f){
            leanValue+= 0.5f * tpf;
        }
        if(leanValue < 0.005f && leanValue > -0.005f){
            leanValue = 0f;
        }
        if(leanValue != 0f){
            System.out.println(leanLeft + " " + leanRight + " " + leanValue);
            lean(leanValue);
        } else {
            leaningBone.setUserControl(false);
            
        }
    }
    
    private void lean(float value){
        FastMath.clamp(value, -maxLean, maxLean);
        
        leaningBone.setUserControl(true);
        Quaternion newQuat = boneRotation.add(new Quaternion().fromAngles(-FastMath.QUARTER_PI * 0.35f, 0, -value)); //-FastMath.QUARTER_PI
        newQuat.normalizeLocal();
        leaningBone.setLocalRotation(newQuat);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("LeanLeft")){
            leanLeft = value;
        } else if (binding.equals("LeanRight")){
            leanRight = value;
        } else if (binding.equals("LeanFree")){
            leanFree = value;
        }
    }

    
    @Override
    public void onAnalog(String name, float value, float tpf) {
        if(leanFree){
            System.out.println("lean value "  + value * tpf);
            if (name.equals("RotateLeft")) {
                leanValue += value * tpf;
            } else if (name.equals("RotateRight")) {
                leanValue -= value * tpf;
            }
            lean(leanValue);
        }
    }
    
}
