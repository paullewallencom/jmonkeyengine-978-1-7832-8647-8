/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author reden
 */
public class AStarPathfinder {

    private List<WaypointNode> openList;
    private WaypointNode startNode;
    private WaypointNode goalNode;
    private static int multiple = 100;
    private static Comparator<WaypointNode> waypointComparator = new Comparator<WaypointNode>() {
        public int compare(WaypointNode wp1, WaypointNode wp2) {
            return wp1.getF() - wp2.getF();
        }
    };

    public AStarPathfinder(WaypointNode startNode, WaypointNode goalNode) {
        this.startNode = startNode;
        this.goalNode = goalNode;

        openList = new ArrayList<WaypointNode>();

        pathfind();
    }

    private void pathfind() {
        openList.add(startNode);

        WaypointNode current;
        while(!openList.isEmpty()) {
            current = openList.get(0);
            System.out.println("Current " + current);
            
            for (WaypointNode neighbor : current.getConnections()) {
                
                if (!neighbor.isClosed()) {
                    if (!neighbor.isOpen()) {
                    
                        openList.add(neighbor);
                        neighbor.setOpen(true);
                        setParent(current, neighbor);
                    } else if (current.getG() + neighbor.getPosition().distance(goalNode.getPosition()) < neighbor.getG()) { // new path is shorter
                        setParent(current, neighbor);
                    }
                }
            }

            openList.remove(current);
            current.setClosed(true);

            if (goalNode.isClosed()) {
                break;
            }
            // sort list
            Collections.sort(openList, waypointComparator);
        }
        System.out.println("done");
        // backtrack
        backtrack();
    }

    private void setParent(WaypointNode current, WaypointNode neighbor) {
        neighbor.setParent(current);
        neighbor.setG(current.getG() + (int) (current.getPosition().distance(neighbor.getPosition()) * multiple));
        if(neighbor.getH() == 0){
            neighbor.setH((int) (neighbor.getPosition().distance(goalNode.getPosition()) * multiple));
        }
        neighbor.updateF();
    }

    private void backtrack() {
        List<WaypointNode> path = new ArrayList<WaypointNode>();
        path.add(goalNode);
        WaypointNode parent = goalNode;
        while (parent != null) {
            parent = parent.getParent();
            path.add(parent);
        }
        
        for(WaypointNode wp: path){
            System.out.println(wp);
        }
    }
}
