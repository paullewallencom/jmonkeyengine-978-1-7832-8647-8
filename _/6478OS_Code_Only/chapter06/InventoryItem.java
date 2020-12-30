/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

/**
 *
 * @author reden
 */
public class InventoryItem {

    public enum Type {

        Hand, Head, Foot
    };
    private Type type;
    private String name;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
