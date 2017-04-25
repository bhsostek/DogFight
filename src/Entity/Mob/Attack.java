/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob;

import Physics.RigidBody;
import Physics.Vector3D;

/**
 *
 * @author Bailey
 */
public class Attack {
    private float damage = 0;
    private Vector3D dir = new Vector3D(0,0,0);
    private RigidBody collision;
    //when animation has finished, remove this
    //private Animation animation;
    
    private boolean remove = false;
    
    public Attack(RigidBody collision, float damage, Vector3D dir){
       this.collision = collision;
       this.damage = damage;
       this.dir = dir;
    }
    
    public void tick(){
        //if(animation.isFinished()){
        remove = true;
        //}
    }
    
    public RigidBody getCollision(){
        return this.collision;
    }
    
    public Vector3D getDir(){
        return this.dir;
    }
    
    public float getDamage(){
        return this.damage;
    }
    
    public boolean shouldRemove(){
        return this.remove;
    }
}
