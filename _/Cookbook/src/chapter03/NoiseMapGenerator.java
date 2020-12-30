/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03;

import com.jme3.math.FastMath;
import com.jme3.terrain.noise.fractal.FractalSum;
import chapter03.util.ImageGenerator;
import chapter03.util.ImprovedNoise;

/**
 *
 * @author reden
 */
public class NoiseMapGenerator {
    
    private final static long TERRAIN_SEED = 1238123987;
    private final static int size = 1024;
    private final static float FREQUENCY = 0.01f;
    private final static double PERSISTENCE = 1f;
    private FractalSum fractalSum;
    
    public static void main(String[] args){
        new NoiseMapGenerator().generateNoiseMap(size, FREQUENCY, 2);
//        new TerrainGenerator().generateTerrain();
    }

    public NoiseMapGenerator() {
        fractalSum = new FractalSum();
    }
    
    
    public void generateNoiseMap(int size, float frequency, int octaves){
        float[][] terrain = new float[size][size];
        
        fractalSum.setFrequency(frequency);
        fractalSum.setAmplitude(0.5f);
        fractalSum.setOctaves(octaves);
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                float value = fractalSum.value(x, 0, y) + 0.5f;
                value = FastMath.clamp(value, 0f, 1f);
                terrain[x][y] = value;
            }
        }
        ImageGenerator.generateImage(terrain);
    }
    
    private double[][] normalize(double[][] terrain, double minHeight, double maxHeight){
        double diff = maxHeight - minHeight;
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                terrain[x][y] = (terrain[x][y] - minHeight) / diff;
            }
        }
        return terrain;
    }
    
    
}
