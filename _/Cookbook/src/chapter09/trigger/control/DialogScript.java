/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger.control;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import chapter06.DialogNode;
import chapter06.controller.DialogScreenController;
import chapter06.state.NiftyAppState;
import chapter09.trigger.ScriptObject;

/**
 *
 * @author reden
 */
public class DialogScript implements ScriptObject{

    private NiftyAppState niftyState;
    private DialogScreenController dialogScreenController;
    private DialogNode dialog;
    private boolean triggered;
    
    public void trigger() {
        if(!triggered){
            triggered = true;
            onTrigger();
        }
    }

    public void onTrigger() {
        dialogScreenController.setDialogNode(dialog);
    }

    public void update(float tpf) {
    }

}
