/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Controller.EnumButtonType;
import Base.Keyboard;
import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteUtils;
import Physics.PhysicsEngine;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public abstract class EntityInteractable extends Entity{
    
    public Sprite highlight;
    private boolean shouldRender = false;
    private boolean debounce = true;
    
    public EntityInteractable(int x, int y, int width, int height, EnumEntityType eet) {
        super(x, y, width, height, eet);
    }
    
    public void setHighlight(Sprite sprite){
        this.highlight = SpriteUtils.outlineSprite(sprite);
    }

    public abstract void Update();
    public abstract void Render(Graphics g);
    public abstract void event();
    
    @Override
    public void update() {
        Update();
        if(highlight != null){
            shouldRender = PhysicsEngine.intersects(super.collision, "player");
            if(shouldRender&&debounce&&(Keyboard.E||(TowerOfPuzzles.controllerManager.getContoller(0).getButton(EnumButtonType.B)<0.0f))){
                debounce = false;
                event();
            }
            if(!debounce&&!(Keyboard.E||(TowerOfPuzzles.controllerManager.getContoller(0).getButton(EnumButtonType.B)<0.0f))){
                debounce = true;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        Render(g);
        if(highlight != null){
            if(shouldRender){
               highlight.render(x, y, width, height, g);
            }
        }
    }
    
    public boolean isHighlighted(){
        return this.shouldRender;
    }
    
}
