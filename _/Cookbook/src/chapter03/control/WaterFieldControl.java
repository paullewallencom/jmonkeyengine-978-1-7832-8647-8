/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.control;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.terrain.Terrain;
import jme3tools.optimize.GeometryBatchFactory;
import chapter03.WaterCell;
import chapter03.util.CellUtil;

/**
 *
 * @author reden
 */
public class WaterFieldControl extends AbstractControl{

    private int width = 256;
    private int height = 256;
    
    private WaterCell[][] waterField = new WaterCell[width][height];
    private Node water;
    private Material material;
    
    private float updateRate = 1f / 10f;
    private float time = 0;
    
    @Override
    protected void controlUpdate(float tpf) {
        time += tpf;
        if(time > updateRate){
            long startTime = System.currentTimeMillis();
            updateCells();
            createGeometry();
            
            time -= updateRate;
            System.out.println(System.currentTimeMillis() - startTime);
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        
        for(Spatial s: ((Node)spatial).getChildren()){
            if(s instanceof Terrain){
                for(int x = 0; x < width; x++){
                    for(int y = 0; y < height; y++){
                        WaterCell cell = new WaterCell();
                        cell.setTerrainHeight(((Terrain)s).getHeight(new Vector2f(x, y)));
                        waterField[x][y] = cell;
                    }
                }
            }
        }
    }
    
    
    
    private void updateCells(){
        waterField[127][127].setAmount(1);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                WaterCell cell = waterField[x][y];
                float cellAmount = cell.getAmount();
                if(cellAmount > 0){
                    int direction = cell.getDirection();
                    for(int i = 0; i < 8; i++){
                        int[] dir = CellUtil.getDirection((direction + i) % 8);
                        int dx = dir[0];
                        int dy = dir[1];
                        if(x + dx > -1 && x + dx < width && y + dy > -1 && y + dy < height && (x != 0 || y != 0)){
                            WaterCell neighborCell = waterField[x+dx][y+dy];
                            if(cellAmount > 0.01){
                                float adjustAmount = neighborCell.compareCells(cell.getTerrainHeight(), cellAmount);
                                cellAmount -= adjustAmount;
                                if(adjustAmount > 0){
                                    neighborCell.setDirection(CellUtil.getDirection(dx, dy));
                                }
                            }
                        }
                    }
                    cell.adjustIncomingWater(cellAmount);
                }
            }
        }
        
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                 waterField[x][y].setAmount(waterField[x][y].getIncomingWater());
                 waterField[x][y].setIncomingWater(0);
            }
        }
        
    }
    
    private void createGeometry(){
        if(((Node)spatial).getChild("Water") != null){
            ((Node)spatial).detachChildNamed("Water");
        }
        water = new Node("Water");
        water.setLocalTranslation(0, -100, 0);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                WaterCell cell = waterField[x][y];
                if(cell.getAmount() > 0){
//                    Geometry g = new Geometry("WaterCell", new Box(1f,  1f + cell.getAmount(), 1f));
//                    g.setLocalTranslation(x, -1f + cell.getTerrainHeight() + cell.getAmount() * 0.5f, y);
//                    water.attachChild(g);
                    Geometry g = cell.getGeometry();
                    if(g != null){
                        g.setLocalTranslation(x, -1f + cell.getTerrainHeight() + cell.getAmount() * 0.5f, y);
                        water.attachChild(g);
                    }
                    
                }
                 
            }
        }
        
        water = GeometryBatchFactory.optimize(water, false);
        water.setMaterial(material);
        ((Node)spatial).attachChild(water);
    }
    
    public void setMaterial(Material mat){
        this.material = mat;
    }
    
    public void addSource(int x, int y){
        
    }
    
    public void removeSource(int x, int y){
        
    }
}
