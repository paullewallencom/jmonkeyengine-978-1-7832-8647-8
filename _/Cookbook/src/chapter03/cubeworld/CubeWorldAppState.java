/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.cubeworld;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author reden
 */
public class CubeWorldAppState extends AbstractAppState implements ActionListener{

    private Camera cam;
    
    CubeWorld cubeWorld;
    private CubeCell takenCube;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        setupKeys(app.getInputManager());
        
        cam = app.getCamera();
        
        cubeWorld = new CubeWorld();
        
        Material m = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        m.setColor("Diffuse", ColorRGBA.Gray);
        Material m2 = m.clone();
        m2.setColor("Diffuse", ColorRGBA.Yellow);
        Material m3 = m.clone();
        m3.setColor("Diffuse", ColorRGBA.Green);
        cubeWorld.setMaterials(new Material[]{m, m2, m3});
        
        cubeWorld.generate();
        ((SimpleApplication)app).getRootNode().attachChild(cubeWorld.getWorld());
        
        
        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(-0.3f, -0.5f, -0.2f));
        ((SimpleApplication)app).getRootNode().addLight(light);
    }

    

    
    private void setupKeys(InputManager inputManager) {
        inputManager.addMapping("take", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("put", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(this, "take", "put");
    }
   
    public void onAction(String name, boolean pressed, float tpf) {
            if (name.equals("take") && !pressed) {
                modifyTerrain(true);
            } else if (name.equals("put") && !pressed) {
                modifyTerrain(false);
            }
        }
    
    private void modifyTerrain(boolean pickupCube){

        Ray r = new Ray(cam.getLocation(), cam.getDirection());
        r.setOrigin(cam.getLocation());
        r.setDirection(cam.getDirection());
        CollisionResults cr = new CollisionResults();
        cubeWorld.getWorld().collideWith(r, cr);
        CollisionResult coll = cr.getClosestCollision();
        if(coll != null && coll.getDistance() < 2f && pickupCube){
            Vector3f geomCoords = coll.getGeometry().getWorldTranslation();
            takenCube = cubeWorld.changeTerrain(geomCoords, takenCube);
        } else if ((coll == null  || coll.getDistance() > 2f) && !pickupCube && takenCube != null){
            Vector3f geomCoords = cam.getLocation().add(cam.getDirection().mult(2f));
            geomCoords.set(Math.round(geomCoords.x), Math.round(geomCoords.y), Math.round(geomCoords.z));
            
            takenCube = cubeWorld.changeTerrain(geomCoords, takenCube);
        }
    }
}
