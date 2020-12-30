/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.pathfinding;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reden
 */
public class WaypointNode extends AbstractControl{
    
    private int f;
    private int h; // current to goal
    private int g; // start to current
    private boolean open;
    private boolean closed;
    
    private List<WaypointNode> connections = new ArrayList<WaypointNode>();
    private WaypointNode parent;
    private Vector3f position = new Vector3f();

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    
    public void updateF(){
        f = g + h;
    }

    public List<WaypointNode> getConnections() {
        return connections;
    }

    public void setConnections(List<WaypointNode> connections) {
        this.connections = connections;
    }
    
    public boolean addConnection(WaypointNode node){
        return connections.add(node);
    }
    
    public boolean removeConnection(WaypointNode node){
        return connections.remove(node);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public WaypointNode getParent() {
        return parent;
    }

    public void setParent(WaypointNode parent) {
        this.parent = parent;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public String toString(){
        return "Position " + position + " G " + g + " H " + h;
    }
}
