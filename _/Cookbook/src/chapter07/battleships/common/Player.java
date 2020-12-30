/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.battleships.common;

/**
 *
 * @author reden
 */
public class Player {
    
    private int id;
    private String name = "Unnamed";
    
    private int ships = 0;
    
    public Player(){
        
    }
    
    public void increaseShips(){
        ships++;
    }
    
    public void decreaseShips(){
        ships--;
    }
    
    public int getShips(){
        return ships;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
