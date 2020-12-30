/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import chapter02.control.SelectableControl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rickard
 */
public class SelectAppState extends AbstractAppState implements ActionListener {

    private static String LEFT_CLICK = "Left Click";
    private SimpleApplication app;
    private InputManager inputManager;
    private List<Spatial> selectables = new ArrayList<Spatial>();
    private Spatial currentSelection;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.inputManager = app.getInputManager();

        inputManager.addMapping(LEFT_CLICK,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, LEFT_CLICK);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        inputManager.deleteMapping(LEFT_CLICK);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(LEFT_CLICK) && isPressed) {
            onClick();
        }
    }

    private void onClick() {
        Vector2f mousePos2D = inputManager.getCursorPosition();
        Vector3f mousePos3D = app.getCamera().getWorldCoordinates(mousePos2D, 0f);
        Vector3f clickDir = mousePos3D.add(app.getCamera().getWorldCoordinates(mousePos2D, 1f)).normalizeLocal();

        Ray ray = new Ray(mousePos3D, clickDir);

        CollisionResults results = new CollisionResults();
        for (Spatial spatial : selectables) {
            spatial.collideWith(ray, results);
        }

        if (results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();

            currentSelection = closest.getGeometry();
            for (Spatial spatial : selectables) {
                if (spatial.getControl(SelectableControl.class) != null) {
                    if (spatial == currentSelection || (spatial instanceof Node && ((Node) spatial).hasChild(currentSelection))) {
                        spatial.getControl(SelectableControl.class).setSelected(true);
                    } else {
                        spatial.getControl(SelectableControl.class).setSelected(false);
                    }
                }
            }
        }

    }

    public void addSelectable(Spatial s) {
        selectables.add(s);
    }

    public void removeSelectable(Spatial s) {
        selectables.remove(s);
    }
    
    public Spatial getCurrentSelection(){
        return currentSelection;
    }
}
