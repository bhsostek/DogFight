/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Base.Controller.EnumButtonType;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Entity.Mob.Mob;
import Graphics.GUI.Inventory;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class InventoryComponent extends AIComponent{

    private boolean keyPressed = false;
    private Debouncer openInventory = new Debouncer(false);
    private Inventory ivnentory;
    
    public InventoryComponent(Mob mob) {
        super(mob, EnumAIComponent.INVENTORY);
    }

    @Override
    public String extraData() {
        return "";
    }

    @Override
    public void tick() {
        if(super.mob.hasComponent(EnumAIComponent.CONTROLLER)){
            keyPressed = ((ControllerComponent)super.mob.getComponent(EnumAIComponent.CONTROLLER)).getKey(EnumButtonType.SELECT)>0.0f;
        }
        
        if(openInventory.risingAction(keyPressed)){
            TowerOfPuzzles.openUI(this.ivnentory);
        }
        
    }
    
    @Override
    public void render(Graphics g) {
        
    }
    
}
