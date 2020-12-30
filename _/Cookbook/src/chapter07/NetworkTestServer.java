package chapter07;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.util.Properties;
import chapter07.message.messages.ServerMessage;

public class NetworkTestServer
{
    public static void main(String[] args ) throws Exception
    {
        /**
        * Messaging
        */
        Serializer.registerClass(ServerMessage.class);  
        /**
        * 
        */
        
        NetworkTestServer gameServer = new NetworkTestServer();
        
        String WAIT = "wait";
        synchronized( WAIT ) {
            WAIT.wait();
        }          
    }
    
    private Server server;

    public NetworkTestServer() throws IOException{
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream("network/resources/network.properties"));

        server = Network.createServer(Integer.parseInt(prop.getProperty("server.port")));
        
        ConnectionListener conListener = new ConnectionListener() {

            public void connectionAdded(Server server, HostedConnection conn) {
                System.out.println("Player connected: " + conn.getAddress());
                
                /**
                 * Messaging
                 */
                ServerMessage connMessage = new ServerMessage();
                String message = "Player connected from: " + conn.getAddress();
                connMessage.setMessage(message);
                server.broadcast(connMessage);
                /**
                 * 
                 */
            }

            public void connectionRemoved(Server server, HostedConnection conn) {
            }
        };
        server.addConnectionListener(conListener);
        server.start();
        
    }
}

