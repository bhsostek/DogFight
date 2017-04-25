/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob;

import Base.util.DynamicCollection;
import Physics.RigidBody;
import Physics.RigidUtils;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class AttackManager{
    public DynamicCollection<Attack> attacks = new DynamicCollection<Attack>();
    
    public void tick(){
        attacks.tick();
        for(Attack attack:attacks.getCollection(Attack.class)){
            attack.tick();
            if(attack.shouldRemove()){
                attacks.remove(attack);
            }
        }
    }
    
    public void render(Graphics g){
        for(Attack attack:attacks.getCollection(Attack.class)){
            RigidUtils.RenderWireframe(attack.getCollision(), g);
        }
    }
    
    public Attack collides(RigidBody mob){
        for(Attack attack:attacks.getCollection(Attack.class)){
            if(RigidUtils.Collides(mob, attack.getCollision())){
                return attack;
            }
        }
        return null;
    }
}
