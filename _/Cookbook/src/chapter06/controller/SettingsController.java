/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.controller;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import static chapter06.controller.NiftyController.app;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author reden
 */
public class SettingsController extends NiftyController{

    private Element forwardMapping;
    private Element backwardMapping;
    private Element leftMapping;
    private Element rightMapping;
    
    private Element selectedElement;
    
    private Map<Integer, String> mappings = new HashMap<Integer, String>();
    private InputManager inputManager;
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen); //To change body of generated methods, choose Tools | Templates.
        inputManager = app.getInputManager();
        inputManager.addRawInputListener(new KeyEventListener());
        
        bindElements();
    }
    
    private void bindElements(){
        
        mappings.put(KeyInput.KEY_W, "MoveForward");
        mappings.put(KeyInput.KEY_S, "MoveBackward");
        mappings.put(KeyInput.KEY_A, "StrafeLeft");
        mappings.put(KeyInput.KEY_D, "StrafeRight");
        
        forwardMapping = screen.findElementByName("forwardKey");
        forwardMapping.findNiftyControl("#command", Label.class).setText("MoveForward");
        forwardMapping.findNiftyControl("#key", Button.class).setText(Keyboard.getKeyName(KeyInput.KEY_W));
        backwardMapping = screen.findElementByName("backwardKey");
        backwardMapping.findNiftyControl("#command", Label.class).setText("MoveBackward");
        backwardMapping.findNiftyControl("#key", Button.class).setText(Keyboard.getKeyName(KeyInput.KEY_S));
        leftMapping = screen.findElementByName("leftKey");
        leftMapping.findNiftyControl("#command", Label.class).setText("StrafeLeft");
        leftMapping.findNiftyControl("#key", Button.class).setText(Keyboard.getKeyName(KeyInput.KEY_A));
        rightMapping = screen.findElementByName("rightKey");
        rightMapping.findNiftyControl("#command", Label.class).setText("StrafeRight");
        rightMapping.findNiftyControl("#key", Button.class).setText(Keyboard.getKeyName(KeyInput.KEY_D));
    }
    
    private void changeMapping(int keyCode){
        String mapping = mappings.get(keyCode);
        if(inputManager.hasMapping(mapping)){ // remove previous key assocaiation
            inputManager.deleteMapping(mapping);
        }
        for(String map: mappings.values()){
            if(map.equals(selectedElement.findNiftyControl("#command", Label.class).getText())){
                inputManager.deleteMapping(map);
            }
        }
        KeyTrigger trigger = new KeyTrigger(keyCode);
        inputManager.addMapping(mapping, trigger);
    }

    @NiftyEventSubscriber(pattern=".*Key#key")
    public void keyClicked(String id, ButtonClickedEvent event){
        selectedElement = event.getButton().getElement().getParent();
    }
    
    protected class KeyEventListener implements RawInputListener {

        public void onKeyEvent(KeyInputEvent evt) {
            
            if(evt.isPressed() && selectedElement != null){
                changeMapping(evt.getKeyCode());
                selectedElement.findNiftyControl("#key", Button.class).setText(Keyboard.getKeyName(evt.getKeyCode()));
                selectedElement = null;
            }
        }
        
        public void onJoyAxisEvent(JoyAxisEvent evt) {
        }

        public void onJoyButtonEvent(JoyButtonEvent evt) {
        }

        public void beginInput() {}
        public void endInput() {}
        public void onMouseMotionEvent(MouseMotionEvent evt) {
        }
        public void onMouseButtonEvent(MouseButtonEvent evt) {
        }
        
        public void onTouchEvent(TouchEvent evt) {}        
    }
}
