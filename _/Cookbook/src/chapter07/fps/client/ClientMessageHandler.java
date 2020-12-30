/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.client;

import com.bulletphysics.dynamics.RigidBody;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import chapter07.fps.client.object.ClientBullet;
import chapter07.fps.client.object.ClientPlayerControl;
import chapter07.fps.client.object.PhysicsObjectControl;
import chapter07.fps.common.message.LoadLevelMessage;
import chapter07.fps.common.message.BulletUpdateMessage;
import chapter07.fps.common.message.PlayerJoinMessage;
import chapter07.fps.common.message.PlayerUpdateMessage;
import chapter07.fps.common.message.WelcomeMessage;
import chapter07.fps.common.object.Game;
import chapter07.message.messages.PhysicsObjectMessage;

/**
 *
 * @author reden
 */
public class ClientMessageHandler implements MessageListener<Client>{

    private FPSClient gameClient;
    private Game game;
    
    public ClientMessageHandler(FPSClient client, Game game){
        this.gameClient = client;
        this.game = game;
    }
    
    public void messageReceived(Client source, Message m) {
        if(m instanceof WelcomeMessage){
            ClientPlayerControl p = gameClient.createPlayer(((WelcomeMessage)m).getMyPlayerId());
//            gameClient.getRootNode().detachChild(p.getSpatial());
            
            gameClient.setThisPlayer(p);
            System.out.println("Your id is " + gameClient.getThisPlayer().getId());
            game.addPlayer(gameClient.getThisPlayer());
        } else if (m instanceof PlayerJoinMessage){
            PlayerJoinMessage joinMessage = (PlayerJoinMessage) m;
            int playerId = joinMessage.getPlayerId();
            if(joinMessage.isLeaving()){
                gameClient.removePlayer((ClientPlayerControl) game.getPlayer(playerId));
                game.removePlayer(playerId);
                System.out.println("Player " + playerId + " has left");
            } else if(game.getPlayer(playerId) == null){
                ClientPlayerControl p = gameClient.createPlayer(joinMessage.getPlayerId());
                game.addPlayer(p);
                System.out.println("Player " + playerId + " has joined!");
            }
            
        } else if (m instanceof PlayerUpdateMessage){
            PlayerUpdateMessage updateMessage = (PlayerUpdateMessage) m;
            int playerId = updateMessage.getPlayerId();
            final ClientPlayerControl p = (ClientPlayerControl) game.getPlayer(playerId);
            if(p != null){
                p.onMessageReceived(updateMessage);
                /**
                * Visibility
                */
                if(p.isVisible() && p.getSpatial().getParent() == null){
                    gameClient.enqueue(new Callable(){

                        public Object call() throws Exception {
                            gameClient.getRootNode().attachChild(p.getSpatial());
                            return null;
                        }
                    });
                    
                } else if (!p.isVisible() && p.getSpatial().getParent() != null){
                    gameClient.enqueue(new Callable(){

                        public Object call() throws Exception {
                            gameClient.getRootNode().detachChild(p.getSpatial());
                            return null;
                        }
                    });
                    
                }
                /**
                * /Visibility
                */
            }
            
        }
        /**
        * Level loading
        */
        else if (m instanceof LoadLevelMessage){
            gameClient.loadLevel(((LoadLevelMessage)m).getLevelName() );
            game.setLevelName(((LoadLevelMessage)m).getLevelName());
            
        }
        
        /**
        * /Level loading
        */
        
        /**
         * Firing
         */
        else if (m instanceof BulletUpdateMessage){
            BulletUpdateMessage update = (BulletUpdateMessage) m;
            ClientBullet bullet = gameClient.getBullet(update.getId());
            if(bullet == null){
                bullet = gameClient.createBullet(update.getId());
            }
            bullet.setPosition(update.getPosition());
            if(!update.isAlive()){
                gameClient.removeBullet(update.getId(), bullet.getSpatial());
            }
        }
        /**
         * /Firing
         */
        
        /**
         * Physics
         */
        else if (m instanceof PhysicsObjectMessage){
            PhysicsObjectMessage physicsMessage = (PhysicsObjectMessage)m;
            int objectId = physicsMessage.getObjectId();
            Map<Integer, Spatial> physicsObjects = game.getPhysicsObjects();
            Spatial s = physicsObjects.get(objectId);
            if(s != null){
                PhysicsObjectControl physicsControl = s.getControl(PhysicsObjectControl.class);
                if(physicsControl.getId() == objectId){
                    s.getControl(RigidBodyControl.class).setPhysicsLocation(physicsMessage.getTranslation());
                    s.getControl(RigidBodyControl.class).setPhysicsRotation(physicsMessage.getRotation());
                }
            }
        }
        /**
         * 
         */
    }
    
}
