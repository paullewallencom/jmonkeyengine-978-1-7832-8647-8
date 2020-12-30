/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter08.network;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.network.Server;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import chapter07.fps.client.object.PhysicsObjectControl;
import chapter07.fps.common.object.Game;
import chapter07.fps.common.object.NetworkedPlayerControl;
import chapter07.fps.server.FPSServer;
import chapter07.message.messages.PhysicsObjectMessage;

/**
 *
 * @author reden
 */
public class ServerPhysicsAppState extends AbstractAppState{
    
    private SimpleApplication app;
    private FPSServer server;
    private Game game;
    private BulletAppState bulletAppState;
    

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        initPhysics();
        
        createPhysicsTestObjects();
        
        bulletAppState.getPhysicsSpace().add(server.getLevelNode().getChild("terrain-TestScene").getControl(PhysicsControl.class));
    }

    
    private void initPhysics() {
        bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
    }
    
    private void createPhysicsTestObjects(){
        int id = 1;
        Map<Integer, Spatial> physicsObjects = new HashMap<Integer, Spatial>();
        for(int i = 0; i < 3; i++){
            Geometry smallBox = new Geometry("small box", new Box(0.1f, 0.1f, 0.1f));
            smallBox.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(0.1f, 0.1f, 0.1f)), 0.1f));
            smallBox.addControl(new PhysicsObjectControl(id++));
            smallBox.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(5f, 5f, 5f));
//            bulletAppState.getPhysicsSpace().add(smallBox);
//            app.getRootNode().attachChild(smallBox);
//            physicsObjects.add(smallBox);
        }
        
        for(int i = 0; i < 3; i++){
            Geometry bigBox = new Geometry("big box", new Box(1f, 1f, 1f));
            bigBox.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(1f, 1f, 1f)), 0.1f));
            bigBox.addControl(new PhysicsObjectControl(id));
            bigBox.getControl(PhysicsObjectControl.class).setServerControllled(true);
            bigBox.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f( i * 2f, 5f, i * 2f));
            bulletAppState.getPhysicsSpace().add(bigBox);
            app.getRootNode().attachChild(bigBox);
            physicsObjects.put(id, bigBox);
            id++;
        }
        game.setPhysicsObjects(physicsObjects);
    }
    
    public void setGame(Game game){
        this.game = game;
    }
    
    public void setServer(FPSServer s){
        this.server = s;
    }
    
    public void addPlayer(BetterCharacterControl rigidBody){
        bulletAppState.getPhysicsSpace().add(rigidBody);
    }
    
    public void removePlayer(BetterCharacterControl rigidBody){
        bulletAppState.getPhysicsSpace().remove(rigidBody);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Collection<Spatial> physicsObjects = game.getPhysicsObjects().values();
        for(Spatial physicsObject: physicsObjects){
            if(physicsObject.getControl(PhysicsObjectControl.class).isServerControllled() && physicsObject.getControl(RigidBodyControl.class).isActive()){
                // create message
                PhysicsObjectMessage message = new PhysicsObjectMessage();
                message.setObjectId(physicsObject.getControl(PhysicsObjectControl.class).getId());
                message.setTranslation(physicsObject.getControl(RigidBodyControl.class).getPhysicsLocation());
                message.setRotation(physicsObject.getControl(RigidBodyControl.class).getPhysicsRotation());
                server.sendMessage(message);
            }
            
        }
    }
    
    
}
