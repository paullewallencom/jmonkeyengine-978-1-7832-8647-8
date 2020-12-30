/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter03.util;

import chapter03.WaterCell;

/**
 *
 * @author reden
 */
public class CellUtil {
    
    private static int[][] directions = new int[][]{{0,-1},{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1}};
    public static int getDirection(int x, int y){
        switch(x){
            case 1:
                switch(y){
                    case -1:
                        return 1;
                    case 0:
                        return 2;
                    case 1:
                        return 3;
                }
                break;
            case -1:
                switch(y){
                    case -1:
                        return 7;
                    case 0:
                        return 6;
                    case 1:
                        return 5;
                }
                break;
            case 0:
                switch(y){
                    case -1:
                        return 0;
                    case 0:
                        return -1;
                    case 1:
                        return 4;
                }
                break;
        }
        return -1;
    }
    
    public static int[] getDirection(int dir){
        return directions[dir];
    }
    
}
