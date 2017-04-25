/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.RigidUtils;
import Physics.Vector3D;
import World.Tiles.TileConstants;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
class EntityWaterDrop extends Entity{

    float volume = 0;
    RigidBody water;
    
    float vecY = 0;
    float vecX = 0;
    
    float friction = 1.25f;
    
    EntityParticleEmitter pe1;
    
    public EntityWaterDrop(int x, int y) {
        super(x, y, 0, 0, EnumEntityType.WATER_DROP);
        PhysicsEngine.addChannel("waterDrop");
        PhysicsEngine.activeChannel = "waterDrop";
        water = PrebuiltBodies.quad(new Point2D(super.x, super.y), TileConstants.SCALE/3);
        water.setColor(new Color(0,255,255,128));
        PhysicsEngine.addToActiveChannel(water);
        pe1 = new EntityParticleEmitter((int)super.x,(int)super.y,"water").setNumParticles(4).setBoundXBottom(-2f).setBoundXTop(2f).setBoundYBottom(-3f).setBoundYTop(3f);
    }

    @Override
    public void update() {
        loop:{
            for(int i = 0; i < PhysicsEngine.getChannel("waterDrop").collisons.length; i++){
                boolean exists = false;
                if(PhysicsEngine.getChannel("waterDrop").collisons[i].equals(water)){
                    exists = true;
                }
                if(exists){
                    break loop;
                }
            }
            //if it does not exist
            TowerOfPuzzles.entityManager.remove(this);
            return;
        }
        
        vecY += TowerOfPuzzles.GRAVITY;
        if(vecX>0){
            
        }
        for(RigidBody bod:PhysicsEngine.getChannel("waterDrop").collisons){
            if(!bod.equals(water)){
                if(RigidUtils.Collides(bod, water)){

                    vecX=(float)((Math.random() * 12.0f)-6.0f);
//                    bod.
                }
            }
        }
        for(RigidBody bod:PhysicsEngine.getChannel("solids").collisons){
            if(RigidUtils.Collides(bod, water)){
                vecY=-1;
            }
        }
        y+=vecY;
        x+=vecX;
        RigidUtils.MoveTo(new Vector3D(x, y, 0), water);
        pe1.x = x;
        pe1.y = y;
        pe1.tick();
    }

    @Override
    public void render(Graphics g) {
//        RigidUtils.Render(water, g);
        pe1.render(g);
    }
    
}
