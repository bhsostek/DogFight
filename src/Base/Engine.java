/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.awt.Graphics;
import javax.script.ScriptEngine;

/**
 *
 * @author Bailey
 */
public abstract class Engine {
    
    private final String engineName;
    
    public Engine(String engine){
        engineName = engine;
        Initialize();
    }
    
    public void Initialize(){
        try{
            System.out.print("Initializing Engine:"+engineName+"...");
            init();
            System.out.println("Success.");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failure.");
        }
    }
    
    public String getName(){
        return this.engineName;
    }
    
    public abstract void init();
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract void registerForScripting(ScriptEngine engine);
    public abstract void onShutdown();
}
