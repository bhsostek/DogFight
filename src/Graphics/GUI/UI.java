/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GUI;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public abstract class UI {
    public int x;
    public int y;
    
    public UI(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void onOpen(){
        return;
    }
    
    public void onClose(){
        return;
    }
    
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract void onClick(Rectangle rect);
}
