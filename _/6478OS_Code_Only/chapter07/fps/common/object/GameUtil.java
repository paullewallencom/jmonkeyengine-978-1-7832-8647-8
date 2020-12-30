/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.common.object;

import com.jme3.network.serializing.Serializer;
import chapter07.battleships.message.GameMessage;
import chapter07.fps.common.message.BulletUpdateMessage;
import chapter07.fps.common.message.LoadLevelMessage;
import chapter07.fps.common.message.PlayerActionMessage;
import chapter07.fps.common.message.PlayerJoinMessage;
import chapter07.fps.common.message.PlayerMessage;
import chapter07.fps.common.message.PlayerUpdateMessage;
import chapter07.fps.common.message.WelcomeMessage;
import chapter07.message.messages.PhysicsObjectMessage;

/**
 *
 * @author reden
 */
public class GameUtil {
    
    public static void initialize(){
        Serializer.registerClass(WelcomeMessage.class);
        Serializer.registerClass(PlayerJoinMessage.class);
        Serializer.registerClass(PlayerMessage.class);
        Serializer.registerClass(PlayerActionMessage.class);
        Serializer.registerClass(PlayerUpdateMessage.class);
        Serializer.registerClass(GameMessage.class);
        Serializer.registerClass(LoadLevelMessage.class);
        Serializer.registerClass(BulletUpdateMessage.class);
        Serializer.registerClass(PhysicsObjectMessage.class);
    }
    
}
