/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import chapter02.CharacterInputTest;
import jme3test.bullet.PhysicsTestHelper;
import chapter08.control.DoorCloseControl;

/**
 *
 * @author reden
 */
public class TestDoor extends CharacterInputTest {

    public static void main(String[] args){
        TestDoor testDoor = new TestDoor();
        testDoor.start();
    }
    
//    private BulletAppState bulletAppState;
    private HingeJoint joint;
    
    Material mat;

    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        
        initMaterial();
        
        bulletAppState.setDebugEnabled(true);
        
        RigidBodyControl attachment = new RigidBodyControl(new BoxCollisionShape(new Vector3f(.1f, .1f, .1f)), 0);
        attachment.setPhysicsLocation(new Vector3f(-5f, 1.52f, 0f));
        bulletAppState.getPhysicsSpace().add(attachment);
        
        Geometry doorGeometry = new Geometry("Door", new Box(0.6f, 1.5f, 0.1f));
        
        doorGeometry.setMaterial(mat);
        RigidBodyControl doorPhysicsBody = new RigidBodyControl(new BoxCollisionShape(new Vector3f(.6f, 1.5f, .1f)), 1);
        bulletAppState.getPhysicsSpace().add(doorPhysicsBody);
        doorGeometry.addControl(doorPhysicsBody);
        
        rootNode.attachChild(doorGeometry);
        
        joint = new HingeJoint(attachment, doorPhysicsBody, new Vector3f(0f, 0f, 0f), new Vector3f(-1f, 0f, 0f), Vector3f.UNIT_Y, Vector3f.UNIT_Y);
        joint.setLimit(-FastMath.HALF_PI - 0.1f, FastMath.HALF_PI + 0.1f);
        bulletAppState.getPhysicsSpace().add(joint);

        DoorCloseControl doorControl = new DoorCloseControl();
        doorControl.setHingeJoint(joint);
        doorGeometry.addControl(doorControl);
    }
    
    public void initMaterial() {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    }

    @Override
    public void update() {
        super.update();
    }

    
}
