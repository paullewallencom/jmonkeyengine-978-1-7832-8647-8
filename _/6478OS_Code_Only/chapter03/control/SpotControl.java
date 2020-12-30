/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.control;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Rickard
 */
public class SpotControl extends AbstractControl{

    private Camera camera;
    
    @Override
    protected void controlUpdate(float tpf) {
        
        spatial.setLocalTranslation(camera.getLocation());
        spatial.setLocalRotation(camera.getRotation());
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void setCamera(Camera cam){
        this.camera = cam;
    }
}
