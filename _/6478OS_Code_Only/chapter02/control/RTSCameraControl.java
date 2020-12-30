/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.control;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Rickard
 */
public class RTSCameraControl extends AbstractControl{

    private final Camera cam;
    private Vector3f idealLocation;
    
    public RTSCameraControl(Camera cam){
        this.cam = cam;
    }
    
    public void move(float x, float y, float z){
        
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
}
