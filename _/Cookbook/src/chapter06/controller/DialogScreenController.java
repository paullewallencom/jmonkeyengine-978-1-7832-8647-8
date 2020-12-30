/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.controller;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import chapter06.DialogNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reden
 */
public class DialogScreenController extends NiftyController{
    
    ListBox dialogOptions;
    private DialogNode dialogNode;

    @Override
    public void onStartScreen() {
        super.onStartScreen();
        
        dialogOptions = screen.findNiftyControl("dialogOptions", ListBox.class);
        
        if(dialogNode != null){
            onDialogNodeChanged();
        }
    }
    
    @NiftyEventSubscriber(id="dialogOptions")
    public void onDialogOptionSelected(final String id, final ListBoxSelectionChangedEvent event){
        if(!event.getSelectionIndices().isEmpty()){
            System.out.println("Option " + event.getSelectionIndices().get(0) + " clicked ");
        }
    }
    
    public void onDialogNodeChanged(){
        screen.findNiftyControl("characterName", Label.class).setText(dialogNode.getCharacterName());
        screen.findNiftyControl("characterDialogText", Label.class).setText(dialogNode.getCharacterDialog());
        screen.findElementByName("characterImage").getRenderer(ImageRenderer.class).setImage(nifty.createImage(dialogNode.getCharacterImage(), true));
        
        
        dialogOptions.clear();
        
        for(String option: dialogNode.getDialogOptions()){
            dialogOptions.addItem(option);
        }
        dialogOptions.refresh();
        screen.layoutLayers();
        screen.findElementByName("dialogPanel").setVisible(true);
    }
    
    public void setDialogNode(DialogNode node){
        this.dialogNode = node;
    }
}
