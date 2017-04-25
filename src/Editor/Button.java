/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Graphics.GUI.UI;
import Base.MouseInput;
import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public abstract class Button extends UI{

    Rectangle rect;
    Sprite sprite;
    
    public Button(int x, int y, String sprite) {
        super(x, y);
        rect = new Rectangle(x, y, 32, 32);
        this.sprite = TowerOfPuzzles.spriteBinder.loadSprite("editor/"+sprite);
    }

    @Override
    public void tick() {
        
    }
    
    public abstract void event();

    @Override
    public void render(Graphics g) {
        sprite.render(x+rect.width/2, y+rect.height/2, g);
    }

    @Override
    public void onClick(Rectangle rect) {
        if(rect.intersects(this.rect)){
            this.event();
            TowerOfPuzzles.mouseInput.IsPressed = false;
            TowerOfPuzzles.mouseInput.IsRightClick = false;
        }
    }
    
}
