/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Entity.Entity;
import Entity.EntityManager;
import Entity.EntityText;
import Entity.Mob.Attack;
import Entity.Mob.Mob;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;

/**
 *
 * @author Bailey
 */
public class HitBox extends AIComponent{
    
    public HitBox(Mob mob, int width, int height) {
        super(mob, EnumAIComponent.HIT_BOX);
        super.mob.width = width*4;
        super.mob.height = height*4;
        super.mob.collision = PrebuiltBodies.quad(new Point2D(super.mob.getX(),super.mob.getY()), super.mob.width,  super.mob.height);
    }

    @Override
    public void tick() {
        Attack attack = TowerOfPuzzles.attackManager.collides(super.mob.collision);
        if(attack!=null){
            float healthLost = attack.getDamage();
            ((Mob)(super.mob)).health-=healthLost;
            super.mob.acceleration.addVector(attack.getDir());
            TowerOfPuzzles.entityManager.add(new EntityText((int)super.mob.getX(), (int)super.mob.getY(), (int)healthLost+""));
        } 
    }

    @Override
    public String extraData() {
        return (super.mob.width/4)+","+(super.mob.height/4);
    }
    
}
