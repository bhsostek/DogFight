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
import Graphics.SpriteBinder;
import Physics.PhysicsEngine;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityPedestalButton extends EntityInteractable{

    Sprite pedestal;
    Sprite button;
    
    private final int maxTicks = 30;
    private int ticks = 0;
    private int distance = (3 * 4);
    
    private boolean debounce = false;
    
    public EntityPedestalButton(int x, int y) {
        super(x, y, (18 * 4), (21 * 4), EnumEntityType.PEDESTAL);
        
        pedestal = TowerOfPuzzles.spriteBinder.loadSprite("entity/pedestal/base.png");
        button = TowerOfPuzzles.spriteBinder.loadSprite("entity/pedestal/button.png");
        
        super.setHighlight(pedestal);
        
        Node[] nodes = new Node[]{
            new Node(0, 0),
            new Node(0, 16*4),
        };
        super.setNodes(nodes);
    }

    @Override
    public void Update() {
        if(super.getNodes()[1].isPowered()){
            if(ticks>0){
                ticks = 0;
                super.getNodes()[0].setPowered(false);
            }
        }
        if(PhysicsEngine.intersects(collision, "player")){
            if((Keyboard.E||(TowerOfPuzzles.controllerManager.getContoller(0).getButton(EnumButtonType.B)>0.0f))){
                if(debounce){
                    super.getNodes()[0].setPowered(!super.getNodes()[0].isPowered());
                    debounce = false;
                }
            }else{
                debounce = true;
            }
        }
        
        if(super.getNodes()[0].isPowered()){
            if(ticks < maxTicks){
                ticks ++;
            }
        }
        if(!super.getNodes()[0].isPowered()){
            if(ticks > 0){
                ticks --;
            }
        }
    }

    @Override
    public void Render(Graphics g) {
        button.render(x, y+(int)(((float)ticks / (float) maxTicks) * distance) - 2, width, height, g);
        pedestal.render(x, y, width, height, g);
    }

    @Override
    public void event() {
        
    }
    
}
