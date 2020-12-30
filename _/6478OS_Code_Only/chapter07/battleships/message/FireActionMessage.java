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
public class FireActionMessage extends GameMessage{
    
    private int playerId;
    private int x;
    private int y;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int player) {
        this.playerId = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
}
