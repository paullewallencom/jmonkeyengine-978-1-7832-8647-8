/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter01;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Rickard
 */
public class TestBirds extends SimpleApplication{

    public static void main(String[] args) {
        TestBirds app = new TestBirds();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        Node scene = (Node) assetManager.loadModel("Scenes/ParticleTest.j3o");
        scene.setLocalTranslation(0, 60, 0);
        rootNode.attachChild(scene);

    }
    
    
}
