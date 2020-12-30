/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03;

import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author reden
 */
public class WaterCell{
    
    private float amount;
    private int direction;
    private float terrainHeight;
    private Geometry geometry;
    private float incomingWater;

    @Override
    public WaterCell clone(){
        WaterCell clone = new WaterCell();
        clone.amount = amount;
        clone.direction = direction;
        clone.terrainHeight = terrainHeight;
        return clone;
    }
    
    public float compareCells(float otherHeight, float cellAmount){
//        float cellAmount = otherCell.getAmount();
        float difference = (otherHeight + cellAmount) - (terrainHeight + amount);
        float amountToChange = 0;
        if(difference > 0.1f){
            amountToChange = difference * 0.5f;
            amountToChange = Math.min(amountToChange, cellAmount);
            incomingWater += amountToChange;
//            otherCell.adjustAmount(-amountToChange);
        }
        
        return amountToChange;
    }
    
    public void adjustAmount(float delta){
        amount += delta;
    }
    
    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public float getTerrainHeight() {
        return terrainHeight;
    }

    public void setTerrainHeight(float terrainHeight) {
        this.terrainHeight = terrainHeight;
    }

    public float getIncomingWater() {
        return incomingWater;
    }

    public void setIncomingWater(float incomingWater) {
        this.incomingWater = incomingWater;
    }
    
    public void adjustIncomingWater(float delta){
        this.incomingWater += delta;
    }
    
    public Geometry getGeometry(){
        if(amount == 0f){
            geometry = null;
        } else {
            if(geometry == null){
                geometry = new Geometry("WaterCell", new Box(1f, 1f, 1f));
            }
            geometry.setLocalScale(1, 1f + amount, 1);
        }
        return geometry;
    }
    
}
