/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import chapter06.controller.NiftyController;

/**
 *
 * @author reden
 */
public class NiftyAppState extends AbstractAppState  implements ActionListener{

    private Nifty nifty;
    
    public static final String TOGGLE_OPTIONS = "TOGGLE_OPTIONS";
    public static final String TOGGLE_CONSOLE = "TOGGLE_CONSOLE";
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
                app.getInputManager(),
                app.getAudioRenderer(),
                app.getRenderManager().getPostView("Gui Default"));
        nifty = niftyDisplay.getNifty();
        
        app.getRenderManager().getPostView("Gui Default").addProcessor(niftyDisplay);
        nifty.fromXml("Interface/Screens/mainScreen.xml", "main");
        
        /**
         * Loading screen
         */
        nifty.addXml("Interface/Screens/loadingScreen.xml");
        /**
         * /Loading screen
         */
        
        /**
         * Dialog screen
         */
        nifty.addXml("Interface/Screens/dialogScreen.xml");
        /**
         * /Dialog screen
         */
        
        /**
         * Inventory screen
         */
        nifty.addXml("Interface/Screens/inventoryScreen.xml");
        /**
         * /Inventory screen
         */
        
        /**
         * Settings screen
         */
        nifty.addXml("Interface/Screens/settingsScreen.xml");
        /**
         * /Settings screen
         */
        
        /**
         * Game screen
         */
        nifty.addXml("Interface/Screens/gameScreen.xml");
        /**
         * /Game screen
         */
        
        /**
         * RTSScreen
         */
        nifty.addXml("Interface/Screens/rtsScreen.xml");
        /**
         * 
         */
        
        NiftyController.setApplication(app);
        
        app.getInputManager().setCursorVisible(true);
        app.getInputManager().deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        app.getInputManager().addMapping(TOGGLE_OPTIONS, new KeyTrigger(KeyInput.KEY_ESCAPE));
        app.getInputManager().addListener(this, TOGGLE_OPTIONS);
        
        /**
         * Console
         */
        app.getInputManager().addMapping(TOGGLE_CONSOLE, new KeyTrigger(KeyInput.KEY_F12));
        app.getInputManager().addListener(this, TOGGLE_CONSOLE);
        /**
         * /Console
         */
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals(TOGGLE_OPTIONS) && isPressed){
            ((NiftyController)nifty.getCurrentScreen().getScreenController()).toggleOptionsMenu();
        } else if(name.equals(TOGGLE_CONSOLE) && isPressed){
            ((NiftyController)nifty.getScreen("main").getScreenController()).toggleConsole();
        }
    }
    
    public Nifty getNifty(){
        return nifty;
    }
    
    public void gotoScreen(String screenName){
        nifty.gotoScreen(screenName);
    }
}
