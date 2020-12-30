/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.control;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class SkyControl extends AbstractControl{

    private Camera cam;
    private Vector3f position = new Vector3f(0, 5f, 0);
    private SunControl sun;
    private ColorRGBA color = new ColorRGBA();
    
    private static final ColorRGBA dayColor = new ColorRGBA(0.5f, 0.5f, 1f, 1f);
    private static final ColorRGBA eveningColor = new ColorRGBA(1f, 0.7f, 0.5f, 1f);
    private static final ColorRGBA nightColor = new ColorRGBA(0.1f, 0.1f, 0.2f, 1f);
    
    
    @Override
    protected void controlUpdate(float tpf) {
        spatial.setLocalTranslation((cam.getLocation().add(position)));
        if(sun != null){
            float sunHeight = sun.getHeight();
            if(sunHeight > 0){
                color.interpolate(eveningColor, dayColor, FastMath.pow(sunHeight, 4));
            } else {
                color.interpolate(eveningColor, nightColor, FastMath.pow(sunHeight, 4));
            }
            ((Geometry)spatial).getMaterial().setColor("Color", color);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void setCamera(Camera cam){
        this.cam = cam;
    }

    public void setSunControl(SunControl sun){
        this.sun = sun;
    }
}
