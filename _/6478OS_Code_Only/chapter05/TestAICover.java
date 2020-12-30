/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05;

import chapter05.control.AIControl_SM;
import chapter05.state.SeekCoverState;
import chapter05.waypoint.CoverPoint;
import chapter04.control.AnimationManagerControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import chapter02.control.GameCharacterControl;
import java.util.ArrayList;
import java.util.List;
import jme3test.bullet.PhysicsTestHelper;

public class TestAICover extends SimpleApplication{

    private BulletAppState bulletAppState;
    public static Material lineMat;
    
    private Node coverStructures = new Node("Cover");
    private List<CoverPoint> coverList = new ArrayList<CoverPoint>();
    
    public static void main(String[] args) {
        TestAICover app = new TestAICover();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        lineMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        // activate physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // init a physics test scene
        PhysicsTestHelper.createPhysicsTestWorldSoccer(rootNode, assetManager, bulletAppState.getPhysicsSpace());
        PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace());


        // Load model, attach to character node
        final Node aiCharacter = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        aiCharacter.setLocalScale(1.50f);
        GameCharacterControl physicsCharacter = new GameCharacterControl(0.3f, 2.5f, 8f);
        aiCharacter.addControl(physicsCharacter);
        getPhysicsSpace().add(physicsCharacter);
        rootNode.attachChild(aiCharacter);
        aiCharacter.addControl(new AIControl_SM());
        aiCharacter.addControl(new AnimationManagerControl());
        aiCharacter.setLocalTranslation(12.25f, 0.5f, -5);
        
        CameraNode camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.CameraToSpatial);

        Geometry g = new Geometry("", new Box(1,1,1));
        g.setModelBound(new BoundingSphere(5f, Vector3f.ZERO));
        g.updateModelBound();
        g.setMaterial(lineMat);
        camNode.attachChild(g);
        getFlyByCamera().setMoveSpeed(25);
        rootNode.attachChild(camNode);
        
        final List<Spatial> targets = new ArrayList<Spatial>();
        targets.add(g);
        aiCharacter.getControl(AIControl_SM.class).setTargetList(targets);
        
        /**
         * Cover
         */
        createCoverStructures();
        SeekCoverState.setAvailableCovers(coverList);
        /**
         * 
         */
    }
    
    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }
    
    private void createCoverStructures(){
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Blue);
        Box box = new Box(2, 0.6f, 0.25f);
        Geometry boxGeometry = new Geometry("Box", box);
        boxGeometry.setMaterial(material);
        boxGeometry.setLocalTranslation(8, 0.3f, -2);
        boxGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
        coverStructures.attachChild(boxGeometry);
//        bulletAppState.getPhysicsSpace().add(boxGeometry);
        
        Box box2 = new Box(0.5f, 1f, 0.25f);
        Geometry boxGeometry2 = new Geometry("Box", box2);
        boxGeometry2.setMaterial(material);
        boxGeometry2.setLocalTranslation(10.25f, 0.5f, -2);
        boxGeometry2.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
        coverStructures.attachChild(boxGeometry2);
//        bulletAppState.getPhysicsSpace().add(boxGeometry2);
        
        
        // adding AI covers
        
        Material coverMaterial = material.clone();
        coverMaterial.setColor("Color", ColorRGBA.White);
        Sphere s = new Sphere(5, 5, 0.5f);
        Geometry cover = new Geometry("Cover", s);
        cover.setMaterial(coverMaterial);
        
        Geometry cover1 = cover.clone();
        cover1.setLocalTranslation(8, 0.5f, -2.5f);
        CoverPoint coverPoint1 = new CoverPoint();
        coverPoint1.setCoverDirection(new Vector3f(0, 0, 1f));
        cover1.addControl(coverPoint1);
        rootNode.attachChild(cover1);
        coverList.add(coverPoint1);
        
        Geometry cover2 = cover.clone();
        cover2.setLocalTranslation(10.25f, 0.5f, -1.5f);
        CoverPoint coverPoint2 = new CoverPoint();
        coverPoint2.setCoverDirection(new Vector3f(0, 0, -1f));
        cover2.addControl(coverPoint2);
        rootNode.attachChild(cover2);
        coverList.add(coverPoint2);
        
        rootNode.attachChild(coverStructures);
    }
}
