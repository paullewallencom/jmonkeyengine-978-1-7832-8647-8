/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import chapter08.control.ThrusterControl;

/**
 *
 * @author reden
 */
public class TestThruster extends SimpleApplication implements AnalogListener{
    private BulletAppState bulletAppState;

    public static void main(String[] args) {
        TestThruster app = new TestThruster();
        app.start();
    }

    Node spaceShip;
    Material mat;
    
    @Override
    public void simpleInitApp() {
//        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        initPhysics();
        initMaterial();

        spaceShip = new Node("SpaceShip");
        RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(1, 1, 1)), 1);
        spaceShip.addControl(control);
        control.setFriction(0.1f);
        
        Node thruster = new Node("Thruster");
        thruster.setLocalTranslation(0, -1, 0);
        spaceShip.attachChild(thruster);
        
//        Geometry thrusterGeometry = new Geometry("", new Box(0.1f, 0.1f, 0.1f));
//        thruster.rotate(0, FastMath.HALF_PI, 0);
//        thrusterGeometry.setMaterial(mat);
//        thruster.attachChild(thrusterGeometry);
        
        rootNode.attachChild(spaceShip);
        getPhysicsSpace().add(spaceShip);
        
        spaceShip.addControl(new ThrusterControl());

        Node ground = new Node("Ground");
        RigidBodyControl floorControl = new RigidBodyControl(new PlaneCollisionShape(new Plane(new Vector3f(0, 1, 0), 0)), 0);
        ground.addControl(floorControl);
        floorControl.setPhysicsLocation(new Vector3f(0f, -10, 0f));
        rootNode.attachChild(ground);
        getPhysicsSpace().add(ground);

        inputManager.addListener(this, "boost");
        inputManager.addMapping("boost", new KeyTrigger(KeyInput.KEY_SPACE));

        addParticles(thruster);
        
        cam.setLocation(new Vector3f(0, 0, 30));
    }
    
    private void addParticles(Node attachment){
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat2.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        ParticleEmitter pe = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 100);
        pe.setGravity(0, 0, 0);
        pe.setLowLife(1);
        pe.setHighLife(2);
        pe.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -5, 0));
        pe.setImagesX(15);
        pe.setStartSize(1);
        pe.setEndSize(5);
        pe.setStartColor(ColorRGBA.Yellow);
        pe.setEndColor(ColorRGBA.Orange);
        pe.setMaterial(mat2);
        attachment.attachChild(pe);
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

    public void onAnalog(String name, float value, float tpf) {
        if(name.equals("boost") && value > 0){
            spaceShip.getControl(ThrusterControl.class).fireBooster();
        }
    }

    
}
