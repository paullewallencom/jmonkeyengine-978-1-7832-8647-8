/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.battleships.client;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import chapter07.battleships.common.Game;
import chapter07.battleships.common.Player;
import chapter07.battleships.message.FireActionMessage;
import chapter07.battleships.message.FiringResult;
import chapter07.battleships.message.GameMessage;
import chapter07.battleships.message.GameStatusMessage;
import chapter07.battleships.message.TurnMessage;
import chapter07.battleships.message.WelcomeMessage;

/**
 *
 * @author reden
 */
public class ClientMessageHandler implements MessageListener<Client>{

    private BattleshipsClient gameClient;
    private Game game;
    
    public ClientMessageHandler(BattleshipsClient client, Game game){
        this.gameClient = client;
        this.game = game;
    }
    
    public void messageReceived(Client source, Message m) {
        if(m instanceof WelcomeMessage){
            gameClient.getThisPlayer().setId(((WelcomeMessage)m).getMyPlayerId());
            System.out.println("Your id is " + gameClient.getThisPlayer().getId());
        } else if(m instanceof GameStatusMessage){
            int status = ((GameStatusMessage)m).getGameStatus();
            
            switch(status){
                case Game.GAME_WAITING:
                    if(game.getId() == 0 && ((GameStatusMessage)m).getGameId() > 0){
                        game.setId(((GameStatusMessage)m).getGameId());
                    }
                    if(game.getPlayerOne() == null && ((GameStatusMessage)m).getPlayerOneId() > 0){
                        int playerOneId = ((GameStatusMessage)m).getPlayerOneId();
                        if(gameClient.getThisPlayer().getId() == playerOneId){
                            game.setPlayerOne(gameClient.getThisPlayer());
                            gameClient.placeShips();
                        } else {
                            Player otherPlayer = new Player();
                            otherPlayer.setId(playerOneId);
                            game.setPlayerOne(otherPlayer);
                        }
                    }
                    if(game.getPlayerTwo() == null && ((GameStatusMessage)m).getPlayerTwoId() > 0){
                        int playerTwoId = ((GameStatusMessage)m).getPlayerTwoId();
                        if(gameClient.getThisPlayer().getId() == playerTwoId){
                            game.setPlayerTwo(gameClient.getThisPlayer());
                            gameClient.placeShips();
                        } else {
                            Player otherPlayer = new Player();
                            otherPlayer.setId(playerTwoId);
                            game.setPlayerTwo(otherPlayer);
                        }
                    }
                    game.setStatus(status);
                    break;
                case Game.GAME_ENDED:
                    
                    break;
            }
        } else if (m instanceof TurnMessage){
            int currentPlayerId = ((TurnMessage)m).getActivePlayer();
            game.setCurrentPlayerId(currentPlayerId);
            if(currentPlayerId == gameClient.getThisPlayer().getId()){
                gameClient.setMyTurn(true);
            } else {
                gameClient.setMyTurn(false);
            }
        } else if (m instanceof FiringResult){
            FiringResult result = (FiringResult) m;
            game.applyMove((FireActionMessage) m);
            String player;
            if(result.getPlayerId() == gameClient.getThisPlayer().getId()){
                player = "You";
            } else {
                player = "Player " + result.getPlayerId();
            }
            System.out.println(player + " fired at " + result.getX() + ", " + result.getY());
            if(result.isSunk()){
                System.out.println("The shot sunk a ship");
            } else if (result.isHit()){
                System.out.println("The shot damaged a ship");
            } else {
                System.out.println("The shot missed");
            }
        }
    }
    
}
