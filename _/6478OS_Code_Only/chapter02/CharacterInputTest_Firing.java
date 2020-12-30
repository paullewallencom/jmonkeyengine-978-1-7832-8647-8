/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import chapter02.control.GameCharacterControl;
import chapter02.control.GameCharacterControl_Firing;
import chapter02.state.CharacterInputAppState;
import chapter02.state.CharacterInputAppState_Firing;
import java.util.ArrayList;
import java.util.List;
import jme3test.bullet.PhysicsTestHelper;

/**
 *
 * @author Rickard
 */
public class CharacterInputTest_Firing extends SimpleApplication {

    private BulletAppState bulletAppState;
    private Vector3f normalGravity = new Vector3f(0, -9.81f, 0);
    public static Material mat;
    
    private List<Geometry> targets = new ArrayList<Geometry>();

    public static void main(String[] args) {
        CharacterInputTest_Firing app = new CharacterInputTest_Firing();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        PhysicsTestHelper.createPhysicsTestWorldSoccer(rootNode, assetManager, bulletAppState.getPhysicsSpace());

        Node playerNode = new Node("Player");
        GameCharacterControl_Firing charControl = new GameCharacterControl_Firing(0.5f, 2.5f, 8f);
        charControl.setGravity(normalGravity);
        charControl.setCamera(cam);
        playerNode.addControl(charControl);
        bulletAppState.getPhysicsSpace().add(charControl);

        createTargets();
        
        CharacterInputAppState_Firing appState = new CharacterInputAppState_Firing();
        appState.setCharacter(charControl);
        appState.setTargets(targets);
        stateManager.attach(appState);
        rootNode.attachChild(playerNode);
        
        
        
    }
    
    public void createTargets(){
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        for(int i = 0; i < 10; i++){
            Geometry target = new Geometry("", new Sphere(10, 10, 1f));
            target.setLocalTranslation(FastMath.nextRandomFloat() * 10f, 1, FastMath.nextRandomFloat() * 10f);
            target.setMaterial(mat);
            target.setModelBound(new BoundingSphere(1f, Vector3f.ZERO));
            target.updateModelBound();
            rootNode.attachChild(target);
            targets.add(target);
        }
    }
    
    public List<Geometry> getTargets(){
        return targets;
    }
}
