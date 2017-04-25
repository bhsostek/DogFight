/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lighting;

import Base.Engine;
import Base.MousePositionLocator;
import Base.util.DynamicCollection;
import Graphics.PixelUtils;
import World.Tiles.TileConstants;
import java.awt.Graphics;
import javax.script.ScriptEngine;

/**
 *
 * @author Bailey the cutie
 */
public class LightingEngine extends Engine{

    private DynamicCollection<Light> lights;
    
    public LightingEngine() {
        super("LightingEngine");
    }

    @Override
    public void init() {
        lights = new DynamicCollection<Light>();
    }

    @Override
    public void tick() {
        lights.tick();
    }

    @Override
    public void render(Graphics g) {

    }
    
   
    public void resetMoved() {
        for(Light light:lights.getCollection(Light.class)){
            light.resetMoved();
        }
    }

    @Override
    public void registerForScripting(ScriptEngine engine) {

    }

    @Override
    public void onShutdown() {

    }
    
    public void addLight(Light light){
        this.lights.add(light);
    }
    
    public void removeLight(Light light){
        this.lights.remove(light);
    }
    
    public Light[] getLights(){
        return this.lights.getCollection(Light.class);
    }
    
}
