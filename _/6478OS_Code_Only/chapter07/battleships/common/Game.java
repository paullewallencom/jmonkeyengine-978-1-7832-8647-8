/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.battleships.common;

import chapter07.battleships.message.FireActionMessage;
import chapter07.battleships.message.GameStatusMessage;

/**
 *
 * @author reden
 */
public class Game {
    
    private int id;
    public final static int GAME_WAITING = 0;
    public final static int GAME_STARTED = 1;
    public final static int GAME_ENDED = 2;
    private int status = GAME_WAITING;
    private int turn = 1;
    private Player playerOne;
    private Player playerTwo;
    private int currentPlayerId;
    
    private Ship[][] boardOne = new Ship[10][10];
    private Ship[][] boardTwo = new Ship[10][10];
    
    public Ship applyMove(FireActionMessage action){
        int x = action.getX();
        int y = action.getY();
        Ship ship = null;
        if(action.getPlayerId() == playerOne.getId()){
            ship = boardTwo[x][y];
            if(ship != null){
                ship.hit();
                if(ship.isSunk()){
                    playerTwo.decreaseShips();

                }
            }
        } else {
            ship = boardOne[x][y];
            if(ship != null){
                ship.hit();
                if(ship.isSunk()){
                    playerOne.decreaseShips();

                }
            }
        }
        if(playerTwo.getShips() < 1 || playerOne.getShips() < 1){
            status = GAME_ENDED;
        }
        if(action.getPlayerId() == playerTwo.getId()){
            turn++;
        }
        return ship;
    }
    
    public int getTurn(){
        return turn;
    }
    
    public int getStatus(){
        return status;
    }
    
    public void setStatus(int status){
        this.status = status;
    }
    
    public Player getWinningPlayer(){
        if(playerOne.getShips() < 1){
            return playerTwo;
        } else if (playerTwo.getShips() < 1){
            return playerOne;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayer) {
        this.currentPlayerId = currentPlayer;
    }
    
    public void placeShip(int playerId, int shipId, int x, int y, boolean horizontal){
        Ship s = GameUtil.getShip(shipId);
        Ship[][] board;
        if(playerId == playerOne.getId()){
            board = boardOne;
            playerOne.increaseShips();
        } else {
            board = boardTwo;
            playerTwo.increaseShips();
        }
        
        for(int i = 0;i < s.getSegments(); i++){
            if(horizontal){
                if(x+i > 9 || y > 9){
                    return;
                }
                board[x+i][y] = s;
            } else {
                if(x > 9 || y + i > 9){
                    return;
                }
                board[x][y+i] = s;
            }
        }
        
    }
}
