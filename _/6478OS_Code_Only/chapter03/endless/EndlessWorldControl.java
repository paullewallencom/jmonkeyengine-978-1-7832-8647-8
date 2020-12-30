/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.endless;

import com.jme3.bounding.BoundingBox;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author reden
 */
public class EndlessWorldControl extends AbstractControl implements ActionListener{

    private boolean moveForward, moveBackward, moveLeft, moveRight;
    private Camera cam;
    private int tileSize = 10;
    private Geometry currentTile;
    private Map<Vector2f, Geometry> cachedTiles = new HashMap<Vector2f, Geometry>();
    
    private Material material;
    
    public EndlessWorldControl(){
        
    }
            
    @Override
    protected void controlUpdate(float tpf) {
        Vector3f camDir = cam.getDirection().mult(tpf).multLocal(50);
        Vector3f camLeftDir = cam.getLeft().mult(tpf).multLocal(50);
        if(moveForward){
            moveTiles(camDir.negate());
        } else if (moveBackward){
            moveTiles(camDir);
        }
        if(moveLeft){
            moveTiles(camLeftDir.negate());
        } else if (moveRight){
            moveTiles(camLeftDir);
        }
    }
    
    private void moveTiles(Vector3f amount){
        for(Geometry g: cachedTiles.values()){
            g.move(amount);
        }
        Vector2f newLocation = null;
        Iterator<Vector2f> it = cachedTiles.keySet().iterator();
        while(it.hasNext() && newLocation == null){
            Vector2f tileLocation = it.next();
            Geometry g = cachedTiles.get(tileLocation);
            if(currentTile != g && g.getWorldBound().contains(Vector3f.ZERO.add(0, -15, 0))){
                currentTile = g;
                newLocation = tileLocation;

            }
        }
        if(newLocation != null){
            updateTiles(newLocation);
            deleteTiles(newLocation);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Forward")) {
            moveForward = isPressed;
        } else if (name.equals("Back")) {
            moveBackward = isPressed;
        } else if (name.equals("Left")) {
            moveLeft = isPressed;
        } else if (name.equals("Right")) {
            moveRight = isPressed;
        }
    }
    
    public void setCamera(Camera cam){
        this.cam = cam;
        for(Geometry g: cachedTiles.values()){
            g.move(cam.getLocation().negate());
        }
    }
    
    private void updateTiles(Vector2f newLocation){
        for(int x = -1; x < 2; x++){
            for(int y = -1; y < 2; y++){
                Vector2f wantedLocation = newLocation.add(new Vector2f(x,y));
                if(!cachedTiles.containsKey(wantedLocation)){
                    Geometry g = new Geometry(wantedLocation.x + ", " + wantedLocation.y, new Box(tileSize * 0.5f, 1, tileSize * 0.5f));
                    g.setMaterial(material.clone());
                    g.getMaterial().setColor("Color", ColorRGBA.randomColor());
                    g.setModelBound(new BoundingBox(Vector3f.ZERO, tileSize, tileSize, tileSize));
                    
                    Vector3f location = new Vector3f(x * tileSize, 0, y * tileSize);
                    if(currentTile != null){
                        location.addLocal(currentTile.getLocalTranslation());
                    }
                    g.setLocalTranslation(location);
                    cachedTiles.put(wantedLocation, g);
                    ((Node)spatial).attachChild(g);
                }
            }
        }
    }
    
    private void deleteTiles(Vector2f newLocation){
        Iterator<Vector2f> it = cachedTiles.keySet().iterator();
        List<Vector2f> tilesToDelete = new ArrayList<Vector2f>();
        while(it.hasNext()){
            Vector2f tileLocation = it.next();
            if(tileLocation.x > newLocation.x + 2 || tileLocation.x < newLocation.x - 2 || tileLocation.y > newLocation.y + 2 || tileLocation.y < newLocation.y - 2){
                tilesToDelete.add(tileLocation);
            }
        }
        for(Vector2f location: tilesToDelete){
            Geometry g = cachedTiles.remove(location);
            ((Node)spatial).detachChild(g);
        }
    }
    
    public void setMaterial(Material m){
        this.material = m;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        updateTiles(Vector2f.ZERO);
    }
    
    
}
