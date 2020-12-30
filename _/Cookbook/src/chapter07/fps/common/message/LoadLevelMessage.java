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
public class LoadLevelMessage extends GameMessage{
   
    private String levelName;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

}
