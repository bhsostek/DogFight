/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Entity.Mob.Mob;
/**
 *
 * @author Bailey
 */
public class GravityComponent extends AIComponent{
    
    private Attribute<Float> gravity = new Attribute<Float>("Gravitational Constant:", TowerOfPuzzles.GRAVITY);

    public GravityComponent(Mob mob, float gravity) {
        super(mob, EnumAIComponent.GRAVITY);
        this.gravity.setData(gravity);
        super.addAttribute(this.gravity);
    }

    @Override
    public void tick() {
        super.mob.acceleration.increaseVelY(gravity.getData());
    }

    @Override
    public String extraData() {
        return this.gravity.getData()+"";
    }
    
}
