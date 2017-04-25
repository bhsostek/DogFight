/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Base.TowerOfPuzzles;
import Entity.EntityBlackHole;
import Entity.EntityManager;
import Entity.EnumEntityType;
import Physics.Vector3D;

/**
 *
 * @author Bailey
 */
public class Particle {
    Vector3D position = new Vector3D(0, 0, 0);
    Vector3D offset = new Vector3D(0, 0, 0);
    public Vector3D velocity = new Vector3D(0, 0, 0);
    public Vector3D acceleration = new Vector3D(0, 0, 0);
    
    public int lifespan;
    private final int maxLifespan;
    public boolean remove = false;
    
    public float rotationConstant = 0.0f;
    private float rotation = 0.0f;
    
    private int index = 0;

    public Particle(Vector3D dir, int lifespan){
        velocity = dir;
        this.lifespan = lifespan;
        maxLifespan = lifespan;
    }
    
    public void addGravity(){
        acceleration.increaseVelY(TowerOfPuzzles.GRAVITY);
    }
    
    public void calculateBlackHoles(){
        for(int i = 0; i<EntityManager.getEntities().length; i++){
            if(EntityManager.getEntities()[i].getType().equals(EnumEntityType.BLACK_HOLE)){
                this.velocity = (((EntityBlackHole)EntityManager.getEntities()[i]).calculateForce(position,velocity,acceleration));
            }
        }
    }
    
    public void tick(){
        if(lifespan > 0){
            lifespan--;

            rotation += rotationConstant;
            
            velocity.addVector(acceleration);
            position.addVector(velocity);

            rotation+=Math.random();
            acceleration.setVelX(0);
            acceleration.setVelY(0);
            acceleration.setVelZ(0);
        }
        if(lifespan <= 0){
            remove = true;
        }
    }
    
    public Particle setOffset(Vector3D offset){
        this.offset = offset;
        return this;
    }
    
    public Particle setPosition(Vector3D p){
        this.position = p;
        return this;
    }
    
    public int getIndex(int steps){
        return ((steps) - (int)Math.ceil(((float)lifespan/(float)maxLifespan) * (steps-1)));
    }
    
    public void setIndex(int i){
        index = i;
    }
    
    public int getIndex(){
        return this.index;
    }
    
    public int getX(){
        return (int)(this.position.getX()+this.offset.getX());
    }
    
    public int getY(){
        return (int)(this.position.getY()+this.offset.getY());
    }
    
    public float getRotation(){
        return this.rotation;
    }
}
