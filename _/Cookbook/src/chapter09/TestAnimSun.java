/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingSphere;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import chapter09.control.SkyControl;
import chapter09.control.SunControl;

/**
 *
 * @author Rickard
 */
public class TestAnimSun extends SimpleApplication{

    public static void main(String[] args) {
        TestAnimSun app = new TestAnimSun();
        app.start();
    }
    
    Material mat;
//    Material skyMat;
    DirectionalLight sunLight;
    private SunControl sunControl;
    private SkyControl skyControl;
    
    LightScatteringFilter filter ;

    Node scene;
    
    @Override
    public void simpleInitApp() {
        scene = (Node) assetManager.loadModel("Scenes/TestScene.j3o");
        rootNode.attachChild(scene);
        scene.detachChildNamed("Sky");
        initMaterial();
//        initSkyMaterial();
        
        createSky();
        
        createSun();
        
        skyControl.setSunControl(sunControl);
        
        // 4. Post Water
        FilterPostProcessor processor = (FilterPostProcessor) assetManager.loadAsset("Effects/Water.j3f");
        viewPort.addProcessor(processor);

//        filter = new LightScatteringFilter();
//        processor.addFilter(filter);
//        sunControl.setLightScatteringFilter(filter);
        
        BloomFilter bloomFilter=new BloomFilter();
        processor.addFilter(bloomFilter);
        
        cam.setLocation(new Vector3f(0, 10f, 30));
        flyCam.setMoveSpeed(50);
        
    }
    
    private void createSky(){
        Geometry sky = new Geometry("Sky", new Box(10f, 10f, 10f));
        sky.setQueueBucket(RenderQueue.Bucket.Sky);
        sky.setCullHint(Spatial.CullHint.Never);
        sky.setShadowMode(RenderQueue.ShadowMode.Off);
        sky.setModelBound(new BoundingSphere(Float.POSITIVE_INFINITY, Vector3f.ZERO));
        Material skyMat = new Material(assetManager, "MatDefs/Misc/Sun.j3md");//mat.clone();
        skyMat.setColor("Color", new ColorRGBA(0.5f, 0.5f, 1f, 1f));
//        skyMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        skyMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
//        skyMat.getAdditionalRenderState().setDepthWrite(false);
        sky.setMaterial(skyMat);
        skyControl = new SkyControl();
        sky.addControl(skyControl);
        skyControl.setCamera(cam);
        scene.attachChild(sky);
    }
    
    private void createSun(){
        Geometry sun = new Geometry("Sun", new Quad(1.5f, 1.5f));
        Material sunMat = mat.clone();
        sun.setMaterial(sunMat);
        sunMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        sunMat.setTexture("ColorMap", assetManager.loadTexture("Textures/sun.png"));
        sunMat.setColor("Color", new ColorRGBA(1f, 1f, 0.9f, 1f));
        sun.setQueueBucket(RenderQueue.Bucket.Sky);
        sun.setCullHint(Spatial.CullHint.Never);
        sun.setShadowMode(RenderQueue.ShadowMode.Off);
        
        sun.setModelBound(new BoundingSphere(Float.POSITIVE_INFINITY, Vector3f.ZERO));

        sunControl = new SunControl();
        sun.addControl(sunControl);
        sunControl.setCamera(cam);
        rootNode.attachChild(sun);
        
        sunLight = (DirectionalLight) scene.getLocalLightList().get(0);
        sunControl.setDirectionalLight(sunLight);
    }
    
    public void initMaterial() {
        mat = new Material(assetManager, "MatDefs/Misc/Unshaded.j3md");
    }
}
