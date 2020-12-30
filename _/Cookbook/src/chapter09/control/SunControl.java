/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.control;

import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class SunControl extends AbstractControl {

    private float time;
    private Camera cam;
    private Vector3f position = new Vector3f();
    private DirectionalLight directionalLight;
    private float height;
    private float timeFactor = 0.1f;
    private LightScatteringFilter lightScatteringFilter;
    
    private ColorRGBA sunColor = new ColorRGBA();
    
    private static final ColorRGBA dayColor = new ColorRGBA(1f, 1f, 0.9f, 1f);
    private static final ColorRGBA eveningColor = new ColorRGBA(1f, 0.7f, 0.4f, 1f);
    
    @Override
    protected void controlUpdate(float tpf) {
        float x = FastMath.cos(time) * 10f;
        float z = FastMath.sin(time) * 10f;
        float y = FastMath.sin(time ) * 5f;
        
        position.set(x, y, z);
        spatial.setLocalTranslation((cam.getLocation().add(position)));
        spatial.lookAt(cam.getLocation(), Vector3f.UNIT_Y);
        if(directionalLight != null){
            updateLight(y);
            if(lightScatteringFilter != null){
                if(y > -2f){
                    if(!lightScatteringFilter.isEnabled()){
                        lightScatteringFilter.setEnabled(true);
                    }
                    lightScatteringFilter.setLightDensity(1.4f);
                } else if(lightScatteringFilter.isEnabled()){
                    lightScatteringFilter.setEnabled(false);
                }

                lightScatteringFilter.setLightPosition(position.mult(1000));
            }
        }
        time += tpf * timeFactor;
        time = time % FastMath.TWO_PI;
    }
    
    private void updateLight(float y){
        directionalLight.setDirection(position.negate());
        height = (y) / 5f ;
        sunColor.interpolate(eveningColor, dayColor, FastMath.sqr(height));
        
        
        directionalLight.setColor(sunColor);
        ((Geometry)spatial).getMaterial().setColor("Color", sunColor);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void setCamera(Camera cam){
        this.cam = cam;
    }
    
    public Vector3f getPosition(){
        return position;
    }
    
    public float getHeight(){
        return height;
    }
    
    public void setDirectionalLight(DirectionalLight light){
        this.directionalLight = light;
        sunColor = directionalLight.getColor();
    }
    
    public void setLightScatteringFilter(LightScatteringFilter filter){
        this.lightScatteringFilter = filter;
    }
}
