/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author reden
 */
public class StellarBody extends AbstractControl{
    
    private float size;
    private float speed;
    private float cycle;
    private float orbit;
    
    public StellarBody(float orbit, float speed, float size){
        this.size = size;
        this.speed = speed;
        this.orbit = orbit;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        RigidBodyControl rigidBody = new RigidBodyControl(new SphereCollisionShape(size), 0f);
        rigidBody.setGravity(Vector3f.ZERO);
        spatial.addControl(rigidBody);
    }
    
    
    /**
     * return gravity relative to position
     * @return 
     */
    public Vector3f getGravity(Vector3f position){
        Vector3f relativePosition = spatial.getWorldTranslation().subtract(position);
        return relativePosition.normalize().multLocal(size * 1000 / relativePosition.lengthSquared());
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        if(speed > 0){
            cycle += (speed * tpf)  % FastMath.TWO_PI;
            float x = FastMath.sin(cycle) * orbit;
            float z = FastMath.cos(cycle) * orbit;
            spatial.setLocalTranslation(x, 0, z);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(spatial.getLocalTranslation());
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
