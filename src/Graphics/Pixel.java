/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public abstract class Pixel{
    private int color = 0;
    //uuid for the display
//    private final int id;
    
    public Pixel(int color){
        this.color = color;
    }
    
    public abstract void render(Graphics g);
    
    public int getColor(){
        return this.color;
    }
    
    public int setColor(int color){
        this.color = color;
        return color;
    }
    
    public Pixel setRGBA(int r, int g, int b, int a){
        this.color = (r * 65536) + (g * 256) + (b * 1) + (a * 16777216);
        return this;
    }
}
