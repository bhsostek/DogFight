/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Physics.PhysicsEngine;
import Physics.RigidBody;
import Physics.RigidUtils;
import Physics.Vector3D;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public abstract class EntityFallingObject extends Entity{
    
    Vector3D velocity = new Vector3D(0,0,0);
    Vector3D acceleration = new Vector3D(0,0,0);
    
    boolean hasHit = false;
    
    public EntityFallingObject(int x, int y, int width, int height, EnumEntityType eet) {
        super(x, y, width, height, eet);
    }

    @Override
    public void update() {
        if(!hasHit){
            acceleration.addVector(new Vector3D(0,TowerOfPuzzles.GRAVITY,0));
            velocity.addVector(acceleration);
            
            //X
            for(int i = 0; i < Math.abs(velocity.getX()); i++){
                this.x += (int)(velocity.getX()/Math.abs(velocity.getX()));
                RigidUtils.MoveTo(new Vector3D(x, y, 0), super.collision);
                for(RigidBody bod : PhysicsEngine.getChannel("solids").collisons){
                    if(bod != this.collision){
                        if(RigidUtils.Collides(super.collision, bod)){
                            this.x-= (int)(velocity.getX()/Math.abs(velocity.getX()));
                            RigidUtils.MoveTo(new Vector3D(x, y, 0), super.collision);
                            break;
                        }
                    }
                }
            }
            
            //Y
            for(int i = 0; i < Math.abs(velocity.getY()); i++){
                this.y += (int)(velocity.getY()/Math.abs(velocity.getY()));
                RigidUtils.MoveTo(new Vector3D(x, y, 0), super.collision);
                for(RigidBody bod : PhysicsEngine.getChannel("solids").collisons){
                    if(bod != this.collision){
                        if(RigidUtils.Collides(super.collision, bod)){
                            this.y-= (int)(velocity.getY()/Math.abs(velocity.getY()));
                            hasHit = true;
                            RigidUtils.MoveTo(new Vector3D(x, y, 0), super.collision);
                            break;
                        }
                    }
                }
            }
        }
        acceleration.setVelX(0);
        acceleration.setVelY(0);
        acceleration.setVelZ(0);
        obj_update();
    }
    
    public abstract void obj_update();
    public abstract void render(Graphics g);
    
}
