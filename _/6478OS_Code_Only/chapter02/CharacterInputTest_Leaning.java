/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import chapter02.control.GameCharacterControl;
import chapter02.control.GameCharacterControl_Leaning;
import chapter02.state.CharacterInputAppState;
import jme3test.bullet.PhysicsTestHelper;

/**
 *
 * @author Rickard
 */
public class CharacterInputTest_Leaning extends SimpleApplication {

    private BulletAppState bulletAppState;
    private Vector3f normalGravity = new Vector3f(0, -9.81f, 0);
    public static Material lineMat;

    public static void main(String[] args) {
        CharacterInputTest_Leaning app = new CharacterInputTest_Leaning();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        PhysicsTestHelper.createPhysicsTestWorldSoccer(rootNode, assetManager, bulletAppState.getPhysicsSpace());

        Node playerNode = new Node("Player");
        GameCharacterControl_Leaning charControl = new GameCharacterControl_Leaning(0.5f, 2.5f, 8f);
        charControl.setGravity(normalGravity);
        charControl.setCamera(cam);
        playerNode.addControl(charControl);
        bulletAppState.getPhysicsSpace().add(charControl);

        CharacterInputAppState appState = new CharacterInputAppState();
        appState.setCharacter(charControl);
        stateManager.attach(appState);
        rootNode.attachChild(playerNode);
    }
}
