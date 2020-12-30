/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.command;

import de.lessvoid.nifty.controls.ConsoleCommands.ConsoleCommand;
import chapter06.controller.NiftyController;

/**
 *
 * @author reden
 */
public class HideCommand implements ConsoleCommand{

    private NiftyController controller;
    
    public void execute(String[] strings) {
        controller.toggleConsole();
    }
    
    public void setController(NiftyController controller){
        this.controller = controller;
    }
}
