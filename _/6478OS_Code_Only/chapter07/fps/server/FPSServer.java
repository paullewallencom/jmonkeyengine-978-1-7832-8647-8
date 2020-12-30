package chapter07.fps.server;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetKey;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import chapter07.fps.common.message.LoadLevelMessage;
import chapter07.fps.common.message.BulletUpdateMessage;
import chapter07.fps.common.message.PlayerJoinMessage;
import chapter07.fps.common.message.PlayerUpdateMessage;
import chapter07.fps.common.message.WelcomeMessage;
import chapter07.fps.common.object.NetworkedPlayerControl;
import chapter07.fps.common.object.Game;
import chapter07.fps.common.object.GameUtil;
import chapter07.fps.server.object.ServerBullet;
import chapter07.fps.server.object.ServerPlayerControl;
import chapter08.network.ServerPhysicsAppState;

public class FPSServer extends SimpleApplication
{
    public static void main(String[] args ) throws Exception
    {
        GameUtil.initialize();
        FPSServer gameServer = new FPSServer();
        
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(30);
        gameServer.setSettings(settings);
        
        gameServer.start(JmeContext.Type.Headless);
    }
    
    private Server server;
    private int nextPlayerId = 1;
    private int nextObjectId = 1;
    private Game game;
    private HashMap<HostedConnection, ServerPlayerControl> playerMap = new HashMap<HostedConnection, ServerPlayerControl>();
    
    /**
     * Level loading
     */
    private Node levelNode;
    /**
     * /Level loading
     */
    
    /**
     * Firing
     */
    private List<ServerBullet> bullets = new ArrayList<ServerBullet>();
    /**
     * /Firing
     */
    
    public FPSServer() throws IOException{
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream("chapter07/resources/network.properties"));

        game = new Game();
        
        server = Network.createServer( prop.getProperty("server.name"), Integer.parseInt(prop.getProperty("server.version")), Integer.parseInt(prop.getProperty("server.port")), 
                Integer.parseInt(prop.getProperty("server.port")));
        

        ConnectionListener conListener = new ConnectionListener() {

            public void connectionAdded(Server server, HostedConnection conn) {
                
                addPlayer(game, conn);
                System.out.println("Player assigned id " + (nextPlayerId - 1));
            }

            public void connectionRemoved(Server server, HostedConnection conn) {
                removePlayer(conn);
            }
        };
        server.addConnectionListener(conListener);
        ServerMessageHandler messageHandler = new ServerMessageHandler(this, game);
        server.addMessageListener(messageHandler);
        server.start();
    }
   
    private void addPlayer(Game game, HostedConnection conn){
        ServerPlayerControl player = new ServerPlayerControl();
        player.setId(nextPlayerId++);
        player.setLevel(levelNode);

        
        
        Node playerNode = new Node("");
        playerNode.addControl(new BetterCharacterControl(0.5f, 1.5f, 1f));
        playerNode.addControl(player);
        rootNode.attachChild(playerNode);
        
        playerMap.put(conn, player);
        game.addPlayer(player);
        

        WelcomeMessage welcomeMessage = new WelcomeMessage();
        welcomeMessage.setMyPlayerId(player.getId());
        server.broadcast(Filters.in(conn), welcomeMessage);
        
        /**
        * Level loading
        */
        LoadLevelMessage levelMessage = new LoadLevelMessage();
        levelMessage.setLevelName(game.getLevelName());
        server.broadcast(Filters.in(conn), levelMessage);
        
        /**
        * /Level loading
        */
        
        
        /**
         * Physics
         */
        stateManager.getState(ServerPhysicsAppState.class).addPlayer(player.getPhysicsCharacter());
        /**
         * /Physics
         */
        Collection<NetworkedPlayerControl> players = game.getPlayers().values();
        for(NetworkedPlayerControl p: players){
            PlayerJoinMessage joinMessage = new PlayerJoinMessage();
            joinMessage.setPlayerId(p.getId());
            server.broadcast(Filters.in(conn), joinMessage);
        }

        PlayerJoinMessage joinMessage = new PlayerJoinMessage();
        joinMessage.setPlayerId(player.getId());
        server.broadcast(joinMessage);
    }
    
    private void removePlayer(HostedConnection conn){
        ServerPlayerControl player = playerMap.remove(conn);
        rootNode.detachChild(player.getSpatial());
        PlayerJoinMessage joinMessage = new PlayerJoinMessage();
        joinMessage.setPlayerId(player.getId());
        joinMessage.setLeaving(true);
        
        /**
         * Physics
         */
        stateManager.getState(ServerPhysicsAppState.class).removePlayer(player.getPhysicsCharacter());
        /**
         * /Physics
         */
        
        server.broadcast(joinMessage);
        game.removePlayer(player.getId());
        
    }
    
    public HashMap<HostedConnection, ServerPlayerControl> getPlayers(){
        return playerMap;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
        server.close();
    }
    
    public void sendMessage(Message message ){
        server.broadcast(message);
    }
    
    public void removeGame(int gameId){
        
    }

    @Override
    public void simpleInitApp() {
    /**
     * Level loading
     */
        levelNode = loadLevel("TestScene");
        rootNode.attachChild(levelNode);
        game.setLevelName("TestScene");
    /**
     * /Level loading
     */
        
        /**
         * Physics
         */
        ServerPhysicsAppState physicsState = new ServerPhysicsAppState();
        physicsState.setGame(game);
        physicsState.setServer(this);
        stateManager.attach(physicsState);
        /**
         * /Physics
         */
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        
        Collection<NetworkedPlayerControl> players = game.getPlayers().values();
        for(NetworkedPlayerControl p: players){
            p.update(tpf);
            PlayerUpdateMessage updateMessage = new PlayerUpdateMessage();
            updateMessage.setPlayerId(p.getId());
            updateMessage.setLookDirection(p.getSpatial().getLocalRotation());
            updateMessage.setPosition(p.getSpatial().getLocalTranslation());
            updateMessage.setYaw(p.getYaw());
            server.broadcast(updateMessage);
        }
        
        
        
        /**
        * Visibility
        */
        
//        Collection<NetworkedPlayerControl> players = game.getPlayers().values();
//        for(NetworkedPlayerControl p: players){
//            p.update(tpf);
//        }
//        Iterator<HostedConnection> it = playerMap.keySet().iterator();
//        while(it.hasNext()){
//            HostedConnection conn = it.next();
//            ServerPlayerControl player = playerMap.get(conn);
//            for(NetworkedPlayerControl otherPlayer: players){
//                float distance = player.getSpatial().getWorldTranslation().distance(otherPlayer.getSpatial().getWorldTranslation());
//                PlayerUpdateMessage updateMessage = null;
//                if(distance < 50){
//                    updateMessage = createUpdateMessage(otherPlayer);
//                    player.addVisiblePlayer(otherPlayer.getId());
//                } else if (player.removeVisiblePlayer(otherPlayer.getId())){
//                    updateMessage = createUpdateMessage(otherPlayer);
//                    updateMessage.setVisible(false);
//                }
//                if(updateMessage != null){
//                    server.broadcast(Filters.in(conn), updateMessage);
//                }
//            }
//        }
        
        /**
        * /Visibility
        */
        
        /**
         * Firing
         */
        int nrOfBullets = bullets.size();
        for(int i = 0; i < nrOfBullets; i++){
            ServerBullet bullet = bullets.get(i);
            bullet.update(tpf);
            BulletUpdateMessage update = new BulletUpdateMessage();
            update.setId(bullet.getId());
            update.setPosition(bullet.getWorldPosition());
            update.setAlive(bullet.isAlive());
            server.broadcast(update);
            if(!bullet.isAlive()){
                bullets.remove(bullet);
                nrOfBullets--;
                i--;
            }
        }
        
        /**
         * /Firing
         */
        
    }
    
    private PlayerUpdateMessage createUpdateMessage(NetworkedPlayerControl p){
        PlayerUpdateMessage updateMessage = new PlayerUpdateMessage();
        updateMessage.setPlayerId(p.getId());
        updateMessage.setLookDirection(p.getSpatial().getLocalRotation());
        updateMessage.setPosition(p.getSpatial().getLocalTranslation());
        updateMessage.setYaw(p.getYaw());
        updateMessage.setVisible(true);
        return updateMessage;
    }
  
    
    /**
     * Level loading
     */
    
    private Node loadLevel(String levelName){
        return (Node) assetManager.loadModel("Scenes/"+levelName + ".j3o");
    }
    
    public Node getLevelNode(){
        return levelNode;
    }
    /**
     * /Level loading
     */
    
    /**
     * Firing
     */
    public void onFire(NetworkedPlayerControl player){
        Vector3f direction = player.getSpatial().getWorldRotation().getRotationColumn(2);
        direction.setY(-player.getYaw());
        ServerBullet bullet = new ServerBullet(player.getSpatial().getWorldTranslation().add(0, 1, 0), direction);
        bullet.setId(nextObjectId++);
        bullets.add(bullet);
    }
    /**
     * Firing
     */
}

