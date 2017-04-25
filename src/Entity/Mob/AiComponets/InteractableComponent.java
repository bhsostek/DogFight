/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Entity.Mob.Mob;

/**
 *
 * @author Bailey
 */
public class InteractableComponent extends AIComponent{

    public InteractableComponent(Mob mob, EnumAIComponent component) {
        super(mob, component);
    }

    @Override
    public String extraData() {
        return "";
    }

    @Override
    public void tick() {
        
    }
    
}
