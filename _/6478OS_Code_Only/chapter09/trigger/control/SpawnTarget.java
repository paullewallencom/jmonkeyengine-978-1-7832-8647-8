/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger.control;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import chapter09.trigger.ScriptObject;

/**
 *
 * @author reden
 */
public class SpawnTarget implements ScriptObject{

    private Vector3f position;
    private Quaternion rotation;
    private boolean triggered;
    private Spatial target;
    private Node rootNode;
    
    public void trigger() {
        if(!triggered){
            triggered = true;
            onTrigger();
        }
    }

    public void onTrigger() {
        target.setLocalTranslation(position);
        target.setLocalRotation(rotation);
        rootNode.attachChild(target);
    }

    public void update(float tpf) {
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Spatial getTarget() {
        return target;
    }

    public void setTarget(Spatial target) {
        this.target = target;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
    
    
}
