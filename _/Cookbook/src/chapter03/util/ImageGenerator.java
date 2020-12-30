/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import chapter03.NoiseMapGenerator;

/**
 *
 * @author reden
 */
public class ImageGenerator {
    
    public static void generateImage(float[][] terrain){
        int size = terrain.length;
        int grey;
        
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                double result = terrain[x][y];
                
                grey = (int) (result * 255);
                int color = (grey << 16) | (grey << 8) | grey;
                img.setRGB(x, y, color);
                   
            }
        }
        
        try {
            ImageIO.write(img, "png", new File("assets/Textures/heightmap.png"));
        } catch (IOException ex) {
            Logger.getLogger(NoiseMapGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
