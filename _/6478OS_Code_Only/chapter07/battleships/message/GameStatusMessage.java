/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.battleships.message;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author reden
 */
@Serializable()
public class GameStatusMessage extends GameMessage{
    
    private int gameStatus;
    private int playerOneId;
    private int playerTwoId;
    
    private int winningPlayer = -1;

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(int winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public int getPlayerOneId() {
        return playerOneId;
    }

    public void setPlayerOneId(int playerOneId) {
        this.playerOneId = playerOneId;
    }

    public int getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(int playerTwoId) {
        this.playerTwoId = playerTwoId;
    }
    
}
