/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Editor.Attribute;
import Graphics.Sprite;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import World.Room;
import World.Tiles.TileConstants;
import World.WorldManager;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityWarp extends Entity{

    private Attribute<String> level = new Attribute<String>("Warp Level:", "");
    private Attribute<Integer> width = new Attribute<Integer>("Width in Tiles:", 0);
    private Attribute<Integer> height = new Attribute<Integer>("Height in Tiles:", 0);
    private Attribute<Boolean> WarpRelative = new Attribute<Boolean>("Warp Relative:", true);
    private Attribute<Integer> destinationX = new Attribute<Integer>("Destination X:", 0);
    private Attribute<Integer> destinationY = new Attribute<Integer>("Destination Y:", 0);
    private Attribute<Boolean> requireInteraction = new Attribute<Boolean>("Require Interaction:", false);
    
    private int countDown = 300;
    private final int maxCountDown = countDown;
    
    private boolean teleportedOutofLevel = false;
    
    private EntityParticleEmitter epe;
    private EntityBlackHole hole1;
    private EntityBlackHole hole2;
    
    public EntityWarp(int x, int y, int width, int height, String level, boolean warpRelative, int destX, int destY, boolean requireInteraction, String id) {
        super(x, y, width*TileConstants.SCALE, height*TileConstants.SCALE, EnumEntityType.WARP);
        if(id.isEmpty()){
            id = "id:"+Math.random();
        }
        super.setID(id);
        this.level.setData(level);
        this.WarpRelative.setData(warpRelative);
        this.destinationX.setData(destX);
        this.destinationY.setData(destY);
        this.width.setData(width);
        this.height.setData(height);
        this.requireInteraction.setData(requireInteraction);
        
        super.addAttribute(this.level);
        super.addAttribute(this.width);
        super.addAttribute(this.height);
        super.addAttribute(this.WarpRelative);
        super.addAttribute(this.destinationX);
        super.addAttribute(this.destinationY);
        super.addAttribute(this.requireInteraction);
        
        super.setNodes(new Node[]{new Node(0,0)});
        
        epe = new EntityParticleEmitter((int)super.x, (int)super.y, "default.txt");
        epe.link(this);
        epe.setLifespan(countDown);
        epe.setShouldSave(false);
        epe.setCanAdd(false);
        
        hole1 = new EntityBlackHole((int) (super.x-(TileConstants.SCALE * 1.5f)), (int) (super.y-(TileConstants.SCALE * 1.5f)), TileConstants.SCALE * 4);
        hole2 = new EntityBlackHole((int) (super.x+(TileConstants.SCALE * 1.5f)), (int) (super.y+(TileConstants.SCALE * 1.5f)), TileConstants.SCALE * 4);
        
        hole1.setShouldSave(false);
        hole2.setShouldSave(false);
    }   

    @Override
    public void update() {
        if(!teleportedOutofLevel){
            epe.tick();
            if(super.getNodes()[0].isPowered()){
                if(countDown==maxCountDown){
                    warp();
                }
            }

            if(countDown==maxCountDown){
                if(this.requireInteraction.getData()){
                    if(Keyboard.E){
                        if(PhysicsEngine.intersects(collision, "player")){
                            warp();
                        }
                    }
                }else{
                    if(PhysicsEngine.intersects(collision, "player")){
                        warp();
                    }
                }
            }else{
                if(countDown>0){
                    countDown--;
                }
                if(countDown==0){
                    //Send actual level
                    if(!this.level.getData().isEmpty()){
                        TowerOfPuzzles.fadeManager.fade(Color.WHITE, -30);
                    }else{
                        TowerOfPuzzles.fadeManager.fade(Color.BLACK, -30);
                    }
                    TowerOfPuzzles.entityManager.remove(hole1);
                    TowerOfPuzzles.entityManager.remove(hole2);
                    loop:{
                        if(this.WarpRelative.getData()){
                            TowerOfPuzzles.player.movePlayerTo((int)x+(this.destinationX.getData()*TileConstants.SCALE), (int)y+(this.destinationY.getData()*TileConstants.SCALE));
                            break loop;
                        }else{
                            if(this.level.getData().isEmpty()){
                                TowerOfPuzzles.player.movePlayerTo((this.destinationX.getData()*TileConstants.SCALE), this.destinationY.getData()*TileConstants.SCALE);
                            }else{
                                this.teleportedOutofLevel = true;
                                TowerOfPuzzles.entityManager.remove(this);
                                TowerOfPuzzles.worldManager.clearAllRooms();
                                WorldManager.addRoom(new Room(0,0,this.level.getData()));
                            }
                        }
                    }
                    TowerOfPuzzles.player.setCanMove(true);
                    resetCountDown();
                }
            }
        }
        
    }
    
    private void warp(){
        //Trigger level
        if(!this.level.getData().isEmpty()){
            Sprite sprite = TowerOfPuzzles.spriteBinder.loadSprite("player/right.png");
            epe.dissipateSprite((int)(TowerOfPuzzles.player.getX()-((sprite.pWidth*4)/2)), (int)(TowerOfPuzzles.player.getY()-((sprite.pHeight*4)/2))+4, sprite);           
            TowerOfPuzzles.entityManager.add(hole1);
            TowerOfPuzzles.entityManager.add(hole2);
            //Warp sound
        }
        TowerOfPuzzles.fadeManager.fade(Color.BLACK, this.maxCountDown);
        TowerOfPuzzles.player.setCanMove(false);
        countDown--;
    }

    @Override
    public void onAttributeChange(){
        super.collision = PrebuiltBodies.quad(new Point2D(x,y), width.getData()*TileConstants.SCALE, height.getData()*TileConstants.SCALE);
    }
    
    @Override
    public void render(Graphics g) {
        if(TowerOfPuzzles.IS_EDITOR){
            g.setColor(Color.MAGENTA);
            g.drawRect((int)x-(width.getData()*TileConstants.SCALE/2), (int)y-(height.getData()*TileConstants.SCALE/2), width.getData()*TileConstants.SCALE, height.getData()*TileConstants.SCALE);
            g.setColor(Color.CYAN);
            if(this.WarpRelative.getData()){
                g.drawRect((int)x+(this.destinationX.getData()*TileConstants.SCALE)-(TileConstants.SCALE/2), (int)y+(this.destinationY.getData()*TileConstants.SCALE)-(TileConstants.SCALE/2), TileConstants.SCALE, TileConstants.SCALE);
            }else{
                g.drawRect((this.destinationX.getData()*TileConstants.SCALE)-(TileConstants.SCALE/2), (this.destinationY.getData()*TileConstants.SCALE)-(TileConstants.SCALE/2), TileConstants.SCALE, TileConstants.SCALE);
            }
        }
        epe.render(g);
    }
    
    public void resetCountDown(){
        this.countDown = this.maxCountDown;
    }
    
    @Override
    public String save(){
        return super.getType()+"{"+super.x+","+super.y+","+this.width.getData()+","+this.height.getData()+","+this.level.getData()+","+this.WarpRelative.getData()+","+this.destinationX.getData()+","+this.destinationY.getData()+","+this.requireInteraction.getData()+","+super.getID()+"}";
    }
    
    @Override
    public void onAdd(){

    }
    
    @Override
    public void onRemove(){
        TowerOfPuzzles.entityManager.remove(hole1);
        TowerOfPuzzles.entityManager.remove(hole2);
    }
}
