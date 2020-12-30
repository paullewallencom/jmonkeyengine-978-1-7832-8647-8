/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.common.object;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author reden
 */
public class Game {
    
    /**
     * Level loading
     */
    private String levelName;
    /**
     * /Level loading
     */
    private Map<Integer, NetworkedPlayerControl> players = new HashMap<Integer, NetworkedPlayerControl>();
    
    /**
     * Physics
     */
    private Map<Integer, Spatial> physicsObjects = new HashMap<Integer, Spatial>();
    /**
     * 
     * @return 
     */
    

    public Map<Integer, NetworkedPlayerControl> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, NetworkedPlayerControl> players) {
        this.players = players;
    }
    
    public void addPlayer(NetworkedPlayerControl player){
        players.put(player.getId(), player);
    }
    
    public void removePlayer(int playerId){
        players.remove(playerId);
    }
    
    public NetworkedPlayerControl getPlayer(int playerId){
        return players.get(playerId);
    }

    /**
     * Level loading
     */
    
    public String getLevelName() {
        return levelName;
    }
    
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    

    
    
    /**
     * /Physics 
     */

    /**
     *
     */
    /**
     * /Level loading
     */
    /**
     * Physics
     */
    public Map<Integer, Spatial> getPhysicsObjects() {
        return physicsObjects;
    }

    public void setPhysicsObjects(Map<Integer, Spatial> physicsObjects) {
        this.physicsObjects = physicsObjects;
    }
    /**
     * /Physics
     */

}
