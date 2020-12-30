/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.client.object;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class ClientBullet extends AbstractControl{

    private Vector3f position;
    private boolean isAlive;
    
    @Override
    protected void controlUpdate(float tpf) {
        float factor = tpf / 0.03f;
        spatial.setLocalTranslation(spatial.getLocalTranslation().interpolateLocal(position, factor));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void setPosition(Vector3f position){
        this.position = position;
        
    }

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
}
