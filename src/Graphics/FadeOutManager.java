/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Base.Engine;
import Base.TowerOfPuzzles;
import java.awt.Color;
import java.awt.Graphics;
import javax.script.ScriptEngine;

/**
 *
 * @author Bailey
 */
public class FadeOutManager extends Engine{
    
    private static float totalTicks = 0;
    private static float maxTicks = 0;
    private static Color color = new Color(0,0,0,0);
    private static boolean hasFade = false;

    public FadeOutManager() {
        super("Fade");
    }
    
    public void tick(){
        if(hasFade){
            if(totalTicks>0){
                totalTicks--;
            }
            if(totalTicks<0){
                totalTicks++;
            }
            if(totalTicks==0){
                hasFade = false;
            }
        }
    }
    
    public void render(Graphics g){
        if(hasFade){
            float value = (1.0f-(totalTicks/maxTicks));
            if(value > 1.0f){
                value-=1.0f;
            }
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255 * value)));
            g.fillRect(0, 0, TowerOfPuzzles.WIDTH, TowerOfPuzzles.HEIGHT);
        }
    }
    
    public void fade(Color col, int ticks){
        totalTicks = (float)ticks;
        maxTicks = (float)Math.abs(ticks);
        color = col;
        hasFade = true;
    }

    @Override
    public void init() {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(), this);
    }

    @Override
    public void onShutdown() {

    }
}
