/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.fps.client.object;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import chapter07.fps.common.message.PlayerMessage;
import chapter07.fps.common.message.PlayerUpdateMessage;
import chapter07.fps.common.object.NetworkedPlayerControl;

/**
 *
 * @author reden
 */
public class ClientPlayerControl extends NetworkedPlayerControl{

    private Node headNode = new Node("Head");
    private float tempYaw;
    /**
     * Visibility
     */
    private boolean visible;
    private boolean interpolate = false;

    /**
     * /Visibility
     */
    
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        headNode.setLocalTranslation(0, 1.4f, 0);
        if(spatial instanceof Node){
            ((Node) spatial).attachChild(headNode);
        }
        
    }

    
    @Override
    public void onMessageReceived(PlayerMessage message) {
        if(message instanceof PlayerUpdateMessage){
            PlayerUpdateMessage updateMessage = (PlayerUpdateMessage) message;
            if(updateMessage.isVisible()){
                tempRotation.set(updateMessage.getLookDirection());
                tempLocation.set(updateMessage.getPosition());
                tempYaw = updateMessage.getYaw();
                visible = true;
            } else {
                visible = false;
            }
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        
        /**
        * Visibility
        */
        if(!visible){
            return;
        }
        /**
        * /Visibility
        */
        if(!interpolate){
            spatial.setLocalTranslation(tempLocation);
            spatial.setLocalRotation(tempRotation);
            yaw = tempYaw;
            interpolate = true;
        } else {
            /**
            * Interpolation
            */

           float factor = tpf / 0.03f;
           spatial.setLocalTranslation(spatial.getLocalTranslation().interpolateLocal(tempLocation, factor));
           spatial.setLocalRotation(spatial.getLocalRotation().slerp(spatial.getLocalRotation(), tempRotation, factor));
           yaw = FastMath.interpolateLinear(factor, tempYaw, yaw);
           /**
            * /Interpolation
            */
        }
        headNode.setLocalRotation(new Quaternion().fromAngles(yaw, 0, 0));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public Node getHeadNode(){
        return headNode;
    }
    
    /**
     * /Visibility
     */
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if(visible){
            interpolate = false;
        }
    }
    /**
     * Visibility
     */
    

}
