/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.common.message;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author reden
 */
@Serializable()
public class PlayerJoinMessage extends GameMessage{
    
    private int playerId;
    private boolean leaving;

    public PlayerJoinMessage(){
        setReliable(true);
    }
    
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isLeaving() {
        return leaving;
    }

    public void setLeaving(boolean leave) {
        this.leaving = leave;
    }
    
    
    
}
