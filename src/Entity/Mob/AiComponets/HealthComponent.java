/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Editor.Attribute;
import Entity.Mob.Mob;

/**
 *
 * @author Bailey
 */
public class HealthComponent extends AIComponent{
    
    private Attribute<Integer> health = new Attribute<Integer>("Health:", 1);
    
    public HealthComponent(Mob mob, int health) {
        super(mob, EnumAIComponent.HIT_BOX);
        this.mob.health = health;
        this.health.setData(this.mob.health);
        super.addAttribute(this.health);
    }

    @Override
    public void tick() {

    }

    @Override
    public String extraData() {
        return (super.mob.health)+"";
    }
    
}
