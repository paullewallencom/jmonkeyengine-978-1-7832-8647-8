/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.client.object;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class PhysicsObjectControl extends AbstractControl {
    
    private boolean serverControllled;
    private int id;

    public PhysicsObjectControl(int id){
        super();
        this.id = id;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public boolean isServerControllled() {
        return serverControllled;
    }

    public void setServerControllled(boolean serverControllled) {
        this.serverControllled = serverControllled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
