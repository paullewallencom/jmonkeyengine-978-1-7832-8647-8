/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.Point2PointJoint;
import com.jme3.bullet.joints.SixDofJoint;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import chapter08.control.BalanceControl2;

/**
 *
 * @author reden
 */
public class TestBalance extends SimpleApplication{

    public static void main(String[] args){
        TestBalance testBalance = new TestBalance();
        testBalance.start();
    }
    
    private BulletAppState bulletAppState;
    Material mat;
    
    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(25);
        initPhysics();
        initMaterial();
        
        Geometry waist = createSegment(new Vector3f(0, 0f, 0), true, new Vector3f(0.25f, 0.25f, 0.25f));
        
        Geometry torso = createSegment(new Vector3f(0, 4.25f, 0), false, new Vector3f(0.25f, 2f, 0.25f));
        
        SixDofJoint waistJoint =  new SixDofJoint(waist.getControl(RigidBodyControl.class), torso.getControl(RigidBodyControl.class), new Vector3f(0, 0.25f, 0), new Vector3f(0, -2.25f, 0f), true);
      
        bulletAppState.getPhysicsSpace().add(waistJoint);
        waistJoint.setAngularLowerLimit(new Vector3f(-FastMath.QUARTER_PI * 0.3f, 0, 0));
        waistJoint.setAngularUpperLimit(new Vector3f(FastMath.QUARTER_PI * 0.3f, 0, 0));
        
        Geometry left = createSegment(new Vector3f(0, 4.25f, 0), false, new Vector3f(0.25f, 0.25f, 2f));
        
        SixDofJoint leftJoint = createJoint(torso.getControl(RigidBodyControl.class), left.getControl(RigidBodyControl.class), true);
        leftJoint.setAngularLowerLimit(new Vector3f(0, 0, 0));
        leftJoint.setAngularUpperLimit(new Vector3f(FastMath.QUARTER_PI, 0, 0));
        BalanceControl2 bc = new BalanceControl2();
        bc.setJoint(leftJoint);
        left.addControl(bc);
        
        Geometry right = createSegment(new Vector3f(0, 4.25f, 0), false, new Vector3f(0.25f, 0.25f, 2f));
        
        SixDofJoint rightJoint = createJoint(torso.getControl(RigidBodyControl.class), right.getControl(RigidBodyControl.class), false);
        rightJoint.setAngularLowerLimit(new Vector3f(-FastMath.QUARTER_PI, 0, 0));
        rightJoint.setAngularUpperLimit(new Vector3f(0, 0, 0));
        
       BalanceControl2 bc2 = new BalanceControl2();
        bc2.setJoint(rightJoint);
        right.addControl(bc2);
//        
    }
    
    private void initPhysics() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
    }

    public void initMaterial() {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    }
    
    private Geometry createSegment(Vector3f location, boolean rigid, Vector3f size) {

        RigidBodyControl newSegment = new RigidBodyControl(new BoxCollisionShape(size), rigid ? 0 : 1f);
        bulletAppState.getPhysicsSpace().add(newSegment);
        newSegment.setSleepingThresholds(0.001f, 0.001f);

        Geometry g = new Geometry("BridgeNode", new Box(size.x, size.y, size.z));
        g.setMaterial(mat);
        g.addControl(newSegment);
        newSegment.setPhysicsLocation(location);
        rootNode.attachChild(g);
        return g;
    }

    private SixDofJoint createJoint(RigidBodyControl body1, RigidBodyControl body2, boolean left) {
        Vector3f pivotPointB = body1.getPhysicsLocation().subtract(body2.getPhysicsLocation());
        SixDofJoint joint = new SixDofJoint(body1, body2, new Vector3f(0, 2.5f, left ? 0.25f : -.25f), new Vector3f(0, 0, left ? -2.5f : 2.5f), true);
      
        bulletAppState.getPhysicsSpace().add(joint);
        
        return joint;
    }
}
