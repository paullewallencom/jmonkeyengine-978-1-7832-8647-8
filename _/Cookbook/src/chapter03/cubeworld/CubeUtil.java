/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.cubeworld;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.terrain.noise.fractal.FractalSum;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.List;
import chapter03.util.ImprovedNoise;

/**
 *
 * @author reden
 */
public class CubeUtil {
    
    private final static long TERRAIN_SEED = 1254623987;
    private final static double FREQUENCY = 00.0671f;
    private final static double PERSISTENCE = 1f;
    private static FractalSum fractalSum;
    
    private static final Vector3f[] vPos = new Vector3f[] {
                Vector3f.ZERO.subtract(Vector3f.UNIT_X).subtractLocal(Vector3f.UNIT_Y).subtractLocal(Vector3f.UNIT_Z).multLocal(0.5f),
                Vector3f.ZERO.add(Vector3f.UNIT_X).subtractLocal(Vector3f.UNIT_Y).subtractLocal(Vector3f.UNIT_Z).multLocal(0.5f),
                Vector3f.ZERO.add(Vector3f.UNIT_X).addLocal(Vector3f.UNIT_Y).subtractLocal(Vector3f.UNIT_Z).multLocal(0.5f),
                Vector3f.ZERO.subtract(Vector3f.UNIT_X).addLocal(Vector3f.UNIT_Y).subtractLocal(Vector3f.UNIT_Z).multLocal(0.5f),
                Vector3f.ZERO.add(Vector3f.UNIT_X).subtractLocal(Vector3f.UNIT_Y).addLocal(Vector3f.UNIT_Z).multLocal(0.5f),
                Vector3f.ZERO.subtract(Vector3f.UNIT_X).subtractLocal(Vector3f.UNIT_Y).addLocal(Vector3f.UNIT_Z).multLocal(0.5f),
                Vector3f.ZERO.add(Vector3f.UNIT_X).addLocal(Vector3f.UNIT_Y).addLocal(Vector3f.UNIT_Z).multLocal(0.5f),
                Vector3f.ZERO.subtract(Vector3f.UNIT_X).addLocal(Vector3f.UNIT_Y).addLocal(Vector3f.UNIT_Z).multLocal(0.5f)
        };
    
    private static final float[] vertices = new float[] {
                vPos[0].x, vPos[0].y, vPos[0].z, vPos[1].x, vPos[1].y, vPos[1].z, vPos[2].x, vPos[2].y, vPos[2].z, vPos[3].x, vPos[3].y, vPos[3].z, // back
                vPos[1].x, vPos[1].y, vPos[1].z, vPos[4].x, vPos[4].y, vPos[4].z, vPos[6].x, vPos[6].y, vPos[6].z, vPos[2].x, vPos[2].y, vPos[2].z, // right
                vPos[4].x, vPos[4].y, vPos[4].z, vPos[5].x, vPos[5].y, vPos[5].z, vPos[7].x, vPos[7].y, vPos[7].z, vPos[6].x, vPos[6].y, vPos[6].z, // front
                vPos[5].x, vPos[5].y, vPos[5].z, vPos[0].x, vPos[0].y, vPos[0].z, vPos[3].x, vPos[3].y, vPos[3].z, vPos[7].x, vPos[7].y, vPos[7].z, // left
                vPos[2].x, vPos[2].y, vPos[2].z, vPos[6].x, vPos[6].y, vPos[6].z, vPos[7].x, vPos[7].y, vPos[7].z, vPos[3].x, vPos[3].y, vPos[3].z, // top
                vPos[0].x, vPos[0].y, vPos[0].z, vPos[5].x, vPos[5].y, vPos[5].z, vPos[4].x, vPos[4].y, vPos[4].z, vPos[1].x, vPos[1].y, vPos[1].z  // bottom
    };
    private static final int[] GEOMETRY_INDICES_DATA = {
         2,  1,  0,  3,  2,  0, // back
         6,  5,  4,  7,  6,  4, // right
        10,  9,  8, 11, 10,  8, // front
        14, 13, 12, 15, 14, 12, // left
        18, 17, 16, 19, 18, 16, // top
        22, 21, 20, 23, 22, 20  // bottom
    };

    private static final float[] GEOMETRY_NORMALS_DATA = {
        0,  0, -1,  0,  0, -1,  0,  0, -1,  0,  0, -1, // back
        1,  0,  0,  1,  0,  0,  1,  0,  0,  1,  0,  0, // right
        0,  0,  1,  0,  0,  1,  0,  0,  1,  0,  0,  1, // front
       -1,  0,  0, -1,  0,  0, -1,  0,  0, -1,  0,  0, // left
        0,  1,  0,  0,  1,  0,  0,  1,  0,  0,  1,  0, // top
        0, -1,  0,  0, -1,  0,  0, -1,  0,  0, -1,  0  // bottom
    };

    private static final float[] GEOMETRY_TEXTURE_DATA = {
        1, 0, 0, 0, 0, 1, 1, 1, // n
        1, 0, 0, 0, 0, 1, 1, 1, // e
        1, 0, 0, 0, 0, 1, 1, 1, // s
        1, 0, 0, 0, 0, 1, 1, 1, // w
        1, 0, 0, 0, 0, 1, 1, 1, // top
        1, 0, 0, 0, 0, 1, 1, 1  // bottom
    };
    
    public static Mesh createMesh(CubeCell cube){
        Mesh m = new Mesh();
        
        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        
        List<Integer> indices = new ArrayList<Integer>();
        for(int dir = 0; dir < 6; dir++){
            if(!cube.hasNeighbor(dir)){
                // add two triangles
                for(int j = 0; j < 6; j++){
                    indices.add(GEOMETRY_INDICES_DATA[dir * 6 + j]);
                }
            }
        }
        int[] indexArray = new int[indices.size()];
        int i = 0;
        for(Integer index: indices){
            indexArray[i++] = index;
        }
        m.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indexArray));
        
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(GEOMETRY_TEXTURE_DATA));
        m.setBuffer(VertexBuffer.Type.Normal, 3, GEOMETRY_NORMALS_DATA);
        return m;
    }
    
    private static Vector3f[] coords = new Vector3f[]{new Vector3f(0, 0, -1), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1), new Vector3f(-1, 0, 0), new Vector3f(0, 1, 0), new Vector3f(0, -1, 0)};
    
    public static Vector3f directionToCoords(int direction){
        return coords[direction];
    }
    
    public static CubeCell[][][] generateBlock(int octaves, int size){
        if(fractalSum == null){
            fractalSum = new FractalSum();
            fractalSum.setOctaves(octaves);
        }
        CubeCell[][][] terrainBlock = new CubeCell[size][size][size];
        for(int y = 0; y < size; y++){
            for(int z = 0; z < size; z++){
                for(int x = 0; x < size; x++){
                    double value = fractalSum.value(x, y, z);
                    System.out.println(value);
                    if(value >= 0.0f){
                        terrainBlock[x][y][z] = new CubeCell();
                    }
                    
                    
                }
            }
        }
        return terrainBlock;
    }
    
    static{
        
        
    }
}
