/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.cubeworld;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author reden
 */
public class CubeWorld {
    
    private CubeCell[][][] terrainBlock;
    int batchSize = 10;
    
    private Node world;
    
    private Material[] materials;

    public CubeWorld(){
            world = new Node();
    }
    
    public void generate(){
        terrainBlock = CubeUtil.generateBlock(4, batchSize);
        generateGeometry(Vector3f.ZERO);
    }
    
    private Spatial generateGeometry(Vector3f blockCoords){
//        Geometry g = new Geometry("Terrain Block");
        int locX = Math.round(blockCoords.x);
        int locY = Math.round(blockCoords.y);
        int locZ = Math.round(blockCoords.z);
        String name = "Block " + locX + ", " + locY + ", " + locZ;
        if(world.getChild(name) != null){
            world.detachChildNamed(name);
        }
        BatchNode node = new BatchNode(name);

        for(int y = 0; y < batchSize; y++){
            for(int z = 0; z < batchSize; z++){
                for(int x = 0; x < batchSize; x++){
                    if(terrainBlock[x][y][z] != null){
                        for(int i = 0; i < 6; i++){
                            Vector3f coords = CubeUtil.directionToCoords(i);
                            if(coords.x + x > -1 && coords.x + x < batchSize){
                                if(coords.z + z > -1 && coords.z + z < batchSize){
                                    if(coords.y + y > -1 && coords.y + y < batchSize){
                                        if(terrainBlock[(int)coords.x + x][(int)coords.y + y][(int)coords.z + z] != null){
                                            terrainBlock[x][y][z].setNeighbor(i, true);
                                        } else {
                                            terrainBlock[x][y][z].setNeighbor(i, false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        for(int y = 0; y < batchSize; y++){
            for(int z = 0; z < batchSize; z++){
                for(int x = 0; x < batchSize; x++){
                    if(terrainBlock[x][y][z] != null){
                        
                        Geometry g = new Geometry("Cube", terrainBlock[x][y][z].getMesh() );
                        g.setLocalTranslation(x, y, z);
                        g.setMaterial(materials[0]);
                        node.attachChild(g);
                    }
                }
            }
        }
        node.updateModelBound();
        node.batch();
        world.attachChild(node);
        return node;
    }
    
    public CubeCell changeTerrain(Vector3f coords, CubeCell blockToPlace){
        CubeCell changedBlock = blockToPlace;
        int x = (int) coords.x;
        int y = (int) coords.y;
        int z = (int) coords.z;
        if(x > -1 && x < batchSize){
            if(z > -1 && z < batchSize){
                if(y > -1 && y < batchSize){
                    if(changedBlock == null){
                        changedBlock = terrainBlock[x][y][z];
                        terrainBlock[x][y][z] = null;
                    } else if(terrainBlock[x][y][z] == null){
                        terrainBlock[x][y][z] = changedBlock;
                        terrainBlock[x][y][z].requestRefresh();
                        changedBlock = null;
                    }
                    generateGeometry(coords);
                }
            }
        }
        return changedBlock;
    }
    
    public Node getWorld(){
        return world;
    }
    
    public void setMaterials(Material[] materials){
        this.materials = materials;
    }
}
