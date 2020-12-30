package chapter07.battleships.server;

import com.jme3.math.FastMath;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filter;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import chapter07.battleships.common.Game;
import chapter07.battleships.common.GameUtil;
import chapter07.battleships.common.Player;
import chapter07.battleships.message.FireActionMessage;
import chapter07.battleships.message.GameMessage;
import chapter07.battleships.message.GameStatusMessage;
import chapter07.battleships.message.PlaceShipMessage;
import chapter07.battleships.message.PlayerNameMessage;
import chapter07.battleships.message.TurnMessage;
import chapter07.battleships.message.WelcomeMessage;

public class BattleshipServer
{
    public static void main(String[] args ) throws Exception
    {
        GameUtil.init();
        BattleshipServer gameServer = new BattleshipServer();
        
        String WAIT = "wait";
        synchronized( WAIT ) {
            WAIT.wait();
        }          
    }
    
    private Server server;
    private int nextGameId = 1;
    private int nextPlayerId = 1;
    
    private HashMap<Integer, Game> games = new HashMap<Integer, Game>();
    private HashMap<HostedConnection, Player> playerMap = new HashMap<HostedConnection, Player>();
    private HashMap<Integer, List<HostedConnection>> connectionFilters = new HashMap<Integer, List<HostedConnection>>();

    public BattleshipServer() throws IOException{
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream("network/resources/network.properties"));

        server = Network.createServer( prop.getProperty("server.name"), Integer.parseInt(prop.getProperty("server.version")), Integer.parseInt(prop.getProperty("server.port")), 
                Integer.parseInt(prop.getProperty("server.port")));
        

        ConnectionListener conListener = new ConnectionListener() {

            public void connectionAdded(Server server, HostedConnection conn) {
                Game game = null;
                if(games.isEmpty() || games.get(nextGameId - 1).getPlayerTwo() != null){
                    game = createGame();
                } else {
                    game = games.get(nextGameId - 1);
                }
                addPlayer(game, conn);
                System.out.println("Player assigned id " + (nextPlayerId - 1) + " and joined game " + game.getId() + " as player " + (game.getPlayerTwo() == null ? 1 : 2));
            }

            public void connectionRemoved(Server server, HostedConnection conn) {
//                ServerMessage connMessage = new ServerMessage();
//                String message = "Player disconnected: " + conn.getAddress();
//                connMessage.setMessage(message);
//                playerMap.remove(conn);
//                server.broadcast(connMessage);
//                System.out.println(message);
            }
        };
        server.addConnectionListener(conListener);
        
        ServerMessageHandler messageHandler = new ServerMessageHandler(this);
        server.addMessageListener( messageHandler, PlayerNameMessage.class, FireActionMessage.class, PlaceShipMessage.class );
        
        server.start();
    }
   
    private Game createGame(){
        Game game = new Game();
        game.setId(nextGameId++);
        games.put(game.getId(), game);
        List<HostedConnection> connsForGame = new ArrayList<HostedConnection>();
        connectionFilters.put(game.getId(), connsForGame);
        return game;
    }
    
    private void addPlayer(Game game, HostedConnection conn){
        Player player = new Player();
        player.setId(nextPlayerId++);

        playerMap.put(conn, player);

        WelcomeMessage welcomeMessage = new WelcomeMessage();
        welcomeMessage.setMyPlayerId(player.getId());
        server.broadcast(Filters.in(conn), welcomeMessage);
                
        if(game.getPlayerOne() == null){
            game.setPlayerOne(player);
        } else {
            game.setPlayerTwo(player);
        }
        List<HostedConnection> connsForGame = connectionFilters.get(game.getId());
        connsForGame.add(conn);
        
        GameStatusMessage waitMessage = new GameStatusMessage();
        waitMessage.setGameId(game.getId());
        waitMessage.setGameStatus(Game.GAME_WAITING);
        waitMessage.setPlayerOneId(game.getPlayerOne() != null ? game.getPlayerOne().getId() : 0);
        waitMessage.setPlayerTwoId(game.getPlayerTwo() != null ? game.getPlayerTwo().getId() : 0);
        server.broadcast(Filters.in(connsForGame), waitMessage);
    }
    
    public Game startGame(int gameId){
        Game game = games.get(gameId);
        game.setStatus(Game.GAME_STARTED);
        System.out.println("Starting game " + gameId);
        List<HostedConnection> connsForGame = connectionFilters.get(gameId);
        GameStatusMessage startMessage = new GameStatusMessage();
        startMessage.setGameId(game.getId());
        startMessage.setGameStatus(Game.GAME_STARTED);
        startMessage.setPlayerOneId(game.getPlayerOne().getId());
        startMessage.setPlayerTwoId(game.getPlayerTwo().getId());
        server.broadcast(Filters.in(connsForGame), startMessage);

        TurnMessage turnMessage = new TurnMessage();
        turnMessage.setGameId(game.getId());
        int startingPlayer = FastMath.nextRandomInt(1, 2);
        if(startingPlayer == 1){
            turnMessage.setActivePlayer(game.getPlayerOne().getId());
            game.setCurrentPlayerId(game.getPlayerOne().getId());
        } else {
            turnMessage.setActivePlayer(game.getPlayerTwo().getId());
            game.setCurrentPlayerId(game.getPlayerTwo().getId());
        }
        
        server.broadcast(Filters.in(connsForGame), turnMessage);
        return game;
    }
 
    public HashMap<HostedConnection, Player> getPlayers(){
        return playerMap;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
        server.close();
    }
    
    public Game getGame(int id){
        return games.get(id);
    }
    
    public void sendMessage(GameMessage message ){
        List<HostedConnection> connsForGame = connectionFilters.get(message.getGameId());
        server.broadcast(Filters.in(connsForGame), message);
    }
    
    public void removeGame(int gameId){
        
    }
}

