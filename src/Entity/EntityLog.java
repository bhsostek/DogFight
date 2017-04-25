/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.RigidUtils;
import Physics.Vector3D;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityLog extends Entity{
    
    RigidBody solid;
    
    float dirY = 0;
    final float buoyancy = 0.025f;
    
    final float terminalVelocity = TileConstants.SCALE*2;
    
    Attribute<Integer> width = new Attribute<Integer>("Log Width:", 0);
    
    Sprite start;
    Sprite mid;
    Sprite end;
    
    boolean playerOnLog = false;
    
    public EntityLog(int x, int y, int width) {
        super(x, y, width*TileConstants.SCALE, (TileConstants.SCALE/4)+(4 * 4), EnumEntityType.LOG);
        this.width.setData(width);
        solid = PrebuiltBodies.quad(new Point2D(x, y), this.width.getData()*TileConstants.SCALE, TileConstants.SCALE/4);
        PhysicsEngine.activeChannel = "solids";
        PhysicsEngine.addToActiveChannel(solid);
        PhysicsEngine.addChannel("log");
        PhysicsEngine.activeChannel = "log";
        PhysicsEngine.addToActiveChannel(super.collision);
        
        //Sprites
        start = TowerOfPuzzles.spriteBinder.loadSprite("entity/log/logStart.png");
        mid = TowerOfPuzzles.spriteBinder.loadSprite("entity/log/logMiddle.png");
        end = TowerOfPuzzles.spriteBinder.loadSprite("entity/log/logEnd.png");
        
        super.addAttribute(this.width);
    }
    
    @Override
    public void onRemove(){
        PhysicsEngine.getChannel("log").remove(super.collision);
        PhysicsEngine.getChannel("solids").remove(solid);
    }

    @Override
    public void update() {
        
        if(playerOnLog == false){
            if(PhysicsEngine.intersects(super.collision, "feet")){
                playerOnLog = true;
                this.dirY += TowerOfPuzzles.player.getDownwardVel();
            }
        }else{
            if(!PhysicsEngine.intersects(super.collision, "feet")){
                playerOnLog = false;
            }
        }
        
        if(PhysicsEngine.intersects(super.collision, "water")){
            if(dirY>0){
                //drastically reduces acceleration
                dirY/=1.25;
            }
             dirY-=(this.buoyancy);
        }else{
            dirY+=TowerOfPuzzles.GRAVITY;
        }
        


        if(dirY > terminalVelocity){
            dirY = terminalVelocity;
        }
        
        for(int i = 0; i < Math.abs(dirY); i++){
            this.y += (int)(dirY/Math.abs(dirY));
            RigidUtils.MoveTo(new Vector3D(x, y-(4*4), 0), super.collision);
            RigidUtils.MoveTo(new Vector3D(x, y, 0), solid);
            for(RigidBody bod : PhysicsEngine.getChannel("solids").collisons){
                if(bod != this.solid){
                    if(RigidUtils.Collides(super.collision, bod)){
                        this.y-=1;
                        this.dirY = 0;
                        RigidUtils.MoveTo(new Vector3D(x, y-(4*4), 0), super.collision);
                        RigidUtils.MoveTo(new Vector3D(x, y, 0), solid);
                        break;
                    }
                }
            }
            if(!PhysicsEngine.intersects(super.collision, "water")){
                if(this.dirY < 0){
                    this.y+=1;
                    RigidUtils.MoveTo(new Vector3D(x, y-(4*4), 0), super.collision);
                    RigidUtils.MoveTo(new Vector3D(x, y, 0), solid);
                    break;
                }
            }
        }

    }
    
    @Override
    public void onMove(){
        this.dirY = 0;
    }
    
    @Override
    public void onAttributeChange(){
        PhysicsEngine.getChannel("solids").remove(solid);
        solid = PrebuiltBodies.quad(new Point2D(x, y), this.width.getData()*TileConstants.SCALE, TileConstants.SCALE/4);
        PhysicsEngine.addToChannel("solids", solid);
        
        PhysicsEngine.getChannel("log").remove(super.collision);
        super.collision = PrebuiltBodies.quad(new Point2D(x,y), width.getData()*TileConstants.SCALE, (TileConstants.SCALE/4)+(4 * 4));
        PhysicsEngine.addToChannel("log", super.collision);
    }

    @Override
    public void render(Graphics g) {
//        if(this.playerOnLog){
//            RigidUtils.Render(super.collision, g);
//        }
//        RigidUtils.Render(solid, g);
        start.render(x - (width.getData()*TileConstants.SCALE/2)-(4*4), y, (8 * 4), (6 * 4), g);
        for(int i = 0; i < width.getData(); i++){
            mid.render((x - (width.getData()*TileConstants.SCALE/2)) + (TileConstants.SCALE * i) + (TileConstants.SCALE/2) , y, (16 * 4), (6 * 4), g);
        }
        end.render(x + (width.getData()*TileConstants.SCALE/2)+(4*4), y, (8 * 4), (6 * 4), g);
    }
    
    public float getDownwardAcceleration(){
        return this.dirY;
    }
    
    @Override
    public String save(){
        return super.getType()+"{"+super.x+","+super.y+","+this.width.getData()+"}";
    }
    
}
