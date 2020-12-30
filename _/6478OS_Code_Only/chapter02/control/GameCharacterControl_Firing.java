/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter02.control;


/**
 *
 * @author Rickard
 */
public class GameCharacterControl_Firing extends GameCharacterControl{

    /**
    * new
    */
    private float cooldownTime = 1f;
    private float cooldown = 0f;
    /**
    * end new
    */
    public GameCharacterControl_Firing(float radius, float height, float mass) {
        super(radius, height, mass);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        /**
         * new
         */
        if(cooldown > 0){
            cooldown -= tpf;
            cooldown = Math.max(cooldown, 0);
        }
        /**
         * end new
         */
    }

    /**
    * new
    */
    public float getCooldown(){
        return cooldown;
    }
    
    public void onFire(){
        cooldown = cooldownTime;
    }
    /**
    * end new
    */
}
