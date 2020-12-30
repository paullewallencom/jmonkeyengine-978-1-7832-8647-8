/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import chapter07.message.messages.ServerMessage;

/**
 *
 * @author reden
 */
public class ServerMessageHandler implements MessageListener<Client>{

    public void messageReceived(Client source, Message m) {
        ServerMessage message = (ServerMessage) m;
        System.out.println("Server message: " + message.getMessage());
    }
    
}
