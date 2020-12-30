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
public class TurnMessage extends GameMessage{
    
    private int activePlayer;

    public int getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }
    
    
}
