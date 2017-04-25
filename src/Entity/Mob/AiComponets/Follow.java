/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Base.TowerOfPuzzles;
import Base.util.DistanceCalculator;
import Editor.Attribute;
import Entity.Entity;
import Entity.Mob.Mob;
import Physics.PhysicsEngine;
import Physics.Vector3D;
import World.Tiles.TileConstants;

/**
 *
 * @author Bailey
 */
public class Follow extends AIComponent{
    
    private Attribute<Integer> range = new Attribute<Integer>("Range:", TileConstants.SCALE*8);
    private Attribute<Float> speed = new Attribute<Float>("Speed:", 1.0f);
    
    public Follow(Mob mob, int range, float speed) {
        super(mob, EnumAIComponent.FOLLOW);
        this.range.setData(range*TileConstants.SCALE);
        this.speed.setData(speed);
        super.addAttribute(this.range);
        super.addAttribute(this.speed);
    }
    
    @Override
    public void tick() {
        if(!super.mob.isInAir()){
            if(DistanceCalculator.CalculateDistanceF(super.mob.getX(), super.mob.getY(), TowerOfPuzzles.player.getX(), TowerOfPuzzles.player.getY())<range.getData()){
                if(super.mob.getLookingDir().getX()<=0){
                    super.mob.acceleration.increaseVelX(-speed.getData());
                }else{
                    super.mob.acceleration.increaseVelX(speed.getData());
                }
            }
        }
    }

    @Override
    public String extraData() {
        return (this.range.getData()/TileConstants.SCALE)+","+this.speed.getData();
    }
    
}
