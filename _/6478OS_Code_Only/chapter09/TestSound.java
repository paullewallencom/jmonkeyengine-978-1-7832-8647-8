/*
 * Copyright (c) 2009-2012 jMonkeyEngine All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;
import com.jme3.audio.Filter;
import com.jme3.audio.LowPassFilter;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.NativeObject;

public class TestSound extends SimpleApplication {

    private AudioNode audioNode;
    private float time = 0;
    private float pauseTime = 0.7f;

    public static void main(String[] args) {
        TestSound test = new TestSound();
        test.start();
    }

    @Override
    public void simpleInitApp() {
        audioNode = new AudioNode(assetManager, "Sound/Effects/Foot steps.ogg");
        
        Environment env = Environment.Cavern;
        audioRenderer.setEnvironment(env);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (time > pauseTime) {
            LowPassFilter f = new LowPassFilter(1, 1);
            audioNode.setDryFilter(f);
            float pitch = FastMath.nextRandomFloat() * 0.2f + 0.9f;
//            audioNode.setPitch(pitch);
            audioNode.setTimeOffset(2.0f);
            audioNode.playInstance();
            time = 0;
        }
        time += tpf;
    }
}
