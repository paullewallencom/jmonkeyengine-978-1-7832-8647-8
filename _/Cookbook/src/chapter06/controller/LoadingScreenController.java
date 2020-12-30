/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.controller;

import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.render.ImageRenderer;

/**
 *
 * @author reden
 */
public class LoadingScreenController extends NiftyController{

    private String loadingText = "";
    private String loadingScreen = "";

    @Override
    public void onStartScreen() {
        super.onStartScreen();
        screen.findNiftyControl("caption", Label.class).setText(loadingText);
        screen.findElementByName("centralPanel").getRenderer(ImageRenderer.class).setImage(nifty.createImage(loadingScreen, true));
        screen.findElementByName("loadingPanel").setVisible(true);
    }
    
    
    public void setLoadingText(String text){
        loadingText = text;
    }
    
    public void setLoadingImage(String image){
        loadingScreen = image;
    }
}
