/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.controller;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.console.ConsoleControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

/**
 *
 * @author reden
 */
public class ConsoleController extends NiftyController{
    
    private Console console;
    
    @NiftyEventSubscriber(id="console")
    public void onConsoleCommand(final String id, final ConsoleExecuteCommandEvent command) {
        // SEND CHAT MESSAGE
        String message = command.getCommandLine();
        if(!message.isEmpty()){
//            if(client.getGame().getGameType() == GameType.Multi) DatabaseHandler.sendChatMessage(message, client.getTheatre().getId());
        }
        nifty.getScreen("mainui").findElementByName("console").findElementByName("console#textInput").disableFocus();
    }
    
    public void toggleConsole(){
        Element e = nifty.getScreen("mainui").findElementByName("console#textInput");
        if(e.isVisible()){
            e.hide();
            e.disableFocus();
        } else {
            e.show();
            e.setFocus();
        }
    }
    
    public void outputToConsole(String message, Color color){
        if(console == null) console = nifty.getScreen("mainui").findNiftyControl("console", Console.class);
        if(console != null){
            if(message == null) System.out.println("message == nll!");
            else if(color == null) System.out.println("color == nll!");
            console.output(message, color);
        }
    }
}
