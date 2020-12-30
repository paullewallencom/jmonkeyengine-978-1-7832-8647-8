/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.battleships.common;

/**
 *
 * @author reden
 */
public class Ship {
    
    private int segments;
    private String name;
    
    public Ship(String name, int length){
        this.name = name;
        this.segments = length;
    }
    
    public Ship(int length){
        segments = length;
    }
    
    public void hit(){
        if(segments > 0){
            segments--;
        }
    }
    
    public boolean isSunk(){
        return segments < 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSegments() {
        return segments;
    }

    public void setSegments(int segments) {
        this.segments = segments;
    }
}
