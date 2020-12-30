package chapter07;

import java.io.IOException;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import java.util.Properties;
import chapter07.message.messages.ServerMessage;

public class NetworkTestClient {

    private Client client;
    
    public static void main(String[] args) throws Exception {
        /**
        * Messaging
        */
        Serializer.registerClass(ServerMessage.class);
        /**
         * 
         */
        
        NetworkTestClient client = new NetworkTestClient();
    }

    public NetworkTestClient() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream("network/resources/network.properties"));
        client = Network.connectToServer(prop.getProperty("server.address"), Integer.parseInt(prop.getProperty("server.port")));
        client.start();
        
        /**
        * Messaging
        */
        ServerMessageHandler serverMessageHandler = new ServerMessageHandler();
        client.addMessageListener(serverMessageHandler, ServerMessage.class);
        /**
         * 
         */
    }
}
