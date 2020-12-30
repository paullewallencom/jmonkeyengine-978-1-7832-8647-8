/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter01;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Rickard
 */
public class LoadScene extends SimpleApplication{

    public static void main(String[] args) {
        LoadScene app = new LoadScene();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        Node scene = (Node) assetManager.loadModel("Scenes/TestScene.j3o");
        rootNode.attachChild(scene);
        
        Node birds = (Node) assetManager.loadModel("Scenes/ParticleTest.j3o");
        birds.setLocalTranslation(0, 40, 0);
        rootNode.attachChild(birds);
        
        // 4. Post Water
        FilterPostProcessor processor = (FilterPostProcessor) assetManager.loadAsset("Effects/Water.j3f");
        viewPort.addProcessor(processor);
        
        // 5. Audio
        ((AudioNode)scene.getChild("AudioNode")).play();
        
        cam.setLocation(new Vector3f(0, 100f, 0));
        flyCam.setMoveSpeed(50);
        
    }
    
    
}
