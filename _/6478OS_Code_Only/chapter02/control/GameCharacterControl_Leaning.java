/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.control;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Rickard
 */
public class GameCharacterControl_Leaning extends GameCharacterControl{
    
    
    
    /**
     * Lean
     */
    private Node centerPoint = new Node("Center");
    private float leanValue;
    private float maxLean = FastMath.QUARTER_PI * 0.5f;
    
    private boolean leanLeft, leanRight, leanFree;
    /**
     * End lean
     */

    public GameCharacterControl_Leaning(float radius, float height, float mass) {
        super(radius, height, mass);
        head.setLocalTranslation(0, 1.8f, 0);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if(leanLeft && leanValue < maxLean){
            lean(leanValue+= 0.5f * tpf);
        } else if(!leanFree && leanValue > 0f){
            lean(leanValue-= 0.5f * tpf);
        }
        if(leanRight && leanValue > -maxLean){
            lean(leanValue-= 0.5f * tpf);
        } else if(!leanFree && leanValue < 0f){
            lean(leanValue+= 0.5f * tpf);
        }
        
    }

    
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if(spatial instanceof Node){
            ((Node)spatial).attachChild(centerPoint);
            centerPoint.setLocalTranslation(0, 0.9f, 0);
            centerPoint.attachChild(head);
        }
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        super.onAction(binding, value, tpf);
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
            if (name.equals("RotateLeft")) {
                leanValue += value * tpf;
            } else if (name.equals("RotateRight")) {
                leanValue -= value * tpf;
            }
            lean(leanValue);
        } else {
            if (name.equals("RotateLeft")) {
                rotate(tpf * value);
            } else if (name.equals("RotateRight")) {
                rotate(-tpf * value);
            } else if(name.equals("LookUp")){
                lookUpDown(value * tpf);
            } else if (name.equals("LookDown")){
                lookUpDown(-value * tpf);
            } 
        }
        
        /**
         * 
         */
        if (name.equals("MoveForward") || name.equals("MoveBackward") || name.equals("StrafeLeft") || name.equals("StrafeRight")){
            moveSpeed = value * 3;
        }
        /**
         * 
         */
    }

    private void lean(float value){
        FastMath.clamp(value, -maxLean, maxLean);
        centerPoint.setLocalRotation(new Quaternion().fromAngles(0, 0, -value));
//        centerPoint.rotate(value, 0, 0);
    }
}
