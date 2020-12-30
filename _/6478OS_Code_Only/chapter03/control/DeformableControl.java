/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.control;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.terrain.Terrain;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reden
 */
public class DeformableControl extends AbstractControl{

    private Terrain terrain;
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        
        terrain = (Terrain) spatial;
    }
    
    public void deform(Vector3f location, int radius, float force){
        Vector2f pos2D = new Vector2f((int)location.x, (int)location.z);
        deform(pos2D, radius, force);
    }
    
    public void deform(Vector2f location, int radius, float force){
        List<Vector2f> heightPoints = new ArrayList<Vector2f>();
        List<Float> heightValues = new ArrayList<Float>();
        
        for(int x = -radius; x < radius; x++){
            for(int y = -radius; y < radius; y++){
                Vector2f terrainPoint = new Vector2f(location.x + x, location.y + y);
                float distance = location.distance(terrainPoint);
                if(distance < radius){
                    float impact = force * FastMath.sqrt(1 - distance / radius) ;
                    float height = terrain.getHeight(terrainPoint);
                    heightPoints.add(terrainPoint);
                    heightValues.add(Math.max(-impact, -height));
                }
            }
        }
        terrain.setLocked(false);
        terrain.adjustHeight(heightPoints, heightValues);
        terrain.setLocked(true);
    }
}
