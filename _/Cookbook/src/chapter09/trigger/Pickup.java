/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 *
 * @author reden
 */
public class Pickup extends Trigger{
    
    private Vector3f position;
    private List<Spatial> actors;
    private Spatial triggeringActor;
    private float triggerDistance = 0.5f;
    
    private boolean pickedUp;
    private Spatial model;
    private Pickupable pickupObject;
    

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        if(isEnabled() && !pickedUp){
            model.rotate(0, 0.05f, 0);
            if(actors != null){
            
                Spatial actor;
                for(int i = 0; i < actors.size(); i++ ){

                    actor = actors.get(i);
                    if((actor.getWorldTranslation().distance(position) < triggerDistance)){
                        triggeringActor = actor;
                        trigger();
                    }
                }
            }
            
        }
    }

    @Override
    public void onTrigger() {
        super.onTrigger();
        pickedUp = true;
        model.getParent().detachChild(model);
        pickupObject.apply(triggeringActor);
    }

    
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public List<Spatial> getActors() {
        return actors;
    }

    public void setActors(List<Spatial> actors) {
        this.actors = actors;
    }

    public Pickupable getPickupObject() {
        return pickupObject;
    }

    public void setPickupObject(Pickupable pickupObject) {
        this.pickupObject = pickupObject;
    }

    public Spatial getModel() {
        return model;
    }

    public void setModel(Spatial model) {
        this.model = model;
    }
    
    
}
