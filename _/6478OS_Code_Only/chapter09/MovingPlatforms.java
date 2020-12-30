/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09;

import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author reden
 */
public class MovingPlatforms extends SimpleApplication{

    public static void main(String[] args){
        MovingPlatforms test = new MovingPlatforms();
        test.start();
    }
    
    private MotionPath path;
    private MotionEvent event;
    private Geometry platform;
    
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.White);
        createObject();
        
        createPath();
        
        createEvent();
        
        cam.setLocation(new Vector3f(-50f, 0, 0));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        
        event.play();
    }
    
    private void createObject(){
        platform = new Geometry("Platform", new Box(1f, 0.1f, 1f));
        Material mat = new Material(assetManager, "MatDefs/Misc/Unshaded.j3md");
        platform.setMaterial(mat);
        
        rootNode.attachChild(platform);
    }
    
    private void createEvent(){
        event = new MotionEvent(platform, path);
//        event.setDirectionType(MotionEvent.Direction.PathAndRotation);
//        event.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        event.setInitialDuration(10f);
        event.setSpeed(1f);    
        event.setLoopMode(LoopMode.Loop);
    }
    
    private void createPath(){
        path = new MotionPath();
        for(int i = 0 ; i < 8; i++){
            path.addWayPoint(new Vector3f(0, FastMath.sin(FastMath.QUARTER_PI * i) * 10f, FastMath.cos(FastMath.QUARTER_PI * i) * 10f));
        }
        path.enableDebugShape(assetManager, rootNode);
        path.setCycle(true);
        
        path.addListener(new MotionPathListener() {

            public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                if (path.getNbWayPoints() == wayPointIndex + 1) {
                    
                }
            }
        });
    }
}
