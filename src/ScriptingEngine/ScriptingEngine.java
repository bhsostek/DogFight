/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptingEngine;

import Base.MouseInput;
import Base.MousePositionLocator;
import Base.TowerOfPuzzles;
import Item.Item;
import Item.Items;
import ScriptingEngine.exceptions.ScriptNotFoundException;
import World.Tiles.TileConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.LinkedList;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Bayjose
 */
public class ScriptingEngine {

    private static Script[] scripts = new Script[]{};
    private static LinkedList<Script> toAdd = new LinkedList<Script>();
    private static LinkedList<Script> toRemove = new LinkedList<Script>();
    
    private static ScriptEngineManager manager = new ScriptEngineManager();
    private static ScriptEngine engine = manager.getEngineByName("JavaScript");
    private static Invocable inv = (Invocable) engine;
    
    public ScriptingEngine(){
        //Register Commands
        engine.put("out", System.out);
        engine.put("Keyboard", TowerOfPuzzles.KEYBOARD);
        engine.put("MouseLocation", MousePositionLocator.MouseLocation);
        engine.put("SCALE", TileConstants.SCALE);
        engine.put("player", TowerOfPuzzles.player);
        engine.put("Items", Items.getItems());
    }

    private void updateScripts(){
        //only run if need to
        if(toRemove.size()>0 || toAdd.size()>0){
            LinkedList<Script> newBodies = new LinkedList<Script>();
            //remove all lights flagged to remove
            for(int i = 0; i < this.scripts.length; i++){
                if(toRemove.contains(this.scripts[i])){
                    //Remove that body
                }else{
                    newBodies.add(this.scripts[i]);
                }
            }
            //clear the buffer
            toRemove.clear();
            
            //add all new lights
            for (Script body : toAdd) {
                newBodies.add(body);
            }
            //clear that buffer
            toAdd.clear();
            //build new light array
            Script[] out = new Script[newBodies.size()];
            for(int i = 0; i < newBodies.size(); i++){
                out[i] = newBodies.get(i);
            }
            //set it
            this.scripts = out;
        }
    }
    
    public void tick(){
        updateScripts();  
        for(Script script : scripts){
            try {
                script.tick(inv);
            } catch (ScriptException | NoSuchMethodException ex) {
                ex.printStackTrace();
                remove(script);
            }
        }
    }
    
    public static void remove(Script object){
        toRemove.add(object);
    }
    
    public static void add(Script object){
        if(object!=null){
            toAdd.add(object);
        }
    }
    
    public static Script add(String name){
        Script script;
        try {
            script = new Script(engine, TowerOfPuzzles.Path+TowerOfPuzzles.ScriptPath+name);
            script.init(inv);
        } catch (ScriptException | IOException | NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
        toAdd.add(script);
        return script;
    }

    public static void killScripts(String name) {
        for(Script bod : scripts){
            if(bod.getFilePath().equals(name)){
                remove(bod);
            }
        }
    }
    
    public static void killAllScripts() {
        for(Script bod : scripts){
            remove(bod);
        }
    }

    public void render(Graphics g) {
        for(Script script : scripts){
            try {
                script.render(inv,g);
            } catch (ScriptException | NoSuchMethodException ex) {
                ex.printStackTrace();
                remove(script);
            }
        }
    }
    
    public static Script findScript(String name, Script[] scripts) throws ScriptNotFoundException{
        for(int i = 0; i < scripts.length; i++){
            if(scripts[i].getFilePath().replace(".js", "").equals(name)){
                return scripts[i];
            }
        }
        
        throw new ScriptNotFoundException(name);
    }
    
    public static Script[] getScripts(){
        return scripts;
    }
    
    public ScriptEngine getEngine(){
        return engine;
    }
}