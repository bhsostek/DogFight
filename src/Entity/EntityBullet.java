/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Engine;
import Base.TowerOfPuzzles;
import Base.util.DistanceCalculator;
import Entity.Mob.Attack;
import Graphics.Sprite;
import Physics.PhysicsEngine;
import Physics.RigidUtils;
import Physics.Vector3D;
import java.awt.Graphics;
import java.util.LinkedList;

/**
 *
 * @author bhsostek
 */
public class EntityBullet extends Entity{
    
    public Vector3D direction = new Vector3D(0,0,0);
    public Vector3D magnitude = new Vector3D(1.0f,1.0f,1.0f);
    
    private final float wavelengthMax=32.0f;
    
    private final EntityParticleEmitter emitter;
    
    private final int maxDistance = Math.max(TowerOfPuzzles.WIDTH, TowerOfPuzzles.HEIGHT);
    private boolean hit = false;
    
    private Sprite spectrum = TowerOfPuzzles.spriteBinder.loadSprite("gui/spectrum.png");
    
    public EntityBullet(int x, int y, Vector3D dir) {
        super(x, y, 1*4, 1*4, EnumEntityType.BULLET);
        direction = dir;
        emitter  = new EntityParticleEmitter(x, y, "bullet.txt");
        emitter.link(this);
        PhysicsEngine.addChannel("bullets");
        PhysicsEngine.addToChannel("bullets", collision);
        super.setShouldSave(false);
    }

    @Override
    public void update() {
        //check for hit
        if(!hit){
            //Move the bullet by the velocity vector
            super.offset(direction.multiplyVector(magnitude));
            if(PhysicsEngine.intersects(collision, "solids")){
                //if it hits a wall stop it
                this.killVelocity();
                hit = true;
            }
            LinkedList<Entity> entities = TowerOfPuzzles.entityManager.getEntitiesOfType(EnumEntityType.MONSTER);
            for(Entity e : entities){
                if(RigidUtils.Collides(e.collision, this.collision)){
                    TowerOfPuzzles.attackManager.attacks.add(new Attack(this.collision, this.magnitude.getX(), this.direction));
                    this.killVelocity();
                    hit = true;
                }
            }
        }

        emitter.offsetToLinked();
        emitter.tick();
        
        //if its off the screen kill it
        if(DistanceCalculator.CalculateDistanceF(x, y, TowerOfPuzzles.cam.x, TowerOfPuzzles.cam.y)>=maxDistance){
            TowerOfPuzzles.entityManager.remove(this);
        }
    }

    @Override
    public void render(Graphics g) {
        emitter.render(g);
    }
    
    private void killVelocity(){
        this.magnitude = new Vector3D(0,0,0);
    }
    
    @Override
    public void onRemove(){
        PhysicsEngine.getChannel("bullets").remove(collision);
    }
}
