/*
 * Copyright (c) 2003-2009 jMonkeyEngine
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

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * <code>ClothPatch</code> is a rectangular trimesh representing a piece of
 * Cloth. It is backed up by and shares verts and normals with a SpringSystem.
 * 
 * @author Joshua Slack
 * @version $Id$
 */
public class WaterMesh extends Mesh {
    private static final long serialVersionUID = 1L;

    /** width, number of nodes wide on x axis. */
    protected int clothNodesX;
    /** height, number of nodes high on y axis. */
    protected int clothNodesY;
    
    // Temp vars used to eliminates object creation
    private Vector3f tNorm = new Vector3f();
    private Vector3f tempV1 = new Vector3f(), tempV2 = new Vector3f(),
            tempV3 = new Vector3f();

    public WaterMesh() {
    }

    /**
     * Public constructor.
     * 
     * @param name
     *            String
     * @param nodesX
     *            number of nodes wide this cloth will be.
     * @param nodesY
     *            number of nodes high this cloth will be.
     * @param springLength
     *            distance between each node
     * @param nodeMass
     *            mass of an individual node in this Cloth.
     */
    public WaterMesh(String name, int nodesX, int nodesY, float springLength,
            float nodeMass) {
        super();
        clothNodesX = nodesX;
        clothNodesY = nodesY;
        int vertexCount = clothNodesY * clothNodesX;
        //setVertexCount(clothNodesY * clothNodesX);
        setBuffer(Type.Position, 3, BufferUtils.createVector3Buffer(vertexCount));
        setBuffer(Type.Normal, 3, BufferUtils.createVector3Buffer(vertexCount));
        setBuffer(Type.TexCoord, 2, BufferUtils.createVector2Buffer(vertexCount));

        //setTriangleQuantity((clothNodesX - 1) * (clothNodesY - 1) * 2);
        int triCount = (clothNodesX - 1) * (clothNodesY - 1) * 2;
        setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(3 * triCount));

        initCloth();
    }

    /**
     * Update the normals in the system.
     */
    public void updateNormals() {
        // zero out the normals
        FloatBuffer b = getFloatBuffer(Type.Normal);

        b.clear();
        for (int i = b.capacity(); --i >= 0;)
            b.put(0);

        // go through each triangle and add the tri norm to it's corner's norms
        int i1, i2, i3;
        getBuffer(Type.Normal).getData().rewind();
        
        for (int i = 0, iMax = b.capacity(); i < iMax; i += 3) {
            // grab triangle normal
            i1 = getIndexBuffer().get(i);
            i2 = getIndexBuffer().get(i+1);
            i3 = getIndexBuffer().get(i+2);
            getTriangleNormal(i1, i2, i3, tNorm);
            BufferUtils.addInBuffer(tNorm, getFloatBuffer(Type.Normal), i1);
            BufferUtils.addInBuffer(tNorm, getFloatBuffer(Type.Normal), i2);
            BufferUtils.addInBuffer(tNorm, getFloatBuffer(Type.Normal), i3);
        }
        // normalize
        for (int i = getVertexCount(); --i >= 0;)
            BufferUtils.normalizeVector3(getFloatBuffer(Type.Normal), i);
    }

    /**
     * Get the normal of the triangle defined by the given vertices. Please note
     * that result is not normalized.
     * 
     * @param vert1
     *            triangle point #1
     * @param vert2
     *            triangle point #2
     * @param vert3
     *            triangle point #3
     * @param store
     *            Vector3f to store result in
     * @return normal of triangle, same as store param.
     */
    protected Vector3f getTriangleNormal(int vert1, int vert2, int vert3,
            Vector3f store) {
        BufferUtils.populateFromBuffer(tempV1, getFloatBuffer(Type.Position), vert1);
        BufferUtils.populateFromBuffer(tempV2, getFloatBuffer(Type.Position), vert2);
        BufferUtils.populateFromBuffer(tempV3, getFloatBuffer(Type.Position), vert3);
        
        // Translate(v2, v1);
        tempV2.subtractLocal(tempV1);

        // Translate(v3, v1);
        tempV3.subtractLocal(tempV1);

        // Result = CrossProduct(v1, v3);
        tempV2.cross(tempV3, store);

        return store;
    }

    /**
     * Initialize the values of the vertex, normal and texture[0] arrays. Build
     * a SpringSystem and call setupIndices(). Then update the various buffers.
     * 
     * @param nodeMass
     *            mass of individual node.
     * @param upperLeft
     *            the upper left corner of the rectangle.
     * @param lowerLeft
     *            the lower left corner of the rectangle.
     * @param lowerRight
     *            the lower right corner of the rectangle.
     * @param upperRight
     *            the upper right corner of the rectangle.
     */
    protected void initCloth() {
        // Setup our shared vectors as a bilinear combination of the 4 corners
        Vector2f texcoord = new Vector2f();
        Vector3f vert = new Vector3f();
        FloatBuffer texs = getFloatBuffer(Type.TexCoord);
        for (int j = 0; j < clothNodesY; j++) {
            for (int i = 0; i < clothNodesX; i++) {
                int ind = getIndex(i, j);
                vert.set(i, 0, j);
                BufferUtils.setInBuffer(vert, getFloatBuffer(Type.Position), ind);
                texcoord.set((float) i / (clothNodesX - 1),
                        (float) (clothNodesY - (j + 1)) / (clothNodesY - 1));
                BufferUtils.setInBuffer(texcoord, texs, ind);
            }
        }

        setupIndices();
    }

    /**
     * Return how many nodes high this cloth is.
     * 
     * @return int
     */
    public int getClothNodesY() {
        return clothNodesY;
    }

    /**
     * Return how many nodes wide this cloth is.
     * 
     * @return int
     */
    public int getClothNodesX() {
        return clothNodesX;
    }

    /**
     * Setup the triangle indices for this cloth.
     */
    protected void setupIndices() {
        IntBuffer b = (IntBuffer) getIndexBuffer().getBuffer().rewind();
        int[] indices = new int[clothNodesY * clothNodesX * 6]; 
        int i = 0;
        for (int Y = 0; Y < clothNodesY - 1; Y++) {
            for (int X = 0; X < clothNodesX - 1; X++) {
//                indices[i++] = getIndex(X, Y);
//                indices[i++] = getIndex(X, Y + 1);
//                indices[i++] = getIndex(X + 1, Y + 1);
//
//                indices[i++] = getIndex(X, Y);
//                indices[i++] = getIndex(X + 1, Y + 1);
//                indices[i++] = getIndex(X + 1, Y);
                
                b.put(getIndex(X, Y));
                b.put(getIndex(X, Y + 1));
                b.put(getIndex(X + 1, Y + 1));

                b.put(getIndex(X, Y));
                b.put(getIndex(X + 1, Y + 1));
                b.put(getIndex(X + 1, Y));
            }
        }
        //setBuffer(Type.Index, indices.length, indices);
    }

    /**
     * Convienence method for calculating the array index of a given node given
     * it's x and y coordiates.
     * 
     * @param x
     *            int
     * @param y
     *            int
     * @return index
     */
    protected int getIndex(int x, int y) {
        return y * clothNodesX + x;
    }

    public void setHeight(int x, int y, float height){
        
    }
}
