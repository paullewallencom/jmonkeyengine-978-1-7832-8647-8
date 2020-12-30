/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04.control;

import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class EdgeCheckControl extends AbstractControl{
    
    private Ray[] rays = new Ray[9];
    private float okDistance = 0.3f;
    private Spatial world;
    private boolean nearEdge;

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        
        for(int i = 0; i < 9; i++){
            rays[i] = new Ray();
            rays[i].setDirection(Vector3f.UNIT_Y.negate());
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f origo = getSpatial().getWorldTranslation();
        rays[0].setOrigin(origo);
        
        float angle = 0;
        boolean collision = false;
        
        for(int i = 1; i < 9; i++){
            float x = FastMath.cos(angle);
            float z = FastMath.sin(angle);
            rays[i].setOrigin(origo.add(x * 0.5f, 0, z * 0.5f));
            
            collision = checkCollision(rays[i]);
            if(!collision){
                break;
            }
            angle += FastMath.QUARTER_PI;
        }
        
        if(!collision && !nearEdge){
            nearEdge = true;
            spatial.getControl(AdvAnimationManagerControlEdge.class).onAction("NearEdge", true, 0);
        } else if(collision && nearEdge){
            nearEdge = false;
            spatial.getControl(AdvAnimationManagerControlEdge.class).onAction("NearEdge", false, 0);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    private boolean checkCollision(Ray r){
        CollisionResults collResuls = new CollisionResults();
        world.collideWith(r, collResuls);
        if(collResuls.size() > 0 && r.getOrigin().distance(collResuls.getClosestCollision().getContactPoint()) > okDistance){
            return true;
        }
        return false;
    }
    
    public void setWorld(Spatial world){
        this.world = world;
    }
}
