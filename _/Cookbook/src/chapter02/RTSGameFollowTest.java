/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import chapter02.state.RTSCameraAppState;
import chapter01.LoadScene;

    
    
/**
 *
 * @author Rickard
 */
public class RTSGameFollowTest extends LoadScene{

    public static void main(String[] args) {
        RTSGameFollowTest app = new RTSGameFollowTest();
        app.start();
    }
    
    private Spatial jaime;
    
    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        getFlyByCamera().setEnabled(false);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        RTSCameraAppState appState = new RTSCameraAppState();
        stateManager.attach(appState);

        jaime = ((Node) rootNode.getChild(0)).getChild("Models/Jaime/Jaime.j3o");
        jaime.setLocalTranslation(10, 0, 0);
        appState.setTargetLocation(jaime.getWorldTranslation());
        appState.setFollow(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        jaime.move(0.2f * tpf, 0, 0);
        
    }
}
