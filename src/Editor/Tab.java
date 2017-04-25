/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Graphics.GUI.UI;
import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class Tab extends UI{
    
    private UI ui;
    private boolean selected = false;
    private final String name;
    private Sprite sprite;
    private Rectangle rect;

    public Tab(int x, int y, UI child, String name) {
        super(x, y);
        this.name = name;
        this.sprite = TowerOfPuzzles.spriteBinder.loadSprite("editor/tab_bg.png");
        this.rect = new Rectangle(x, y, 128, 48);
        this.ui = child;
    }

    @Override
    public void tick(){
        if(this.selected){
            if(this.ui!=null){
                ui.tick();
            }
        }
    }

    @Override
    public void render(Graphics g){
    
        sprite.render(x+rect.width/2, y+rect.height/2, rect.width, rect.height, g);
        g.setColor(Color.BLACK);
//        g.drawRect(rect.x, rect.y, rect.width, rect.height);
        g.drawString(name, x, y+g.getFont().getSize());
        
        if(this.selected){
            if(this.ui!=null){
                ui.render(g);
            }
        }
    }

    public void setSelected(Boolean b){
        this.selected = b;
    }
    
    public boolean isSelected(){
        return this.selected;
    }
    
    public boolean collides(Rectangle rect){
        if(this.rect.intersects(rect)){
            return true;
        }
        return false;
    }
    
    @Override
    public void onClick(Rectangle rect){
        if(this.rect.intersects(rect)){
            if(!this.selected){
                this.selected = true;
                //play sound
            }
            return;
        }
        if(this.selected){
            if(this.ui!=null){
                ui.onClick(rect);
            }
        }
    }
    
    
}
