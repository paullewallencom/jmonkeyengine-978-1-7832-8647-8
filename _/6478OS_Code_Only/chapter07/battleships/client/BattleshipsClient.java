/*
 * Copyright (c) 2011 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package chapter07.battleships.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.io.IOException;

import com.jme3.network.Client;
import com.jme3.network.Network;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import chapter07.battleships.common.Game;
import chapter07.battleships.common.GameUtil;
import chapter07.battleships.common.Player;
import chapter07.battleships.common.Ship;
import chapter07.battleships.message.FireActionMessage;
import chapter07.battleships.message.PlaceShipMessage;

public class BattleshipsClient {

    private Client client;
    private Player thisPlayer;
    private Game game;
    private boolean myTurn;
    
    public static void main(String[] args) throws Exception {
        GameUtil.init();
        BattleshipsClient client = new BattleshipsClient();
    }

    public BattleshipsClient() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream("network/resources/network.properties"));
        client = Network.connectToServer(prop.getProperty("server.name"), Integer.parseInt(prop.getProperty("server.version")), prop.getProperty("server.address"), Integer.parseInt(prop.getProperty("server.port")));
        
        
        game = new Game();
        
        ClientMessageHandler messageHandler = new ClientMessageHandler(this, game);
        client.addMessageListener(messageHandler);
        client.start();
        
        thisPlayer = new Player();
    }
    
    public Game getGame(){
        return game;
    }

    public Player getThisPlayer(){
        return thisPlayer;
    }

    public boolean isMyTurn() {
        return myTurn;
    }
    
    public void placeShips(){
        for(int i = 0; i < 5; i++){
            Ship s = GameUtil.getShip(i);
            
            boolean waitForInput = true;
            int x = -1;
            int y = -1;
            while(waitForInput){
                try{
                    System.out.println("Enter coordinates for your " + s.getName() + "(" + s.getSegments() + " segments) in the format 'x,y,h/v'");
            
                    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                    String input = bufferRead.readLine();
                    String[] coords = input.split(",");
                    try{
                        x = Integer.parseInt(coords[0]);
                        y = Integer.parseInt(coords[1]);
                        waitForInput = false;
                    } catch(Exception e){
                        System.out.println("Coordinates must be entered as numbers");
                    }
                    boolean horizontal = coords[2].equals("h");
                    PlaceShipMessage shipMessage = new PlaceShipMessage();
                    shipMessage.setGameId(game.getId());
                    shipMessage.setPlayerId(thisPlayer.getId());
                    shipMessage.setShip(i);
                    shipMessage.setX(x);
                    shipMessage.setY(y);
                    shipMessage.setHorizontal(horizontal);
                    thisPlayer.increaseShips();
                    client.send(shipMessage);
                } catch(IOException ex){
                    Logger.getLogger(BattleshipsClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
        
        if(myTurn){
            System.out.println("It is your turn!");
                try {
                    boolean waitForInput = true;
                    int x = -1;
                    int y = -1;
                    while(waitForInput){
                        System.out.println("Enter coordinates to fire at in the format 'x,y' ");
                        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                        String input = bufferRead.readLine();
                        String[] coords = input.split(",");
                        try{
                            x = Integer.parseInt(coords[0]);
                            y = Integer.parseInt(coords[1]);
                            waitForInput = false;
                        } catch(Exception e){
                            System.out.println("Coordinates must be entered as numbers");
                        }
                    }
                    FireActionMessage fireMessage = new FireActionMessage();
                    fireMessage.setGameId(game.getId());
                    fireMessage.setPlayerId(thisPlayer.getId());
                    fireMessage.setX(x);
                    fireMessage.setY(y);
                    client.send(fireMessage);
                } catch (IOException ex) {
                    Logger.getLogger(ClientMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
        } else {
            System.out.println("Waiting for other player's move...");
        }
    }
}
