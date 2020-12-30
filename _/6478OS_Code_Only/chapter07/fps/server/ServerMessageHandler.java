/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.server;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import chapter07.fps.common.message.PlayerActionMessage;
import chapter07.fps.common.message.PlayerMessage;
import chapter07.fps.common.object.Game;
import chapter07.fps.common.object.NetworkedPlayerControl;

    
/**
 *
 * @author reden
 */
public class ServerMessageHandler implements MessageListener<HostedConnection>{

    private FPSServer server;
    private Game game;

    public ServerMessageHandler(FPSServer server, Game game) {
        this.server = server;
        this.game = game;
    }
    
    
    
    public void messageReceived(HostedConnection source, Message m) {
        if(m instanceof PlayerActionMessage){
            PlayerActionMessage message = (PlayerActionMessage)m;
            NetworkedPlayerControl p = game.getPlayer(message.getPlayerId());
            p.onMessageReceived(message);
            
            /**
             * Firing
             */
            if(message.getAction().equals("Fire") && message.isPressed()){
                server.onFire(p);
            }
            /**
             * 
             */
        }
    }
}
