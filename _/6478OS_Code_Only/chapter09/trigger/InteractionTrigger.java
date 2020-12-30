/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger;

import com.jme3.bounding.BoundingVolume;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 *
 * @author reden
 */
public class InteractionTrigger extends Trigger implements ActionListener{
    
    private Vector3f position;
    private BoundingVolume volume;
    private Node guiNode;
    
    private BitmapText interactionPrompt;
    private Spatial player;
    private boolean inside;
    
    public static String INTERACTION_KEY = "Interact";

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if(isEnabled() && volume != null && player != null){
            Boolean contains = volume.contains(player.getWorldTranslation());
            if(!inside && contains){
                if(interactionPrompt != null){
                    guiNode.attachChild(interactionPrompt);
                }
            } else if (inside && !contains){
                if(interactionPrompt != null){
                    guiNode.detachChild(interactionPrompt);
                }
            }
            inside = contains;
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

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        if(volume != null){
            volume.setCenter(position);
        }
    }

    public Node getGuiNode() {
        return guiNode;
    }

    public void setGuiNode(Node guiNode) {
        this.guiNode = guiNode;
    }

    public BitmapText getInteractionPrompt() {
        return interactionPrompt;
    }

    public void setInteractionPrompt(BitmapText interactionPrompt) {
        this.interactionPrompt = interactionPrompt;
    }
    
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equalsIgnoreCase(INTERACTION_KEY) && !isPressed){
            if(isEnabled() && inside){
                trigger();
            }
        }
    }
    
}
