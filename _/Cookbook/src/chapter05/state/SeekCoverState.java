/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter05.state;

import chapter05.waypoint.CoverPoint;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.util.List;

/**
 *
 * @author reden
 */
public class SeekCoverState extends AIState{

    private static List<CoverPoint> availableCovers;
    private CoverPoint targetCover;
    
    @Override
    public void stateEnter() {
        // sort by closest
        for(CoverPoint cover: availableCovers){
            if(aiControl.getTarget() != null){
                Vector3f directionToTarget = cover.getSpatial().getWorldTranslation().add(aiControl.getTarget().getWorldTranslation()).normalizeLocal();
                System.out.println(cover.getCoverDirection().dot(directionToTarget));
                if(cover.getCoverDirection().dot(directionToTarget) > 0){
                    targetCover = cover;
                    break;
                }
            }
        }
        if(targetCover == null){
            System.out.println("could not find cover");
        }
    }

    @Override
    public void stateExit() {
        aiControl.move(Vector3f.ZERO, false);
        
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(targetCover != null){
            Vector3f direction = targetCover.getSpatial().getWorldTranslation().subtract(this.spatial.getWorldTranslation());
            if(direction.length() < 1){
                targetCover = null;
            } else {
                direction.normalizeLocal();
                aiControl.move(direction, true);
            }
            
        } else {
            setEnabled(false);
            this.spatial.getControl(AttackState.class).setEnabled(true);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public static void setAvailableCovers(List<CoverPoint> availableCovers) {
        SeekCoverState.availableCovers = availableCovers;
    }
    
}
