/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import chapter08.control.ThrusterControl;

/**
 *
 * @author reden
 */
public class SpaceShip {

    Node spatial;
    
    public SpaceShip() {
        spatial = new Node("SpaceShip");
        RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(1, 1, 1)), 1);
        spatial.addControl(control);
        control.setGravity(Vector3f.ZERO);
        
        Node thruster = new Node("Thruster");
        thruster.setLocalTranslation(-1, 0, 0);
        spatial.attachChild(thruster);
        spatial.rotate(0, FastMath.PI, 0);
        
        spatial.addControl(new ThrusterControl());
    }
    
    public Spatial getSpatial(){
        return spatial;
    }
    
    public void setGravity(Vector3f relativeGravity){
        spatial.getControl(RigidBodyControl.class).setGravity(relativeGravity);
    }
    
}
