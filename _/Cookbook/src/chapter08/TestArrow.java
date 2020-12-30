/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

/**
 *
 * @author Rickard
 */
public class TestArrow extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private Material matBullet;

    public static void main(String[] args) {
        TestArrow app = new TestArrow();
        app.start();
    }

    @Override
    public void simpleInitApp() {
viewPort.setBackgroundColor(ColorRGBA.White);
        initMaterial();
        initPhysics();

        inputManager.addListener(this, "fire");
        inputManager.addMapping("fire", new KeyTrigger(KeyInput.KEY_SPACE));
cam.setLocation(new Vector3f(0, 0, 20));
    }

    private void initPhysics() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
    }

    public void initMaterial() {
        matBullet = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matBullet.setColor("Color", ColorRGBA.DarkGray);
    }

    public void fireArrow() {
        Arrow arrow = new Arrow(new Vector3f(0f, 6f, -10f), new Vector3f(1f, 0.0f, 0.0f).mult(5));
        arrow.setMaterial(matBullet);
        rootNode.attachChild(arrow);
        getPhysicsSpace().add(arrow);
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("fire") && !isPressed) {
            fireArrow();
        }
    }
}
