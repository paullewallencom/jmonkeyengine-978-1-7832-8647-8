/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04.control;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rickard
 */
public class AdvAnimationManagerControlEdge extends AbstractControl implements AnimEventListener, ActionListener, AnalogListener{

    private AnimControl animControl;
    private AnimChannel upperChannel;
    private AnimChannel lowerChannel;
    
    boolean forward, backward, leftRotate, rightRotate, leftStrafe, rightStrafe, jumpStarted, inAir, firing, nearEdge;
    private Properties animationNames;

    public enum Animation{
        Idle("idle", LoopMode.Loop, 0.2f),
        Walk("walk", LoopMode.Loop, 0.2f),
        Run("run", LoopMode.Loop, 0.2f),
        JumpStart("jump.start", LoopMode.DontLoop, 0.2f),
        Jumping("jump.mid", LoopMode.Loop, 0.0f),
        JumpEnd("jump.end", LoopMode.DontLoop, 0.1f),
        Punches("attack1", LoopMode.DontLoop, 0.1f),
        Taunt("taunt", LoopMode.DontLoop, 0.1f),
        Wave("greet", LoopMode.DontLoop, 0.1f),
        SideKick("attack2", LoopMode.DontLoop, 0.1f);
        
        Animation(String key, LoopMode loopMode, float blendTime){
            this.key = key;
            this.loopMode = loopMode;
            this.blendTime = blendTime;
        }
        
        String key;
        LoopMode loopMode;
        float blendTime;
    }
    
    public enum Channel{
        Upper,
        Lower,
        All,
    }
    
    public AdvAnimationManagerControlEdge(){
        
    }
    
    public AdvAnimationManagerControlEdge(String animationNameFile){
        animationNames = new Properties();
        try {
            animationNames.load(getClass().getClassLoader().getResourceAsStream(animationNameFile));
        } catch (IOException ex) {
            Logger.getLogger(AdvAnimationManagerControlEdge.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public String getAnimationName(String key){
        String animName = animationNames.getProperty(key);
        if(animName != null){
            return animName;
        }
        return key;
    }
    @Override
    protected void controlUpdate(float tpf) {
        if(inAir){
            BetterCharacterControl charControl =spatial.getControl(BetterCharacterControl.class);
            if(charControl != null && charControl.isOnGround()){
//                setAnimation(Animation.JumpEnd);
                setAnimation(Animation.Idle);
                jumpStarted = false;
                inAir = false;
            }
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        AdvAnimationManagerControlEdge control = new AdvAnimationManagerControlEdge();
        control.animationNames = animationNames;
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
        System.out.println(" " + animation);
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
    
    private void setAnimation(Animation animation, AnimChannel channel) {
        if(channel.getAnimationName() == null || !channel.getAnimationName().equals(animation.name())){
            channel.setAnim(getAnimationName(animation.key), animation.blendTime);
        }
        
        channel.setLoopMode(animation.loopMode);
                
    }
    
    public void setAnimation(Animation animation){
        setAnimation(animation, upperChannel);
        setAnimation(animation, lowerChannel);
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(channel.getLoopMode() == LoopMode.DontLoop){

            Animation newAnim = Animation.Idle;
            Animation anim = Animation.valueOf(animName);
            switch(anim){
                case JumpStart:
                    newAnim = Animation.Jumping;
                    inAir = true;
                    break;
                case Punches:
                    firing = false;
                    break;
            }
            setAnimation(newAnim, channel);
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
    
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("StrafeLeft")) {
            leftStrafe = value;
        } else if (binding.equals("StrafeRight")) {
            rightStrafe = value;
        } else if (binding.equals("MoveForward")) {
            forward = value;
        } else if (binding.equals("MoveBackward")) {
            backward = value;
        }else if (binding.equals("Fire") && value) {
            firing = true;
            setAnimation(Animation.Punches, Channel.Upper);
        } else if (binding.equals("Jump") && value) {
            jumpStarted = true;
            setAnimation(Animation.JumpStart);
        } 
        
        /**
         * Edge detection
         */
        else if (binding.equals("NearEdge")) {
            nearEdge = value;
            if(nearEdge){
                setAnimation(Animation.Jumping, Channel.Upper);
            }
            
        }
        
        /**
         * /Edge detection
         */
        if(jumpStarted || firing){
            
        } else if(forward || backward || rightStrafe || leftStrafe){
            if(nearEdge){
                setAnimation(Animation.Walk, Channel.Lower);
            } else {
                setAnimation(Animation.Walk);
            }
            
        } else {
            if(nearEdge){
                setAnimation(Animation.Idle, Channel.Lower);
            } else {
                setAnimation(Animation.Idle);
            }
            
        }
        
    }

    public void onAnalog(String name, float value, float tpf) {
        
    }

}
