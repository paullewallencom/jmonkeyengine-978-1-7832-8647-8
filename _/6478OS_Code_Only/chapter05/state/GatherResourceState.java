/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.state;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 *
 * @author reden
 */
public abstract class GatherResourceState extends AIStateRTS{

    protected Spatial resource;
    protected int amountCarried;
    
    @Override
    public void stateEnter() {
    }

    @Override
    public void stateExit() {
        amountCarried = 0;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(amountCarried == 0){
            Vector3f direction = resource.getWorldTranslation().subtract(this.spatial.getWorldTranslation());
            if(direction.length() > 1f){
                direction.normalizeLocal();
                aiControl.move(direction, true);
            } else {
                amountCarried = 10;
            }
            
        } else {
            Vector3f direction = Vector3f.ZERO.subtract(this.spatial.getWorldTranslation());
            if(direction.length() > 1f){
                direction.normalizeLocal();
                aiControl.move(direction, true);
            } else {
                finishTask();
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void setResource(Spatial resource){
        this.resource = resource;
    }
    
    protected void finishTask(){
        aiControl.getAiManager().onFinishTask(this.getClass(), amountCarried);
        amountCarried = 0;
    }

    public int getAmountCarried() {
        return amountCarried;
    }

}
