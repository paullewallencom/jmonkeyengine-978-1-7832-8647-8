/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import chapter08.control.ArrowFacingControl;

/**
 *
 * @author reden
 */
public class Arrow extends Node{

    public Arrow(Vector3f location, Vector3f velocity) {
        Box arrowBody = new Box(0.3f, 4f, 0.3f);
        Geometry geometry = new Geometry("bullet", arrowBody);
        geometry.setLocalTranslation(0f, -4f, 0f);
        SphereCollisionShape arrowHeadCollision = new SphereCollisionShape(0.5f);
        RigidBodyControl rigidBody = new RigidBodyControl(arrowHeadCollision, 1f);
        rigidBody.setCcdMotionThreshold(0.01f);
        rigidBody.setLinearVelocity(velocity);
        setLocalTranslation(location);
        addControl(rigidBody);
        attachChild(geometry);
        addControl(new ArrowFacingControl());
    }
}
