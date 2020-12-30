/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.bullet.joints.Point2PointJoint;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reden
 */
public class TestBridge extends SimpleApplication implements ActionListener {

    public static void main(String[] args) {
        TestBridge testBridge = new TestBridge();
        testBridge.start();
    }
    private static String LEFT_CLICK = "Left Click";
    private static String RIGHT_CLICK = "Right Click";
    private static String TOGGLE_PHYSICS = "Toggle";
    private BulletAppState bulletAppState;
    private RigidBodyControl selectedSegment;
    private List<Geometry> segments = new ArrayList<Geometry>();
    private List<Point2PointJoint> joints = new ArrayList<Point2PointJoint>();
    private float maxImpulse = 10;
    Material mat;

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.White);
        initMaterial();

        initPhysics();

        initCamera();

        initWorld();

        initInput();


    }

    @Override
    public void update() {
        super.update();

        for (Geometry g : segments) {
            Vector3f velocity = g.getControl(RigidBodyControl.class).getLinearVelocity();
            velocity.setZ(0);
            g.getControl(RigidBodyControl.class).setLinearVelocity(velocity);
        }
        int jointsSize = joints.size();
        for (int i = 0; i < jointsSize; i++) {
            Point2PointJoint p = joints.get(i);
            if (p.getAppliedImpulse() > maxImpulse) {
                bulletAppState.getPhysicsSpace().remove(p);
                joints.remove(p);
                i--;
                jointsSize--;
            }

        }
    }

    public void initMaterial() {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    }

    private void initPhysics() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
        bulletAppState.setSpeed(0);
//        bulletAppState.setEnabled(false);
    }

    private void initCamera() {
        flyCam.setEnabled(false);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setParallelProjection(true);
        cam.setFrustum(1, 1000, -100 * aspect, 100 * aspect, 100, -100);
        cam.setLocation(new Vector3f(0, 0, 20));
        cam.setRotation(new Quaternion().fromAngles(new float[]{0, -FastMath.PI, 0}));
    }

    private void initWorld() {
        RigidBodyControl leftBox = new RigidBodyControl(new BoxCollisionShape(new Vector3f(75f, 50f, 5f)), 0);
        leftBox.setPhysicsLocation(new Vector3f(-100f, -50f, 0));
        bulletAppState.getPhysicsSpace().add(leftBox);
        RigidBodyControl rightBox = new RigidBodyControl(new BoxCollisionShape(new Vector3f(75f, 50f, 5f)), 0);
        rightBox.setPhysicsLocation(new Vector3f(100f, -50f, 0));
        bulletAppState.getPhysicsSpace().add(rightBox);
    }

    private void initInput() {
        inputManager.addMapping(LEFT_CLICK,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(RIGHT_CLICK,
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping(TOGGLE_PHYSICS,
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, LEFT_CLICK);
        inputManager.addListener(this, RIGHT_CLICK);
        inputManager.addListener(this, TOGGLE_PHYSICS);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(LEFT_CLICK) && !isPressed) {
            RigidBodyControl newSelection = checkSelection();

            if (newSelection != null) {
                selectedSegment = newSelection;
            }
        } else if (name.equals(RIGHT_CLICK) && !isPressed) {
            RigidBodyControl hitSegment = checkSelection();
            if (hitSegment != null && hitSegment != selectedSegment) {
                createJoint(selectedSegment, hitSegment);
            } else {
                createSegment(cam.getWorldCoordinates(inputManager.getCursorPosition(), 10f));
            }
        } else if (name.equals(TOGGLE_PHYSICS) && !isPressed) {
            bulletAppState.setSpeed(1);
        }
    }

    private RigidBodyControl checkSelection() {
        Ray ray = new Ray();
        ray.setOrigin(cam.getWorldCoordinates(inputManager.getCursorPosition(), 0f));
        ray.setDirection(new Vector3f(0, 0, -1f));
        CollisionResults collRes = new CollisionResults();
        for (Geometry geometry : segments) {
            geometry.collideWith(ray, collRes);
            if (collRes.getClosestCollision() != null) {
                return geometry.getControl(RigidBodyControl.class);
            }
        }
        return null;
    }

    private void createSegment(Vector3f location) {
        location.setZ(0);

        RigidBodyControl newSegment = new RigidBodyControl(new SphereCollisionShape(1f), 5);
        bulletAppState.getPhysicsSpace().add(newSegment);

        Geometry g = new Geometry("BridgeNode", new Sphere(10, 10, 1f));
        g.setMaterial(mat);
        g.setModelBound(new BoundingSphere(2f, Vector3f.ZERO));
        g.addControl(newSegment);
        newSegment.setPhysicsLocation(location);
        segments.add(g);
        rootNode.attachChild(g);

        if (selectedSegment != null) {
            createJoint(selectedSegment, newSegment);
        }
        selectedSegment = newSegment;
    }

    private void createJoint(RigidBodyControl body1, RigidBodyControl body2) {
        Vector3f pivotPointB = body1.getPhysicsLocation().subtract(body2.getPhysicsLocation());
        Point2PointJoint joint = new Point2PointJoint(body1, body2, Vector3f.ZERO, pivotPointB);
        joints.add(joint);
        bulletAppState.getPhysicsSpace().add(joint);
    }
}
