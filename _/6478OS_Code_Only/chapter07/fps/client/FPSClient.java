/*
 * Copyright (c) 2011 jMonkeyEngine
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
package chapter07.fps.client;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import java.io.IOException;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.Callable;
import chapter07.fps.client.object.ClientBullet;
import chapter07.fps.client.object.ClientPlayerControl;
import chapter07.fps.common.message.PlayerActionMessage;
import chapter07.fps.common.object.Game;
import chapter07.fps.common.object.GameUtil;
import chapter07.fps.common.object.NetworkedPlayerControl;
import chapter08.network.ClientPhysicsAppState;
import com.jme3.light.AmbientLight;

public class FPSClient extends SimpleApplication{

    private Client client;
    private ClientPlayerControl thisPlayer;
    private Game game;
    
    private Material playerMaterial;
//    private Geometry playerGeometry;
    private Node playerModel;
    
    /**
     * Firing
     */
    private HashMap<Integer, ClientBullet> bullets = new HashMap<Integer, ClientBullet>();
    /**
     * Firing
     */
    
    /**
     * Level loading
     */
    private Node levelNode = new Node("Level");
    /**
     * /Level loading
     */
 
    public static void main(String[] args) throws Exception {
        GameUtil.initialize();
        
        FPSClient client = new FPSClient();
        client.setPauseOnLostFocus(false);
        AppSettings settings = new AppSettings(false);
        settings.setFrameRate(60);
        client.setSettings(settings);
        client.start();
    }

    public FPSClient() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream("chapter07/resources/network.properties"));
        client = Network.connectToServer(prop.getProperty("server.name"), Integer.parseInt(prop.getProperty("server.version")), prop.getProperty("server.address"), Integer.parseInt(prop.getProperty("server.port")));
    }
    
    public ClientPlayerControl getThisPlayer(){
        return thisPlayer;
    }
    
    public void setThisPlayer(final ClientPlayerControl player){
        this.thisPlayer = player;
        final CameraNode camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        enqueue(new Callable(){

            public Object call() throws Exception {
                ((Node)player.getSpatial()).detachChildAt(0);
                player.getHeadNode().attachChild(camNode);
                return null;
            }
        });
        
    }

    public ClientPlayerControl createPlayer(int id){
        ClientPlayerControl player = new ClientPlayerControl();
        player.setId(id);
        final Node playerNode = new Node("Player Node");
        playerNode.attachChild(assetManager.loadModel("Models/Jaime/Jaime.j3o"));//
        playerNode.addControl(player);
        enqueue(new Callable(){

            public Object call() throws Exception {
                rootNode.attachChild(playerNode);
                return null;
            }
        });
        
        return player;
    }
    
    public void removePlayer(ClientPlayerControl player){
        rootNode.detachChild(player.getSpatial());
    }
    
    @Override
    public void simpleInitApp() {
        InputAppState inputAppState = new InputAppState();
        inputAppState.setClient(this);
        stateManager.attach(inputAppState);
        
        playerMaterial  = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        playerGeometry = new Geometry("Player", new Box(1f,1f,1f));
//        playerGeometry.setMaterial(playerMaterial);
        
        getFlyByCamera().setEnabled(false);
        
        game = new Game();
        ClientMessageHandler messageHandler = new ClientMessageHandler(this, game);
        client.addMessageListener(messageHandler);
        client.start();
        
        rootNode.addLight(new AmbientLight());
    }
    
    public void send(Message message){
        client.send(message);
    }
    
    /**
     * Level loading
     */
    
    public void loadLevel(final String levelName){
        enqueue(new Callable(){

            public Object call() throws Exception {
                if(rootNode.hasChild(levelNode)){
                    rootNode.detachChild(levelNode);
                }
                levelNode = (Node) assetManager.loadModel("Scenes/"+levelName + ".j3o");
                rootNode.attachChild(levelNode);
                
                /**
                * Physics
                */
               ClientPhysicsAppState clientPhysics = new ClientPhysicsAppState();
               clientPhysics.setGame(game);
               clientPhysics.setClient(FPSClient.this);
               stateManager.attach(clientPhysics);

               /**
                * Physics
                */
                return null;
            }
        });
        
    }
    
    public Node getLevelNode(){
        return levelNode;
    }
    /**
     * /Level loading
     */

    @Override
    public void destroy() {
        super.destroy();
        client.close();
    }

    
    /**
     * Firing
     */
    public ClientBullet createBullet(int id){
        final ClientBullet bulletControl = new ClientBullet();
        final Spatial g = assetManager.loadModel("Models/Banana/banana.j3o");
        g.rotate(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
        
        g.addControl(bulletControl);
        bullets.put(id, bulletControl);
        enqueue(new Callable<Object>() {

            public Object call() throws Exception {
                rootNode.attachChild(g);
                return null;
            }
        });
        return bulletControl;
    }
    
    public ClientBullet getBullet(int id){
        return bullets.get(id);
    }
    
    public void removeBullet(int id, final Spatial g){
        bullets.remove(id);
        enqueue(new Callable<Object>() {

            public Object call() throws Exception {
                rootNode.detachChild(g);
                return null;
            }
        });
    }
    /**
     * /Firing
     */
}
