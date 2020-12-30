/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.controller;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author reden
 */
public class RTSScreenController extends NiftyController{
    
    private int messageIndex = 0;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen); //To change body of generated methods, choose Tools | Templates.
        
//        screen.findNiftyControl("buildingOptions", ListBox.class).
        createItems();
    }
    
    private void createItems(){
        
        screen.findNiftyControl("buildingOptions", ListBox.class).addItem("Big building, 500");
        screen.findNiftyControl("buildingOptions", ListBox.class).addItem("Smaller building, 300");
        screen.findNiftyControl("buildingOptions", ListBox.class).addItem("Shack, 100");
        screen.findNiftyControl("buildingOptions", ListBox.class).addItem("Small Shop, 400");
        screen.findNiftyControl("buildingOptions", ListBox.class).addItem("Factory, 800");
    }
    
}
