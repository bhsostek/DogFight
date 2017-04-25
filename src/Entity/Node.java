/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class Node {
    private boolean powered = false;
    int x;
    int y;
    
    int offsetX = 0;
    int offsetY = 0;
    
    Sprite img_powered;
    Sprite img_dePowered;
    
    public Node(int x, int y){
        this.x = x;
        this.y = y;
        img_powered = TowerOfPuzzles.spriteBinder.loadSprite("entity/nodes/powered.png");
        img_dePowered = TowerOfPuzzles.spriteBinder.loadSprite("entity/nodes/dePowered.png");
    }
    
    public void reset(){
        powered = false;
    }
    
    public void render(Graphics g){
        if(this.powered){
            img_powered.render(x+offsetX, y+offsetY, 5 * 4, 5 * 4, g);
        }else{
            img_dePowered.render(x+offsetX, y+offsetY, 5 * 4, 5 * 4, g);
        }
        
    }
    
    public void setPowered(boolean b){
        this.powered = b;
    }
    
    public boolean isPowered(){
        return this.powered;
    }
    
    public int getX(){
        return this.x+this.offsetX;
    }
    
    public int getY(){
        return this.y+this.offsetY;
    }
}
