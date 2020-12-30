/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06.controller;

import com.jme3.math.FastMath;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.controls.dragndrop.builder.DroppableBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.screen.Screen;
import chapter06.InventoryItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author reden
 */
public class InventoryScreenController extends NiftyController implements DroppableDropFilter{

    private Map<String, InventoryItem> itemMap = new HashMap<String, InventoryItem>();
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        
        screen.findNiftyControl("HandLeft", Droppable.class).addFilter(this);
        screen.findNiftyControl("HandRight", Droppable.class).addFilter(this);
        screen.findNiftyControl("Head", Droppable.class).addFilter(this);
        screen.findNiftyControl("Foot", Droppable.class).addFilter(this);
        
        createSlots();
        
        createItems();
        
        screen.layoutLayers();
        
        
    }

    
    
    public boolean accept(Droppable drpbl, Draggable drgbl, Droppable drpbl1) {
        // check type of droppable and draggable
        System.out.println(drpbl + ", " + drgbl.getId() + ", " + drpbl1.getId());
        InventoryItem item = itemMap.get(drgbl.getId());
        if(drpbl1.getId().startsWith(item.getType().name()) || drpbl1.getId().startsWith("itemSlot")){
            return true;
        }
        return false;
    }
    
    
    private void createSlots(){
        ControlBuilder slotBuilder;
        PanelBuilder panelBuilder;
        for(int y = 0; y < 5; y++){
            final int posY = y;
            panelBuilder = new PanelBuilder("") {{
                id("inventoryColumn"+posY);
                childLayoutVertical();
            }};
            panelBuilder.build(nifty, screen, screen.findElementByName("inventorySlots"));
            for(int x = 0; x < 5; x++){
                final int posX = x;
                final int index = posX + 5 * posY;
                slotBuilder = new ControlBuilder("itemSlot") {{
                    id("itemSlot"+index);
                }};
                Element e = slotBuilder.build(nifty, screen, screen.findElementByName("inventoryColumn"+posY));
                e.findNiftyControl("itemSlot"+index, Droppable.class).addFilter(this);
            }
        }
        screen.findElementByName("inventorySlots").layoutElements();
    }
    
    private void createItems(){
        List<InventoryItem> items = new ArrayList<InventoryItem>();
        for(int i = 0; i < 10; i++){
            InventoryItem item = new InventoryItem();
            item.setType(InventoryItem.Type.values()[FastMath.rand.nextInt(InventoryItem.Type.values().length)]);
            item.setName(item.getType().name() + "Item");
            items.add(item);
        }
        ControlBuilder itemBuilder;
        for(int i = 0; i < items.size(); i++){
            final InventoryItem item = items.get(i);
            final int index = i;
            itemBuilder = new ControlBuilder("item") {{
                    id("item"+index);
                    visibleToMouse(true);
                }};
                Element e = itemBuilder.build(nifty, screen, screen.findElementByName("itemSlot"+index));
                e.findElementByName("#itemLabel").getRenderer(TextRenderer.class).setText(item.getName());
                itemMap.put(e.getId(), item);
        }
    }
    
    
}
