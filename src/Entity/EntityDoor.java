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
import Graphics.SpriteBinder;
import Physics.PhysicsEngine;
import Sound.SoundManager;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityDoor extends Entity{

    Sprite bg;
    Sprite[] openAnimation;
    
    Sprite lock;
    Sprite lock_powered;
    Sprite lockArm;
    
    Attribute<Boolean> unlocked = new Attribute<Boolean>("Unlocked:", false);
    boolean open = false;
    boolean lastOpen = open;
    private final int countDown = 16;
    private int ticks = countDown;
    
    private final int countDownLock = 120;
    private int lockTicks = countDownLock;
    
    public EntityDoor(int x, int y, boolean unlocked) {
        super(x, y, 48 * 4, 64 * 4, EnumEntityType.DOOR);
        
        this.unlocked.setData(unlocked);
        
        bg = TowerOfPuzzles.spriteBinder.loadSprite("door/door.png");
        
        lock = TowerOfPuzzles.spriteBinder.loadSprite("door/lock.png");
        lock_powered = TowerOfPuzzles.spriteBinder.loadSprite("door/lock_powered.png");
        lockArm = TowerOfPuzzles.spriteBinder.loadSprite("door/lockArm.png");
        
        openAnimation = new Sprite[]{
            TowerOfPuzzles.spriteBinder.loadSprite("door/door_-1.png"),
            TowerOfPuzzles.spriteBinder.loadSprite("door/door_0.png"),
            TowerOfPuzzles.spriteBinder.loadSprite("door/door_1.png"),
            TowerOfPuzzles.spriteBinder.loadSprite("door/door_2.png"),
            TowerOfPuzzles.spriteBinder.loadSprite("door/door_3.png"),
        };
        
        Node[] nodes = new Node[]{
            new Node((18 * 4),(12 * 4)),
            new Node(0,-(TileConstants.SCALE+(4*4)-2)),
        };
        
        super.setNodes(nodes);
        
        super.addAttribute(this.unlocked);
        super.getNodes()[0].setPowered(this.unlocked.getData());
    }
    
    @Override
    public void onAttributeChange(){
        super.getNodes()[0].setPowered(this.unlocked.getData());
    }

    @Override
    public void update() {
        
        boolean nodeOpen = false;
        
        if(super.getNodes()[0].isPowered()){
            if(lockTicks == countDownLock){
                TowerOfPuzzles.soundManager.playSound("doorUnlock.wav");
            }
            if(lockTicks>0){
                lockTicks--;
            }
        }else{
            this.unlocked.setData(false );
            if(lockTicks<countDownLock){
                lockTicks++;
            }
        }
        
        if(this.unlocked.getData()==true){
            this.lockTicks = 0;
        }
        
        if(lockTicks==0){
            this.unlocked.setData(true);
        }
        
        if(unlocked.getData()){
            if(!open){
                if(PhysicsEngine.intersects(collision, "player")){
                    open = true;
                }
            }else{
                if(!PhysicsEngine.intersects(collision, "player")){
                    open = false;
                }
            }
            if(open){
                if(Keyboard.E){
                    nodeOpen = true;
                }
                if(ticks>0){
                    ticks--;
                }
            }
            if(!open){
                if(ticks<countDown){
                    ticks++;
                }
            }
            if(lastOpen!=open){
                if(open){
                    //open door
                    TowerOfPuzzles.soundManager.playSound("doorOpen.wav");
                }else{
                    //close door
                    TowerOfPuzzles.soundManager.playSound("doorClose.wav");
                }
            }
            lastOpen = open;
        }
        super.getNodes()[1].setPowered(nodeOpen);
    }

    @Override
    public void render(Graphics g) {
        bg.render(x, y, width, height, g);
        int doorStage = (int)(Math.floor((float)((float)ticks/(float)countDown)*(float)(openAnimation.length-1)));
        openAnimation[doorStage].render(x, y, width, height, g);
        g.setClip((int)x, (int)y, (18 * 4), 18*4);
        lockArm.render(x+(18 * 4)+(9 * 4)-(int)(((float)lockTicks/(float)countDownLock)*(18 * 4)), y+(12 * 4), (18 * 4), (3 * 4), g);
        g.setClip(null);
        if(this.lockTicks!=countDownLock){
            lock_powered.render(x+(18 * 4), y+(12 * 4), 6*4, 4*4, g);
        }else{
            lock.render(x+(18 * 4), y+(12 * 4), 6*4, 4*4, g);
        }
        super.drawNodes(g);
    }
    
    @Override
    public String save(){
        return super.getType()+"{"+super.x+","+super.y+","+this.unlocked.getData()+","+super.getID()+"}";
    }
    
}
