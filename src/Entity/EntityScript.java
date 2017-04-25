/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Editor.Attribute;
import ScriptingEngine.Script;
import ScriptingEngine.ScriptingEngine;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityScript extends Entity{

    private Attribute<String> path = new Attribute<String>("Path:", "");
    private Script script = null;
    
    public EntityScript(String path) {
        super(0, 0, 0, 0, EnumEntityType.SCRIPT);
        this.path.setData(path);
        super.addAttribute(this.path);
    }

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        
    }
    
    @Override
    public void onAdd(){
        String thePath = this.path.getData();
        
        if(this.path.getData().contains(".js")){
            
        }else{
            thePath = this.path.getData()+".js";
        }
        if(this.path.getData().isEmpty()){
            
        }else{
            this.script = ScriptingEngine.add(thePath);
            this.path.setData(thePath);
        }
    }
    
    @Override
    public void onRemove(){
        if(script != null){
            ScriptingEngine.remove(this.script);
        }
    }
    
    @Override
    public void onAttributeChange(){
        if(this.script!= null){
            ScriptingEngine.remove(this.script);
        }
         
        if(!this.path.getData().contains(".js")){
            this.path.setData(this.path.getData()+".js");
        }
        
        this.script = ScriptingEngine.add(this.path.getData());
    }
    
    @Override
    public String save() {
        return this.getType()+"{"+this.path.getData()+"}";
    }
    
}
