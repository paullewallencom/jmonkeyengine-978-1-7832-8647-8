/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.battleships.server;

import com.jme3.math.FastMath;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import chapter07.battleships.common.Game;
import chapter07.battleships.common.Player;
import chapter07.battleships.common.Ship;
import chapter07.battleships.message.FireActionMessage;
import chapter07.battleships.message.FiringResult;
import chapter07.battleships.message.PlaceShipMessage;
import chapter07.battleships.message.PlayerNameMessage;
import chapter07.battleships.message.TurnMessage;

/**
 *
 * @author reden
 */
public class ServerMessageHandler implements MessageListener<HostedConnection>{

    private BattleshipServer gameServer;
    
    public ServerMessageHandler(BattleshipServer server){
        this.gameServer = server;
    }
    
    public void messageReceived(HostedConnection conn, Message m) {
        if (m instanceof PlaceShipMessage){
            PlaceShipMessage shipMessage = (PlaceShipMessage) m;
            int gameId = shipMessage.getGameId();
            Game game = gameServer.getGame(gameId);
            game.placeShip(shipMessage.getPlayerId(), shipMessage.getShip(), shipMessage.getX(), shipMessage.getY(), shipMessage.isHorizontal());
            System.out.println("Adding ship at " + shipMessage.getX() + ", " + shipMessage.getY());
            if(game.getPlayerOne().getShips() == 5 && game.getPlayerTwo() != null&& game.getPlayerTwo().getShips() == 5){
                gameServer.startGame(gameId);
            }
        } else if(m instanceof FireActionMessage){
            FireActionMessage fireAction = (FireActionMessage) m;
            int gameId = fireAction.getGameId();
            Game game = gameServer.getGame(gameId);
            if(game.getCurrentPlayerId() == fireAction.getPlayerId()){
                Ship hitShip = game.applyMove(fireAction);
                FiringResult result = new FiringResult();
                result.setGameId(game.getId());
                result.setX(fireAction.getX());
                result.setY(fireAction.getY());
                result.setPlayerId(fireAction.getPlayerId());
                
                if(hitShip != null){
                    result.setHit(true);
                    if(hitShip.isSunk()){
                        result.setSunk(true);
                    }
                }
                gameServer.sendMessage(result);
                
                TurnMessage turnMessage = new TurnMessage();
                turnMessage.setGameId(game.getId());
                game.setCurrentPlayerId(game.getCurrentPlayerId() == 1 ? 2 : 1);
                System.out.println("Current player is " + game.getCurrentPlayerId());
                turnMessage.setActivePlayer(game.getCurrentPlayerId());
                gameServer.sendMessage(turnMessage);
                
            } else {
                // wrong player
                
            }
        } else 
        
        if(m instanceof PlayerNameMessage){
            Player p = gameServer.getPlayers().get(conn);
            if(p != null){
                p.setName(((PlayerNameMessage)m).getName());
                
            }
            System.out.println(((PlayerNameMessage)m).getName());
        }
    }
    
}
