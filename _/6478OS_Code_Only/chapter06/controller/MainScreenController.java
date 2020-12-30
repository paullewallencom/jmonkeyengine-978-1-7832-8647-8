/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.controller;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.window.builder.WindowBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.ElementInteractionClickHandler;

/**
 *
 * @author reden
 */
public class MainScreenController extends NiftyController{
    
    private int messageIndex = 0;
    private int windowIndex = 0;
    
    public void addMessage(String text){
        ControlBuilder messageBuilder;
        messageBuilder = new ControlBuilder("gameMessage") {{
            id("message"+messageIndex);
            interactOnClick("removeMessage("+messageIndex+")");
        }};
        
        Element e = messageBuilder.build(nifty, screen, screen.findElementByName("mq"));
        e.findNiftyControl("#title", Label.class).setText("Message " + messageIndex);
        e.findNiftyControl("#content", Label.class).setText(text);
        e.show();
        messageIndex++;
    }
    
    public void removeMessage(final String id){
        screen.findElementByName("message"+id).hide(new EndNotify() {

            public void perform() {
                screen.findElementByName("message"+id).markForRemoval();
//                screen.findElementByName("mq").layoutElements();
            }
        });
        
    }
    
    public void showMessageWindow(final String text){
        ControlBuilder windowBuilder = new ControlBuilder("messageWindow"){{
            id("window"+windowIndex);
            set("title", "Window"+windowIndex);
        }};
        Element e = windowBuilder.build(nifty, screen, screen.findElementByName("layer0"));
        e.findNiftyControl("#content", Label.class).setText(text);
        windowIndex++;
    }
    
//    public ElementInteractionClickHandler clickHandler = new ElementInteractionClickHandler(nifty, null, null)
}
