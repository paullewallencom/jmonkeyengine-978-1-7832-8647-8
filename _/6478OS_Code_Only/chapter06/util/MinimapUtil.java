/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.util;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import java.util.concurrent.Callable;

/**
 * Code based on TestRenderToTexture.java
 * @author reden
 */
public class MinimapUtil {
    
    
    public static void createMiniMap(final SimpleApplication app, final Spatial scene, int width, int height) throws Exception{
        Camera offScreenCamera = new Camera(width, height);

        offScreenCamera.setParallelProjection(true);
        offScreenCamera.setFrustum(1, 1000, -width, width, height, -height);
        offScreenCamera.setLocation(new Vector3f(0, 100f, 0));
        offScreenCamera.setRotation(new Quaternion().fromAngles(new float[]{FastMath.HALF_PI,FastMath.PI,0}));

        final ViewPort offScreenView = app.getRenderManager().createPreView(scene.getName() + "_View", offScreenCamera);
        offScreenView.setClearFlags(true, true, true);
        offScreenView.setBackgroundColor(ColorRGBA.DarkGray.mult(ColorRGBA.Blue).mult(0.3f));

        //setup framebuffer's texture
        final Texture2D offScreenTexture = new Texture2D(width, height, Image.Format.RGB8);
        offScreenTexture.setMinFilter(Texture.MinFilter.Trilinear);

        //setup framebuffer to use texture
        FrameBuffer offScreenBuffer = new FrameBuffer(width, height, 1);
        offScreenBuffer.setDepthBuffer(Image.Format.Depth);
        offScreenBuffer.setColorTexture(offScreenTexture);

        //set viewport to render to offscreen framebuffer
        offScreenView.setOutputFrameBuffer(offScreenBuffer);
        
//        AmbientLight a = new AmbientLight();
//        a.setColor(ColorRGBA.White.mult(0.3f));
//        DirectionalLight d = new DirectionalLight();
//        d.setDirection(new Vector3f(-0.2f, -0.9f, -0.2f));
//        d.setColor(ColorRGBA.White.mult(0.6f));
//        scene.addLight(d);
//        scene.addLight(a);

        offScreenView.attachScene(scene);
        ((DesktopAssetManager)app.getAssetManager()).addToCache( new TextureKey(scene.getName()+"_mini.png", true), offScreenTexture);
        
        
//        app.enqueue(new Callable(){
//
//            public Object call() throws Exception {
                app.getRenderManager().renderViewPort(offScreenView, 0);
                //System.out.println("created image " + sector.getName()+"_mini.png");
                app.getRenderManager().removePreView(offScreenView);
//                return null;
//            }
//        });
    }
}
