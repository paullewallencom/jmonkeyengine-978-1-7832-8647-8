/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05;

import com.jme3.math.FastMath;

/**
 *
 * @author reden
 */
public class TestBias {

    public static void main(String[] args) {
        float gold = 10f;
        float wood = 1f;
        float food = 0f;
        float foodConsumption = 1f;
        float desiredFoodStorage = 10f;

        float factorFood;
        float factorWood = 0.5f;
        float factorGold = 0.5f;


        if (gold > wood) {
            factorGold = wood / gold / 2f;
            factorWood = 1f - factorGold;
        } else if (wood > gold) {
            factorWood = gold / wood / 2f;
            factorGold = 1f - factorWood;
        }
        
        float foodLast = foodConsumption * 20f + desiredFoodStorage;
        float foodValue = 1f - (Math.min(food, foodLast)) / foodLast;
        
        

        System.out.println(" " + factorGold + " " + factorWood + " " + foodValue);
    }
}
