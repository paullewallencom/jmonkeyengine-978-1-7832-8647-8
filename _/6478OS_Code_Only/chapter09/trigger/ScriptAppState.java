/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter09.trigger;

import com.jme3.app.state.AbstractAppState;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reden
 */
public class ScriptAppState extends AbstractAppState{
    
    private List<ScriptObject> scriptObjects = new ArrayList<ScriptObject>();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if(isEnabled()){
            for(ScriptObject scriptObject: scriptObjects){
                scriptObject.update(tpf);
            }
        }
    }
    
    public void addScriptObject(ScriptObject scriptObject){
        scriptObjects.add(scriptObject);
    }
    
    public void removeScriptObject(ScriptObject scriptObject){
        scriptObjects.remove(scriptObject);
    }
}
