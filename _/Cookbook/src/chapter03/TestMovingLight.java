/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package chapter03;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.LightNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import chapter03.control.SpotControl;
import chapter03.control.TreeControl;
import chapter03.control.WaterFieldControl;

/**
 * Demonstrates how to use terrain.
 * The base terrain class it uses is TerrainQuad, which is a quad tree of actual
 * meshes called TerainPatches.
 * There are a couple options for the terrain in this test:
 * The first is wireframe mode. Here you can see the underlying trianglestrip structure.
 * You will notice some off lines; these are degenerate triangles and are part of the
 * trianglestrip. They are only noticeable in wireframe mode.
 * Second is Tri-Planar texture mode. Here the textures are rendered on all 3 axes and
 * then blended together to reduce distortion and stretching.
 * Third, which you have to modify the code to see, is Entropy LOD calculations.
 * In the constructor for the TerrainQuad, un-comment the final parameter that is
 * the LodPerspectiveCalculatorFactory. Then you will see the terrain flicker to start
 * while it calculates the entropies. Once it is done, it will pick the best LOD value
 * based on entropy. This method reduces "popping" of terrain greatly when LOD levels
 * change. It is highly suggested you use it in your app.
 *
 * @author bowens
 */
public class TestMovingLight extends SimpleApplication {

    private TerrainQuad terrain;
    Material matTerrain;
    Material matWire;
    boolean wireframe = false;
    boolean triPlanar = false;
    protected BitmapText hintText;
    PointLight pl;
    Geometry lightMdl;
    private float grassScale = 64;
    private float dirtScale = 16;
    private float rockScale = 128;
    
    private Node worldNode = new Node("world");

    public static void main(String[] args) {
        TestMovingLight app = new TestMovingLight();
        app.start();
    }

    @Override
    public void initialize() {
        super.initialize();

        loadHintText();
    }

    @Override
    public void simpleInitApp() {
        setupKeys();

        Spatial s = assetManager.loadModel("Models/Sponza/Sponza.j3o");
        s.setMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"));
        rootNode.attachChild(s);
        
        PointLight pointLight = new PointLight();
        pointLight.setRadius(30);
        rootNode.addLight(pointLight);
        
        CameraNode camNode = new CameraNode("", cam);
        camNode.setControlDir(CameraControl.ControlDirection.CameraToSpatial);
        rootNode.attachChild(camNode);
        LightControl lightControl = new LightControl(pointLight);
        lightControl.setControlDir(LightControl.ControlDirection.SpatialToLight);
        camNode.addControl(lightControl);

    }

    public void loadHintText() {
        hintText = new BitmapText(guiFont, false);
        hintText.setSize(guiFont.getCharSet().getRenderedSize());
        hintText.setLocalTranslation(0, getCamera().getHeight(), 0);
        hintText.setText("Hit T to switch to wireframe,  P to switch to tri-planar texturing");
        guiNode.attachChild(hintText);
    }

    private void setupKeys() {
        flyCam.setMoveSpeed(50);
        inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(actionListener, "wireframe");
        inputManager.addMapping("triPlanar", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "triPlanar");
    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean pressed, float tpf) {
            if (name.equals("wireframe") && !pressed) {
                wireframe = !wireframe;
                if (!wireframe) {
                    terrain.setMaterial(matWire);
                } else {
                    terrain.setMaterial(matTerrain);
                }
            } else if (name.equals("triPlanar") && !pressed) {
                triPlanar = !triPlanar;
                if (triPlanar) {
                    matTerrain.setBoolean("useTriPlanarMapping", true);
                    // planar textures don't use the mesh's texture coordinates but real world coordinates,
                    // so we need to convert these texture coordinate scales into real world scales so it looks
                    // the same when we switch to/from tr-planar mode
                    matTerrain.setFloat("Tex1Scale", 1f / (float) (512f / grassScale));
                    matTerrain.setFloat("Tex2Scale", 1f / (float) (512f / dirtScale));
                    matTerrain.setFloat("Tex3Scale", 1f / (float) (512f / rockScale));
                } else {
                    matTerrain.setBoolean("useTriPlanarMapping", false);
                    matTerrain.setFloat("Tex1Scale", grassScale);
                    matTerrain.setFloat("Tex2Scale", dirtScale);
                    matTerrain.setFloat("Tex3Scale", rockScale);
                }
            }
        }
    };
}
