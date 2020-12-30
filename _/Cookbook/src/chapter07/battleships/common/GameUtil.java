/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07.battleships.common;

import com.jme3.math.FastMath;
import com.jme3.network.serializing.Serializer;
import chapter07.battleships.message.FireActionMessage;
import chapter07.battleships.message.FiringResult;
import chapter07.battleships.message.GameMessage;
import chapter07.battleships.message.GameStatusMessage;
import chapter07.battleships.message.PlaceShipMessage;
import chapter07.battleships.message.PlayerNameMessage;
import chapter07.battleships.message.TurnMessage;
import chapter07.battleships.message.WelcomeMessage;

/**
 *
 * @author reden
 */
public class GameUtil {
    
    public static void init(){
        Serializer.registerClass(WelcomeMessage.class);
        Serializer.registerClass(FireActionMessage.class);
        Serializer.registerClass(PlayerNameMessage.class);
        Serializer.registerClass(GameStatusMessage.class);
        Serializer.registerClass(TurnMessage.class);
        Serializer.registerClass(GameMessage.class);
        Serializer.registerClass(FiringResult.class);
        Serializer.registerClass(PlaceShipMessage.class);
    }
    
    private static Ship[] ships = new Ship[]{new Ship("PatrolBoat", 2), new Ship("Destroyer", 3), new Ship("Submarine", 3), new Ship("Battleship", 4), new Ship("Carrier", 5)};
    public static Ship[][] generateBoard(){
        
        int[] order = new int[]{0, 1, 2, 3, 4};
        for(int i = 0; i < 5; i++){
            int first = i;
            int second = FastMath.nextRandomInt(0, 4);
            int temp = order[first];
            order[first] = order[second];
            order[second] = temp;
        }
        int posX = 0;
        int posY = 0;
        int maxY = 0;
        while(true){
            Ship[][] board = new Ship[10][10];
            int shipsPlaced = 0;
            for(int i = 0; i < 5; i++){
                Ship s = ships[order[i]];
                boolean horizontal = FastMath.nextRandomInt(0, 1) == 1;
                posX += FastMath.nextRandomInt(0, 9);
                posY = maxY;
                if(posX > 9 || (horizontal && posX + s.getSegments() > 9)){
                    posX = (posX + s.getSegments()) % 9;
                    posY++;
                } 
                if (posY > 9 || (!horizontal && posY + s.getSegments() > 9)){
                    break;
                }
                for(int j = 0; j < s.getSegments(); j++){
                    if(horizontal){
                        board[posX + j][posY] = s;
                    } else {
                        board[posX][posY + j] = s;
                        maxY = posY + j + 1;
                    }
                }
                shipsPlaced++;
            }
            if(shipsPlaced == 5){
                return board;
            } else {
                System.out.println("Only " + shipsPlaced + " ships placed");
            }
        }
        
        
    }
    
    public static void main(String[] args){
        Ship[][] board = generateBoard();
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++){
                if(board[x][y] != null){
                    System.out.print("x,");
                } else {
                    System.out.print(" ,");
                }
            }
            System.out.print("\r");
        }
    }
    
    public static Ship getShip(int id){
        return new Ship(ships[id].getName(), ships[id].getSegments());
    }
}
