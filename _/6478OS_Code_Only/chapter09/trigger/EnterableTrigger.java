/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 *
 * @author reden
 */
public class EnterableTrigger extends Trigger{
    
    private Vector3f position;
    private BoundingVolume volume;
    
    private List<Spatial> actors;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if(isEnabled() && volume != null && actors != null){
            
            Spatial n;
            for(int i = 0; i < actors.size(); i++ ){
                
                n = actors.get(i);
                if(volume.contains(n.getWorldTranslation())){
                    trigger();
                }
            }
        }
    }

    public BoundingVolume getVolume() {
        return volume;
    }

    public void setVolume(BoundingVolume volume) {
        this.volume = volume;
        if(position != null){
            volume.setCenter(position);
        }
    }

    public List<Spatial> getActors() {
        return actors;
    }

    public void setActors(List<Spatial> actors) {
        this.actors = actors;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        if(volume != null){
            volume.setCenter(position);
        }
    }
    
}
