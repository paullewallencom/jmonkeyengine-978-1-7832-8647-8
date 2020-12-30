/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.message.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author reden
 */
@Serializable()
public class ServerMessage extends AbstractMessage{
    
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
