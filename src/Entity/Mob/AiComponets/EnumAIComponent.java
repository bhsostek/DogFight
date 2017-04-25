/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Base.util.StringUtils;
import Entity.Mob.Mob;

/**
 *
 * @author Bailey
 */
public enum EnumAIComponent {
    
    SPRITE(),
    FOLLOW(),
    GRAVITY(),
    HIT_BOX(),
    HEALTH(),
    INTERACTABLE(),
    SCRIPT(),
    SOUND(),
    CONTROLLER(),
    INVENTORY(),
    ;
    
    public AIComponent generate(Mob mob, String[] data){
        switch(this){
            case SPRITE:
                return new FlipingSpriteComponent(mob, data[0]);
            case FOLLOW:
                return new Follow(mob, Integer.parseInt(data[0]), Float.parseFloat(data[1]));
            case GRAVITY:
                return new GravityComponent(mob, Float.parseFloat(data[0]));
            case HIT_BOX:
                return new HitBox(mob, Integer.parseInt(data[0]), Integer.parseInt(data[1]));
            case HEALTH:
                return new HealthComponent(mob, Integer.parseInt(data[0]));
            case CONTROLLER:
                return new ControllerComponent(mob, Integer.parseInt(data[0]));
        }
        System.out.println("EnumAIComponent[generate]:"+this.name()+":"+StringUtils.unify(data));
        return null;
    }
}
