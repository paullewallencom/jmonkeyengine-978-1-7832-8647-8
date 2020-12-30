/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.cubeworld;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 *
 * @author reden
 */
public class TestCell extends SimpleApplication{

    public static void main(String[] args){
        TestCell test = new TestCell();
        test.start();
    }
    @Override
    public void simpleInitApp() {
        
        CubeCell cube = new CubeCell();
//        cube.setNeighbor(2, true);
        Geometry g = new Geometry("Cube", cube.getMesh());
        g.setMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"));
        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(-0.3f, -0.5f, -0.2f));
        rootNode.addLight(light);
        rootNode.attachChild(g);
        
    }
    
}
