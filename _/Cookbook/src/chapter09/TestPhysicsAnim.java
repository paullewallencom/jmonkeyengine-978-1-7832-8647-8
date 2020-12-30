/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.List;
import chapter08.control.ThrusterControl;

/**
 *
 * @author reden
 */
public class TestPhysicsAnim extends SimpleApplication implements ActionListener{

    
    
    public static void main(String[] args){
        TestPhysicsAnim test = new TestPhysicsAnim();
        test.start();
    }
    
    private BulletAppState bulletAppState;
//    List<Node> debris = new ArrayList<Node>();
    
    Node testBox;
    
    Material mat;
    
    private float time;
    private float lastUpdate;
    
    private MotionEvent event;
    private MotionPath path;
    private Cinematic cinematic;
    private float simulationLength = 7f;
    
    @Override
    public void simpleInitApp() {
        initMaterial();
        initPhysics();
        
        for(int i = 0; i < 4; i++){
            Node box = new Node("Box");
            box.attachChild(new Geometry("Box", new Box(1f, 1f, 1f)));
            RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(1, 1, 1)), 1);
            control.setPhysicsLocation(new Vector3f(i, 0, 0));
            box.addControl(control);
            box.setMaterial(mat);
            control.setFriction(0.1f);
            rootNode.attachChild(box);
            getPhysicsSpace().add(box);
            if(i == 1){
                testBox = box;
            }
        }
        
        Node ground = new Node("Ground");
        RigidBodyControl floorControl = new RigidBodyControl(new PlaneCollisionShape(new Plane(new Vector3f(0, 1, 0), 0)), 0);
        ground.addControl(floorControl);
        floorControl.setPhysicsLocation(new Vector3f(0f, -1f, 0f));
        rootNode.attachChild(ground);
        getPhysicsSpace().add(ground);
        
        path = new MotionPath();
        
        
        inputManager.addListener(this, "start");
        inputManager.addMapping("start", new KeyTrigger(KeyInput.KEY_SPACE));
        
        cam.setLocation(new Vector3f(-80, 10, 80));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        
    }
    
    
    private void initPhysics() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
    }

    public void initMaterial() {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        
        
        
        if(time > simulationLength){
            createEvent();
            getPhysicsSpace().remove(testBox);
            testBox.setLocalTranslation(Vector3f.ZERO);
        } else if(time == 0 || time - lastUpdate > 0.3f){
            path.addWayPoint(testBox.getWorldTranslation());
            
        }
        time += tpf;
    }
    
    private void createEvent(){
        event = new MotionEvent(testBox, path);
        event.setDirectionType(MotionEvent.Direction.Rotation);
        event.setRotation(testBox.getLocalRotation());
        event.setInitialDuration(simulationLength);
        
        event.setSpeed(1);
    }
    
    // create motion event and path
    
    // add updates to path
    
    // set initial duration when simluation ends

    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("start") && isPressed){
            event.play();
            System.out.println("start");
        }
    }
}
