/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05;

import chapter05.pathfinding.AStarPathfinder;
import chapter05.pathfinding.WaypointNode;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;

/**
 *
 * @author reden
 */
public class TestPathfinding extends SimpleApplication{

    
    public static void main(String[] args){
        TestPathfinding app = new TestPathfinding();
        
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        
        boolean[][] world = new boolean[][]{
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, false, true, false, false, false, false, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, false, true, true},
            {true, true, true, true, true, true, true, false, true, true},
            {true, true, true, true, true, true, true, false, true, true},
        };
        
        WaypointNode[][] waypoints = new WaypointNode[10][10];
        
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++){
                if(world[x][y]){
                    waypoints[x][y] = new WaypointNode();
                    waypoints[x][y].setPosition(new Vector3f(x, 0, y));
                }
            }
        }
        
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++){
                WaypointNode wp = waypoints[x][y];
                if(wp != null){
                    for(int x2 = -1; x2 < 2; x2++){
                        if(x2 + x > -1 && x2 + x < 10){
                            for(int y2 = -1; y2 < 2; y2++){
                                if(y2 + y > -1 && y2 + y < 10){
                                    if(x2 != 0 || y2 != 0){
                                        WaypointNode wp2 = waypoints[x + x2][y + y2];
                                        if(wp2 != null){
                                            waypoints[x + x2][y + y2].addConnection(wp);
                                            wp.addConnection(waypoints[x + x2][y + y2]);
                                        } else {
                                            System.out.println((x+x2) + ", " + (y+y2) + " == null");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        AStarPathfinder pathfinder = new AStarPathfinder(waypoints[0][0], waypoints[9][9]);
    }
    
}
