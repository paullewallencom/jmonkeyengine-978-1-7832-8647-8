/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.state;

import chapter05.control.AIControl_RTS;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author reden
 */
public class AIAppState extends AbstractAppState{
    
    private List<AIControl_RTS> aiList = new ArrayList<AIControl_RTS>();
    private Map<Class<? extends AIStateRTS>, Spatial> resources = new HashMap<Class<? extends AIStateRTS>, Spatial>();
    
    private int wood;
    private float food = 10f;
    private float foodConsumption;
    private float minimumFoodStorage = 10f;
    private float timer = 0f;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        food -= foodConsumption * tpf;
        food = Math.max(0, food);
        
        timer-= tpf;
        if(timer <= 0f){
            System.out.println("Food: " + food + ", Wood: " + wood);
            evaluate();
            timer = 5f;
        }
    }
    
    private void evaluate(){
        float factorWood;

        float foodRequirement = foodConsumption * 20f + minimumFoodStorage;
        float factorFood = 1f - (Math.min(food, foodRequirement)) / foodRequirement;
        
        factorWood = 1f - factorFood;
        System.out.println(" " + factorWood + " " + factorFood);
        
        int numWorkers = aiList.size();
        int requiredFoodGatherers = (int) Math.round(numWorkers * factorFood);
        int foodGatherers = workersByState(GatherFoodState.class);
        int toSet = requiredFoodGatherers - foodGatherers;
        Class<? extends AIStateRTS> state = null;
        if(toSet > 0){
            state = GatherFoodState.class;
        } else if (toSet < 0){
            state = GatherWoodState.class;
            toSet = -toSet;
        }
        for(int i = 0; i < toSet; i++){
            if(setWorkerState(state)){
                toSet--;
            }
        }
    }
    
    private int workersByState(Class<? extends AIStateRTS> state){
        int amount = 0;
        for(AIControl_RTS ai: aiList){
            if(ai.getCurrentState() != null && ai.getCurrentState().getClass() == state){
                amount++;
            }
        }
        return amount;
    }
    
    private boolean setWorkerState(Class<? extends AIStateRTS> state){
        for(AIControl_RTS ai: aiList){
            if(ai.getCurrentState() == null || ai.getCurrentState().getClass() != state){
                ai.setCurrentState(state);
                ((GatherResourceState)ai.getCurrentState()).setResource(resources.get(state));
                return true;
            }
        }
        return false;
    }
    
    public void addWorker(AIControl_RTS aiControl){
        foodConsumption += 0.5f;
        aiList.add(aiControl);
        aiControl.setAiManager(this);
    }
    
    public void setResource(Class<? extends AIStateRTS> state,Spatial resource){
        resources.put(state, resource);
    }
    
    public void onFinishTask(Class<? extends AIStateRTS> state, int amount){
        if(state == GatherFoodState.class){
            food += amount;
        } else if (state == GatherWoodState.class){
            wood += amount;
        }
    }
}
