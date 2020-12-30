/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.firing;

import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import java.util.List;

/**
 *
 * @author reden
 */
public class Bullet {
    
    private Vector3f worldPosition;
    private Vector3f direction;
    private float speed = 10;
    private Ray ray;
    private final static int RANGE = 10;
    private float distance;
    private boolean alive = true;
    
    public Bullet(Vector3f origin, Vector3f direction){
        ray = new Ray(origin, direction);
        this.worldPosition = origin;
        this.direction = direction;
    }
    
    public void update(float tpf){
        ray.setOrigin(worldPosition);
        ray.setLimit(speed * tpf);
        
        distance += ray.limit;
        worldPosition.addLocal(direction.mult(ray.limit));
        
        if(distance >= RANGE){
            System.out.println("out of range");
            alive = false;
        }
        
    }
    
    public CollisionResult checkCollision(List<Collidable> targets){
        CollisionResults collRes = new CollisionResults();
        for(Collidable g: targets){
            g.collideWith(ray, collRes);
        }
        if(collRes.size() > 0){
            alive = false;
            return collRes.getClosestCollision();
        }
        return null;
    }
    
    public boolean isAlive(){
        return alive;
    }
}
