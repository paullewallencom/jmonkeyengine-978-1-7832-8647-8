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
public class AnimationManagerControl extends AbstractControl implements AnimEventListener{

    private AnimControl animControl;
    private AnimChannel mainChannel;
    
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
        animControl.addListener(this);
    }
    
    public void setAnimation(Animation animation) {
        if(mainChannel.getAnimationName() == null || !mainChannel.getAnimationName().equals(animation.name())){
            mainChannel.setAnim(animation.name(), animation.blendTime);
            mainChannel.setLoopMode(animation.loopMode);
        }
        
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(channel.getLoopMode() == LoopMode.DontLoop){
            Animation newAnim = Animation.Idle;
            Animation anim = Animation.valueOf(animName);
            switch(anim){
                case JumpStart:
                    newAnim = Animation.Jumping;
                    break;
            }
            setAnimation(newAnim);
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
    
}
