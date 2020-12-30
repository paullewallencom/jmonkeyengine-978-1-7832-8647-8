/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04.control;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Animation;
import com.jme3.animation.Bone;
import com.jme3.animation.BoneTrack;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.animation.Track;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author Rickard
 */
public class ExpressionsControl extends AbstractControl{

    
    private AnimControl animControl;
    private AnimChannel mainChannel;
    private AnimChannel mouthChannel;
    private AnimChannel eyeBrowChannel;
    
    public enum PhonemeMouth{
        AAAH,
        EEE,
        I,
        OH,
        OOOH,
        FUH,
        MMM,
        LUH,
        ESS,
        RESET;
       
    };
    
    public enum ExpressionEyes{
        NEUTRAL,
        HAPPY,
        ANGRY;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        AnimationManagerControl control = new AnimationManagerControl();
        return control;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        animControl = spatial.getControl(AnimControl.class);
        mainChannel = animControl.createChannel();

        mouthChannel = animControl.createChannel();
        mouthChannel.addBone("LipSide.L");
        mouthChannel.addBone("LipSide.R");
        mouthChannel.addBone("LipTop.L");
        mouthChannel.addBone("LipTop.R");
        mouthChannel.addBone("LipBottom.L");
        mouthChannel.addBone("LipBottom.R");
//        mouthChannel.addBone("Cheek.L");
//        mouthChannel.addBone("Cheek.R");
        mouthChannel.addBone("jaw");
        mouthChannel.setSpeed(0);
        mouthChannel.setLoopMode(LoopMode.Loop);
        mouthChannel.setSpeed(1.0f);
        
        eyeBrowChannel = animControl.createChannel();
        eyeBrowChannel.addBone("eyebrow.01.L");
        eyeBrowChannel.addBone("eyebrow.02.L");
        eyeBrowChannel.addBone("eyebrow.03.L");
        eyeBrowChannel.addBone("eyebrow.01.R");
        eyeBrowChannel.addBone("eyebrow.02.R");
        eyeBrowChannel.addBone("eyebrow.03.R");
        eyeBrowChannel.setLoopMode(LoopMode.Loop);
        eyeBrowChannel.setSpeed(1.0f);
    }
    
    public void setPhoneme(PhonemeMouth p){
        mouthChannel.setAnim("Phoneme_" + p.name(), 0.2f);
        
    }
    
    public void setExpression(ExpressionEyes e){
        eyeBrowChannel.setAnim("Expression_" + e.name(), 0.2f);
    }
   
}
