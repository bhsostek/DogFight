/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Graphics.GUI.UI;
import Base.MouseInput;
import Base.MousePositionLocator;
import Base.TowerOfPuzzles;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class ScrollBar extends UI{
    private Rectangle scroller;
    
    private boolean moving = false;
    private final int height;
    
    public ScrollBar(int x, int y, int height) {
        super(x, y);
        scroller = new Rectangle(x,y,16,128);
        this.height = height;
    }

    public ScrollBar setHeight(int i){
        this.scroller.height = i;
        return this;
    }
    
    @Override
    public void tick() {
        if(TowerOfPuzzles.mouseInput.IsPressed){
            if(moving == true){
                scroller.y = Math.max(Math.min(MousePositionLocator.MouseLocation.y, y+height-scroller.height),y);
            }
        }else{
            moving = false;
        }
    }

    @Override
    public void render(Graphics g) {
       g.setColor(Color.GRAY);
       g.fillRect(x, y, 16, height);
       g.setColor(Color.DARK_GRAY);
       g.drawRect(x, y, 16, height);
       g.setColor(Color.LIGHT_GRAY);
       g.fillRect(scroller.x, scroller.y, scroller.width, scroller.height);
       g.setColor(Color.DARK_GRAY);
       g.drawRect(scroller.x, scroller.y, scroller.width, scroller.height);
    }

    @Override
    public void onClick(Rectangle rect) {
        if(TowerOfPuzzles.mouseInput.Mouse.intersects(scroller)){
            this.moving = true;
        }
    }
    
    public float getOffset(){
        return (float)(scroller.y-y)/(float)(height-scroller.height);
    }
    
    public Rectangle getCollision(){
        return new Rectangle(x, y, 16, height);
    }
    
    public void offset(int units){
        scroller.y = Math.max(Math.min(scroller.y+units, y+height-scroller.height),y);
    }
    
}
