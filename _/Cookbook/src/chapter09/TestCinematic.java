/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;

/**
 *
 * @author reden
 */
public class TestCinematic extends TestAnimSun{

    public static void main(String[] args){
        TestCinematic test = new TestCinematic();
        test.start();
    }
    
    private MotionPath jaimePath;
    private MotionEvent jaimeMotionEvent;
    private MotionEvent cam1Event, cam2Event, cam3Event;
    
    private Cinematic cinematic;
    private Spatial jaime;
    
    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        
        ((AudioNode)scene.getChild("AudioNode")).play();
        
        cam.setLocation(new Vector3f(0, 15f, 40));
        flyCam.setMoveSpeed(50);
        
        jaime = ((Node) scene.getChild("Models/Jaime/Jaime.j3o")).getChild(0);
        
        cinematic = new Cinematic(scene, 60f);
        
        createPath();
        createMotionEvent();
        
        createCameraEvents();
        
        createCinematic();
        
        cinematic.play();
    }
    
    private void createPath(){
        jaimePath = new MotionPath();

        jaimePath.addWayPoint(new Vector3f(0, getHeight(Vector2f.ZERO), 0));
        
        jaimePath.addWayPoint(scene.getChild("WayPoint1").getWorldTranslation());
        
        jaimePath.addWayPoint(scene.getChild("WayPoint2").getWorldTranslation());
    }
    
    private void createMotionEvent(){
        jaimeMotionEvent = new MotionEvent(jaime, jaimePath, 25f);
        jaimeMotionEvent.setDirectionType(MotionEvent.Direction.Path);
        jaimeMotionEvent.setSpeed(1f);    
    }
    
    private void createCameraEvents(){
        CameraNode camNode = cinematic.bindCamera("cam1", cam);
        
        MotionPath camPath1 = new MotionPath();
        
        camPath1.addWayPoint(scene.getChild("Cam1Pos1").getWorldTranslation().add(0, 1.5f, 0));
        camPath1.addWayPoint(scene.getChild("Cam1Pos2").getWorldTranslation().add(0, 1.5f, 0));
        
        cam1Event = new MotionEvent(camNode, camPath1, 5f);
        cam1Event.setDirectionType(MotionEvent.Direction.LookAt);
        cam1Event.setLookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y);
        
        MotionPath camPath2 = new MotionPath();
        
        camPath2.addWayPoint(scene.getChild("Cam2Pos1").getWorldTranslation().add(0, 1.5f, 0));
        camPath2.addWayPoint(scene.getChild("Cam2Pos2").getWorldTranslation().add(0, 1.5f, 0));
        camPath2.addWayPoint(scene.getChild("Cam2Pos3").getWorldTranslation().add(0, 1.5f, 0));
        
        cam2Event = new MotionEvent(camNode, camPath2, 15f);
        
        
        MotionPath camPath3 = new MotionPath();
        camPath3.addWayPoint(scene.getChild("Cam3Pos1").getWorldTranslation().add(0, 1.5f, 0));
        
        CameraNode camNode2 = cinematic.bindCamera("cam2", cam);
        camNode2.setLocalTranslation(scene.getChild("Cam3Pos1").getWorldTranslation().add(0, 1.5f, 0));
        camNode2.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
    }
    
    
    
    private void createCinematic(){
        final AnimationEvent walkEvent = new AnimationEvent(jaime, "Walk", LoopMode.Loop);
        
        final AnimationEvent idleEvent = new AnimationEvent(jaime, "Idle", 10f, LoopMode.DontLoop);
        AnimationEvent waveEvent = new AnimationEvent(jaime, "Wave", LoopMode.DontLoop);
        
        cinematic.addCinematicEvent(0, jaimeMotionEvent);
        cinematic.addCinematicEvent(0, walkEvent);
        cinematic.addCinematicEvent(0, cam1Event);
        cinematic.activateCamera(0, "cam1");
        cinematic.addCinematicEvent(5f, cam2Event);
        cinematic.activateCamera(20f, "cam2");
        cinematic.addCinematicEvent(25.8f, idleEvent);

        cinematic.addCinematicEvent(30f, waveEvent);
        cinematic.setLoopMode(LoopMode.DontLoop);

        jaimePath.addListener(new MotionPathListener() {

            public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
                if(wayPointIndex == 2){
                    walkEvent.stop();
                }
            }
        });
        stateManager.attach(cinematic);
    }
    
    private float getHeight(Vector2f position){
       return ((Terrain)rootNode.getChild("terrain-TestScene")).getHeight(position);
    }
}
