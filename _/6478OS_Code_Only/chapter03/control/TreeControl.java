/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.control;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.noise.fractal.FractalSum;
import chapter03.WaterCell;

/**
 *
 * @author reden
 */
public class TreeControl extends AbstractControl{

    private TerrainQuad terrain;
    private FractalSum fractalSum;
    
    private Spatial treeModel;
    private BatchNode treeNode;
    
    private float treeLimit = 45f;

    public TreeControl() {
        fractalSum = new FractalSum();
        fractalSum.setOctaves(1);
        fractalSum.setFrequency(0.02f);
    }
    
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        treeNode = new BatchNode();
        for(Spatial s: ((Node)spatial).getChildren()){
            if(s instanceof TerrainQuad){
                this.terrain = (TerrainQuad) s;
                
                int size = (int) (terrain.getTerrainSize());
                for(int x = -size; x < size; x++){
                    for(int y = -size; y < size; y++){
                        float value = fractalSum.value(x, 0, y);
                        float terrainHeight = terrain.getHeight(new Vector2f(x, y));
                        if(value > 0.5f && terrainHeight < treeLimit){
                            Spatial treeClone = treeModel.clone();
                            Vector3f location = new Vector3f((x), terrainHeight, (y));
                            treeClone.setLocalTranslation(location);
                            treeNode.attachChild(treeClone);
                        }
                    }
                }
                treeNode.batch();
                ((Node)spatial).attachChild(treeNode);
            }
        }
    }
    
    private void placeTree(float x, float y){
        
    }

    public void setTreeModel(Spatial treeModel) {
        this.treeModel = treeModel;
    }
    
}
