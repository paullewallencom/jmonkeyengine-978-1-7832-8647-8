/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.cubeworld;

import com.jme3.scene.Mesh;

/**
 *
 * @author reden
 */
public class CubeCell {
    
    private Mesh mesh;
    private boolean[] neighbors = new boolean[6]; // n, e, s, w, up, down
    private boolean refresh;
    
    public enum Type{
        Rock,
        Sand,
        Grass,
    }
    private Type type;
    
    public boolean hasNeighbor(int direction){
        return neighbors[direction];
    }
    
    public void setNeighbor(int direction, boolean neighbor){
        if(neighbors[direction] != neighbor){
            neighbors[direction] = neighbor;
            refresh = true;
        }
        
    }
    
    public Mesh getMesh(){
        if(mesh == null || refresh){
            mesh = CubeUtil.createMesh(this);
            refresh = false;
        }
        return mesh;
    }
    
    public void requestRefresh(){
        refresh = true;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
