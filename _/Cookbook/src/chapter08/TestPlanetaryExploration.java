/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import chapter08.control.ThrusterControl;

/**
 *
 * @author reden
 */
public class TestPlanetaryExploration extends SimpleApplication implements AnalogListener{

    public static void main(String[] args){
        TestPlanetaryExploration test = new TestPlanetaryExploration();
        test.start();
    }
    
    private BulletAppState bulletAppState;
    Material mat;
    SpaceShip ship;
    
    private List<StellarBody> gravitationalBodies = new ArrayList<StellarBody>();
    
    @Override
    public void simpleInitApp() {
        initPhysics();
        
        initMaterial();
        
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(0, 300f, 0));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        
        Geometry sun = new Geometry("planet 1 ", new Sphere(10, 10, 30f)); 
        sun.setMaterial(mat.clone());
        sun.getMaterial().setColor("Color", ColorRGBA.Yellow);
        StellarBody pcSun = new StellarBody(0f, 0f, 30f);
        sun.addControl(pcSun);
        gravitationalBodies.add(pcSun);
        
        rootNode.attachChild(sun);
        bulletAppState.getPhysicsSpace().add(sun);
        
        Geometry planetGeom = new Geometry("planet 1 ", new Sphere(10, 10, 20f)); 
        planetGeom.setMaterial(mat.clone());
        StellarBody pc = new StellarBody(130f, 0.1f, 20f);
        planetGeom.addControl(pc);
        gravitationalBodies.add(pc);
        
        rootNode.attachChild(planetGeom);
        bulletAppState.getPhysicsSpace().add(planetGeom);
        
        Geometry planetGeom2 = new Geometry("planet 1 ", new Sphere(10, 10, 12f)); 
        planetGeom2.setMaterial(mat.clone());
        StellarBody pc2 = new StellarBody(80f, 0.3f, 12f);
        planetGeom2.addControl(pc2);
        gravitationalBodies.add(pc2);
        
        rootNode.attachChild(planetGeom2);
        bulletAppState.getPhysicsSpace().add(planetGeom2);
        
        ship = new SpaceShip();
        
        ship.getSpatial().setLocalTranslation(100f, 0, 0);
        ship.getSpatial().getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(100, 0, 0));
        ship.setGravity(Vector3f.ZERO);
        rootNode.attachChild(ship.getSpatial());
        bulletAppState.getPhysicsSpace().add(ship.getSpatial());
        bulletAppState.getPhysicsSpace().setGravity(Vector3f.ZERO);
        
        initControls();
    }
    
    
    private void initPhysics() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
    }

    public void initMaterial() {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    }
    
    private void initControls(){
        String[] mappings = new String[]{"rotateLeft", "rotateRight", "boost"};
        inputManager.addListener(this, mappings);
        inputManager.addMapping("boost", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("rotateLeft", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("rotateRight", new KeyTrigger(KeyInput.KEY_RIGHT));
    }

    @Override
    public void update() {
        super.update();
        Vector3f combinedGravity = new Vector3f();
        
        for(StellarBody body: gravitationalBodies){
            combinedGravity.addLocal(body.getGravity(ship.getSpatial().getWorldTranslation()));
        }
        ship.setGravity(combinedGravity);
    }
    
    public void onAnalog(String name, float value, float tpf) {
        if(name.equals("boost") && value > 0){
            ship.getSpatial().getControl(ThrusterControl.class).fireBooster();
        } else if(name.equals("rotateLeft") && value > 0){
            ship.getSpatial().rotate(0, 0.02f, 0);
            ship.getSpatial().getControl(RigidBodyControl.class).setPhysicsRotation(ship.getSpatial().getLocalRotation());
        } else if(name.equals("rotateRight") && value > 0){
            ship.getSpatial().rotate(0, -0.02f, 0);
            ship.getSpatial().getControl(RigidBodyControl.class).setPhysicsRotation(ship.getSpatial().getLocalRotation());
        }
    }
}
