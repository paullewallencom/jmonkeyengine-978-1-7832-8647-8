/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Rickard
 */
public class SelectableControl extends AbstractControl {

    private boolean selected;
    private Spatial marker;

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (marker != null) {
            if (this.selected) {
                ((Node) spatial).attachChild(marker);
            } else {
                ((Node) spatial).detachChild(marker);
            }
        }
    }

    public void setMarker(Spatial marker) {
        this.marker = marker;
    }
    
    public boolean isSelected(){
        return selected;
    }
}
