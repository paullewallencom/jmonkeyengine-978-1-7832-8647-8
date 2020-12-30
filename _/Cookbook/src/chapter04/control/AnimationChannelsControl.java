/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04.control;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author Rickard
 */
public class AnimationChannelsControl extends AbstractControl implements AnimEventListener{

    private AnimControl animControl;
    private AnimChannel upperChannel;
    private AnimChannel lowerChannel;
    private AnimChannel mainChannel;
    
    private int repeats = 0;
    
    public enum Animation{
        Idle(LoopMode.Loop, 0.2f),
        Walk(LoopMode.Loop, 0.2f),
        Run(LoopMode.Loop, 0.2f),
        JumpStart(LoopMode.DontLoop, 0.2f),
        Jumping(LoopMode.Loop, 0.0f),
        JumpEnd(LoopMode.DontLoop, 0.1f),
        Punches(LoopMode.DontLoop, 0.1f),
        Taunt(LoopMode.DontLoop, 0.1f),
        Wave(LoopMode.DontLoop, 0.1f),
        SideKick(LoopMode.DontLoop, 0.1f);
        
        Animation(LoopMode loopMode, float blendTime){
            this.loopMode = loopMode;
            this.blendTime = blendTime;
        }
        
        LoopMode loopMode;
        float blendTime;
    }
    
    public enum Channel{
        Upper,
        Lower,
        All,
    }
    
    @Override
    protected void controlUpdate(float tpf) {
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        AnimationChannelsControl control = new AnimationChannelsControl();
        
        return control;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        animControl = spatial.getControl(AnimControl.class);
        upperChannel = animControl.createChannel();
        lowerChannel = animControl.createChannel();
        upperChannel.addFromRootBone("spine");
        lowerChannel.addBone("Root");
        lowerChannel.addFromRootBone("pelvis");

        animControl.addListener(this);
    }
    
    public void setAnimation(Animation animation, Channel channel){
        switch(channel){
            case Upper:
                setAnimation(animation, upperChannel);
                break;
            case Lower:
                setAnimation(animation, lowerChannel);
                break;
            case All:
                setAnimation(animation, upperChannel);
                setAnimation(animation, lowerChannel);
                break;
        }
    }
    
    public void setAnimation(Animation animation, AnimChannel channel) {
//        this.repeats = repeats;
//        if (repeats > 1 || repeats == -1) {
//            mainChannel.setLoopMode(LoopMode.Loop);
//        } else {
//            mainChannel.setLoopMode(LoopMode.DontLoop);
//        }
        channel.setAnim(animation.name(), animation.blendTime);
        channel.setLoopMode(animation.loopMode);
                
    }
    
    public void setAnimation(Animation animation){
        setAnimation(animation, upperChannel);
        setAnimation(animation, lowerChannel);
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(channel.getLoopMode() == LoopMode.DontLoop){
//            if (repeats > 0) {
//                repeats--;
//            }
            
            Animation newAnim = Animation.Idle;
            Animation anim = Animation.valueOf(animName);
            switch(anim){
                case JumpStart:
                    newAnim = Animation.Jumping;
                    break;
            }
//            if (repeats == 0) {
                setAnimation(newAnim, channel);
//            }
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
    
}
