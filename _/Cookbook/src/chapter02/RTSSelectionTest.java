/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02;

import com.jme3.app.FlyCamAppState;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import chapter02.control.SelectableControl;
import chapter02.state.RTSCameraAppState;
import chapter02.state.SelectAppState;
import chapter01.LoadScene;

/**
 *
 * @author Rickard
 */
public class RTSSelectionTest extends LoadScene {

    public static void main(String[] args) {
        RTSSelectionTest app = new RTSSelectionTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        getFlyByCamera().setEnabled(false);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        RTSCameraAppState appState = new RTSCameraAppState();
        stateManager.attach(appState);

        SelectAppState selectAppState = new SelectAppState();
        stateManager.attach(selectAppState);

        Spatial jaime = ((Node) rootNode.getChild(0)).getChild("Models/Jaime/Jaime.j3o");
        selectAppState.addSelectable(jaime);

        Geometry marker = new Geometry("Marker", new Quad(1f, 1f));
        marker.rotate(-FastMath.HALF_PI, 0, 0);
        marker.move(-0.5f, 0.05f, 0.5f);

        Material selectionMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        marker.setMaterial(selectionMaterial);

        SelectableControl selectableControl = new SelectableControl();
        selectableControl.setMarker(marker);
        jaime.addControl(selectableControl);

    }
}
