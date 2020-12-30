/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter04.state;

import chapter04.control.ExpressionsControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Spatial;

/**
 *
 * @author Rickard
 */
public class ExpressionsAppState extends AbstractAppState implements ActionListener{
    
    private Application app;
    private InputManager inputManager;
    
    private ExpressionsControl lc;
    
    private ExpressionsControl.PhonemeMouth phoneme;
    
    private ExpressionsControl.ExpressionEyes eyeBrowse;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        Spatial jamie = ((SimpleApplication)app).getRootNode().getChild("Jaime");
        
        lc = new ExpressionsControl();//jamie.getControl(LipSyncControl.class);
        jamie.addControl(lc);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    public void onAction(String name, boolean isPressed, float tpf) {
        
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public ExpressionsControl.PhonemeMouth getPhoneme() {
        return phoneme;
    }

    public void setPhoneme(ExpressionsControl.PhonemeMouth phoneme) {
        this.phoneme = phoneme;
        lc.setPhoneme(phoneme);
    }

    public ExpressionsControl.ExpressionEyes getEyeBrowse() {
        return eyeBrowse;
    }

    public void setEyeBrowse(ExpressionsControl.ExpressionEyes eyeBrowse) {
        this.eyeBrowse = eyeBrowse;
        lc.setExpression(eyeBrowse);
    }

}
