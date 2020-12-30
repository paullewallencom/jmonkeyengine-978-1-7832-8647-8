/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.controller;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleCommands;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import chapter06.command.HideCommand;

/**
 *
 * @author reden
 */
public abstract class NiftyController implements ScreenController{

    protected Nifty nifty;
    protected Screen screen;
    protected static Application app;
    private boolean optionsMenuVisible;
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
        /**
         * Console
         */
        initConsole();
        /**
         * /Console
         */
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
    
    public static void setApplication(Application app){
        NiftyController.app = app;
    }
    
    public void toggleOptionsMenu(){
        if(optionsMenuVisible){
            nifty.getCurrentScreen().findElementByName("options").hide();
            nifty.getCurrentScreen().findElementByName("layer0").setFocus();
            optionsMenuVisible = false;
        } else {
            nifty.getCurrentScreen().findElementByName("options").show();
            nifty.getCurrentScreen().findElementByName("optionsLayer").setFocus();
            optionsMenuVisible = true;
        }
    }
    
    public void startGame(){
        
    }
    
    public void showSettings(){
        
    }
    
    public void quit(){
        app.stop();
    }
    
    /**
     * Console
     */
    private Console console;
    
    private void initConsole(){
        console = getConsole();
        ConsoleCommands consoleCommands = new ConsoleCommands(nifty, console);
        HideCommand c = new HideCommand();
        c.setController(this);
        consoleCommands.registerCommand("/hide", c);
        consoleCommands.registerCommand("/h", c);
        consoleCommands.enableCommandCompletion(true);
    }
    
    @NiftyEventSubscriber(id="console")
    public void onConsoleCommand(final String id, final ConsoleExecuteCommandEvent command) {
        String message = command.getCommandLine();
        if(!message.isEmpty()){
            if(!message.startsWith("/")){
                // send chat message
            }
        }
    }
    
    public void toggleConsole(){
        Console console = getConsole();
        if(console.getElement().isVisible()){
            console.getElement().hide();
        } else {
            console.getElement().show();
        }
    }
    
    public void outputToConsole(String message){
        console.output(message);
        
    }
    
    private Console getConsole(){
        if(console == null){
            console = nifty.getScreen("main").findNiftyControl("console", Console.class);
        }
        return console;
    }
   
    /**
     * Console
     */
}
