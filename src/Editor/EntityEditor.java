 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Graphics.GUI.UI;
import Base.Keyboard;
import Base.MouseInput;
import Base.MousePositionLocator;
import Base.MouseWheel;
import Base.TowerOfPuzzles;
import Entity.Entity;
import Entity.EntityANDGate;
import Entity.EntityBarrel;
import Entity.EntityBed;
import Entity.EntityBlackHole;
import Entity.EntityBook;
import Entity.EntityBookShelf;
import Entity.EntityBuffer;
import Entity.EntityCellularWater;
import Entity.EntityChest;
import Entity.EntityDoor;
import Entity.EntityElevator;
import Entity.EntityFloorLamp;
import Entity.EntityFloorLantern;
import Entity.EntityGate;
import Entity.EntityGrate;
import Entity.EntityItemPedestal;
import Entity.EntityLight;
import Entity.EntityLog;
import Entity.EntityManager;
import Entity.EntityMobSpawner;
import Entity.EntityNotGate;
import Entity.EntityORGate;
import Entity.EntityParticleEmitter;
import Entity.EntityPedestalButton;
import Entity.EntityPositionalAudio;
import Entity.EntityRSLatch;
import Entity.EntityScript;
import Entity.EntitySpawnPlayer;
import Entity.EntitySpawnRoom;
import Entity.EntityTeleportPlayer;
import Entity.EntityTimer;
import Entity.EntityTorch;
import Entity.EntityTree;
import Entity.EntityWarp;
import Entity.EntityWater;
import Entity.EntityWindow;
import Entity.EnumEntityType;
import Entity.Mob.Mob;
import Physics.RigidUtils;
import Physics.Vector3D;
import World.Tiles.TileConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class EntityEditor extends UI{
    
    private final int width = 196;
    private final int height = 20;
    
    private final int numItemShown = 26;
    private final int overhang;
    
    private final int size;
    
    private final Color col1 = new Color(61,122,147);
    private final Color col2 = new Color(98,161,187);
    private final Color col3 = new Color(161,227,255);
    
    private int index = 0;
    private int entityIndex=0;
    
    private final Rectangle[] entities = new Rectangle[EnumEntityType.values().length];
    private final Rectangle collision;
    private final Rectangle leftCollision;
    
    private Entity selected = null;
    private boolean isMoving = false;
    
    public boolean snapToGrid = true;
    
    private ScrollBar bar;
    private ScrollBar attributesBar;
    private ScrollBar entitiesInWorld;
    
    private int offsetAttributes=0;
    
    public EntityEditor(int x, int y){
        super(x, y);
        size = entities.length;
        
        leftCollision  = new Rectangle(TowerOfPuzzles.WIDTH-128-16,y,144,TowerOfPuzzles.HEIGHT-y);
        
        for(int i = 0; i < size; i++){
            entities[i] = new Rectangle(x, y+(height * i), width, height);
        }
        collision = new Rectangle(x, y, width, height*numItemShown);
        overhang = (size-numItemShown)*height;
        
        bar = new ScrollBar(x+width,y,TowerOfPuzzles.HEIGHT-200);
        entitiesInWorld = new ScrollBar(TowerOfPuzzles.WIDTH-128-16,y,TowerOfPuzzles.HEIGHT);
        attributesBar = new ScrollBar(x+width,y+TowerOfPuzzles.HEIGHT-200,154).setHeight(24);
    }
    
    public void tick(){
        bar.tick();
        entitiesInWorld.tick();
        attributesBar.tick();
        
        if(this.selected==null){
            entityIndex = -1;
            offsetAttributes = 0;
        }
        
        if(MousePositionLocator.MouseLocation.intersects(collision)){
            bar.offset(MouseWheel.scrollIndex);
        }
        if(this.selected!=null){
            if(Keyboard.ESC){
                this.selected = null;
                isMoving=false;
                return;
            }
        }
        if(this.selected!=null){
            if(Keyboard.BACKSPACE||Keyboard.DELETE){
                TowerOfPuzzles.entityManager.remove(selected);
                selected = null;
            }
        }
        if(isMoving){
            if(TowerOfPuzzles.mouseInput.IsPressed){
                int pos_x = (MousePositionLocator.MouseLocation.x+(int)TowerOfPuzzles.cam.x);
                int pos_y = (MousePositionLocator.MouseLocation.y+(int)TowerOfPuzzles.cam.y);
                if(Editor.SNAP_TO_GRID){
                        pos_x/=TileConstants.SCALE;
                        pos_y/=TileConstants.SCALE;
                        pos_x*=TileConstants.SCALE;
                        pos_y*=TileConstants.SCALE;
                        if((this.selected.collision.getCollision().getBounds().getWidth()/TileConstants.SCALE)%2==1){
                            pos_x+=TileConstants.SCALE/2;
                        }
                        if((this.selected.collision.getCollision().getBounds().getHeight()/TileConstants.SCALE)%2==1){
                            pos_y+=TileConstants.SCALE/2;
                        }
                    
                    selected.setX(pos_x);
                    selected.setY(pos_y);
                }else{
                    selected.setX(pos_x);
                    selected.setY(pos_y);
                }
                
                RigidUtils.MoveTo(new Vector3D(selected.getX(), selected.getY(), 0), selected.collision);
                selected.onMove();
            }else{
                isMoving = false;
            }
        }
    }
    
    public void render(Graphics g){
        g.setClip(collision);
        for(int i = 0; i < entities.length; i++){
            if(i!=index){
                if(i%2==0){
                    g.setColor(col1);
                }else{
                    g.setColor(col2);
                }
            }else{
                g.setColor(col3);
            }
            g.fillRect(entities[i].x,entities[i].y-(int)((float)overhang*bar.getOffset()),entities[i].width,entities[i].height);
            g.setColor(Color.BLACK);
            g.drawString(EnumEntityType.values()[i].name(), entities[i].x,entities[i].y+entities[i].height-(int)((float)overhang*bar.getOffset()));
        }
        g.setClip(null);
        
        g.setColor(Color.decode("#464646"));
        g.fillRect(x, y+collision.height, width+bar.getCollision().width, TowerOfPuzzles.HEIGHT - collision.height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y+collision.height, width+bar.getCollision().width, TowerOfPuzzles.HEIGHT - collision.height);
        
        if(selected!=null){
            g.translate(-(int)TowerOfPuzzles.cam.x, -(int)TowerOfPuzzles.cam.y);
                RigidUtils.RenderWireframe(selected.collision, g);
            g.translate((int)TowerOfPuzzles.cam.x, (int)TowerOfPuzzles.cam.y);
            
            g.drawRect(collision.x, collision.y+collision.height, collision.width, TowerOfPuzzles.WIDTH - collision.width);
            //Clip
            g.setClip(new Rectangle(collision.x, collision.y+collision.height,collision.width,TowerOfPuzzles.HEIGHT-collision.height));
            for(int i = 0; i < selected.getAttributes().length; i++){
                if(i%2==0){
                    g.setColor(Color.decode("#ededed"));
                }else{
                    g.setColor(Color.decode("#bebebe"));
                }
                g.fillRect(collision.x, collision.y+collision.height+(i*height)-(int)((float)((selected.getAttributes().length*height)-154)*attributesBar.getOffset()), width, height);
                if(MousePositionLocator.MouseLocation.intersects(new Rectangle(collision.x, collision.y+collision.height+(i*height)-(int)((float)((selected.getAttributes().length*height)-154)*attributesBar.getOffset()), width, height))){
                    g.setColor(Color.RED);
                    g.drawRect(collision.x, collision.y+collision.height+(i*height)-(int)((float)((selected.getAttributes().length*height)-154)*attributesBar.getOffset()), width-1, height-1);
                    g.setClip(null);
                    selected.getAttributes()[i].highlightRender(MousePositionLocator.MouseLocation.x+this.width+(16 * 4), MousePositionLocator.MouseLocation.y, g);
                    g.setClip(new Rectangle(collision.x, collision.y+collision.height,collision.width,TowerOfPuzzles.HEIGHT-collision.height));
                }
            }
            g.setColor(Color.BLACK);
            int attributeIndex = 0;
            for(Attribute a : selected.getAttributes()){
                attributeIndex++;
                a.render(x, y+collision.height - height/2+((attributeIndex * Math.max(g.getFont().getSize(), height))+4)-(int)((float)((selected.getAttributes().length*height)-154)*attributesBar.getOffset()), g);
            }
            g.setClip(null);
        }
        bar.render(g);   
        entitiesInWorld.render(g);
        attributesBar.render(g);
        //Entities In World
        int ex = TowerOfPuzzles.WIDTH-128;
        int ey = y;
        for(int i = 0; i < EntityManager.getEntities().length; i++){
            if(i!=entityIndex){
                if(i%2==0){
                    g.setColor(col1);
                }else{
                    g.setColor(col2);
                }
            }else{
                g.setColor(col3);
            }
            
            int offsetIndex = (EntityManager.getEntities().length * height)-(TowerOfPuzzles.HEIGHT-y);
            
            g.fillRect(ex,ey+(height*i)-(int)(((float)offsetIndex)*entitiesInWorld.getOffset()),width,128);
            g.setColor(Color.BLACK);
            g.drawString(EntityManager.getEntities()[i].getType().name(), ex,ey+height+(height*i)-(int)(((float)offsetIndex)*entitiesInWorld.getOffset()));
        }
    }
    
    public void onClick(Rectangle rect){
        if(rect.intersects(bar.getCollision())||rect.intersects(entitiesInWorld.getCollision())||rect.intersects(attributesBar.getCollision())){
            bar.onClick(rect);
            entitiesInWorld.onClick(rect);
            attributesBar.onClick(rect);
            return;
        }
        if(selected == null){
            if(!rect.intersects(collision) && !rect.intersects(Editor.MENU_BAR) && !rect.intersects(leftCollision)){
                Entity e = EntityManager.getEntityFromPoint((MousePositionLocator.MouseLocation.x+(int)TowerOfPuzzles.cam.x), (MousePositionLocator.MouseLocation.y+(int)TowerOfPuzzles.cam.y));
                this.selected = e;
                for(int i = 0; i < EntityManager.getEntities().length; i++){
            		if(EntityManager.getEntities()[i].equals(selected)){
            			entityIndex = i;
            		}
            	}
                if(e==null){
                    String data = 
                            (MousePositionLocator.MouseLocation.x+(int)TowerOfPuzzles.cam.x)+","+
                            +(MousePositionLocator.MouseLocation.y+(int)TowerOfPuzzles.cam.y)+","+
                            ("id:"+Math.random());
                    Entity e2 = this.generate(EnumEntityType.values()[index], data);
                    if(e2 != null){
                        TowerOfPuzzles.entityManager.add(e2);
                    }
                }
            }else{
                for(int i = 0; i < entities.length; i++){
                    if(rect.intersects(new Rectangle(entities[i].x, entities[i].y-(int)((float)overhang*bar.getOffset()), entities[i].width, entities[i].height))){
                        index = i;
                        return;
                    }
                }
                for(int i = 0; i < EntityManager.getEntities().length; i++){
                    if(rect.intersects(new Rectangle(leftCollision.x, leftCollision.y+(i*height)-(int)(((float)(EntityManager.getEntities().length * height)-(TowerOfPuzzles.HEIGHT-y))*entitiesInWorld.getOffset()), width, height))){
                    	//Set Entity
                    	entityIndex = i;
                    	//Replace Selected
                    	selected = EntityManager.getEntities()[i];
                    	//Camera Translation
                    	TowerOfPuzzles.cam.gotoPos(selected.getX(), selected.getY(), 20);
                        return;
                    }
                }
            }
        }else{
            //when entity is selected and right click happens in the world
            if(rect.intersects(new Rectangle(collision.x, collision.y+collision.height, collision.width, TowerOfPuzzles.WIDTH - collision.width))){
                for(int i = 0; i < selected.getAttributes().length; i++){
                    if(rect.intersects(new Rectangle(collision.x, collision.y+collision.height+(i*height)-(int)((float)((selected.getAttributes().length*height)-154)*attributesBar.getOffset()), width, height))){
                        selected.getAttributes()[i].userChangeData();
                        selected.onAttributeChange();
                        return;
                    }
                }
            }
            Entity e = EntityManager.getEntityFromPoint((MousePositionLocator.MouseLocation.x+(int)TowerOfPuzzles.cam.x), (MousePositionLocator.MouseLocation.y+(int)TowerOfPuzzles.cam.y));
            if(e!=null){
                if(e.equals(selected)){
                    isMoving = true;
                }else{
                    selected = e;
                    for(int i = 0; i < EntityManager.getEntities().length; i++){
                		if(EntityManager.getEntities()[i].equals(selected)){
                			entityIndex = i;
                		}
                	}
                }
            }else{
                selected = null;
            }
            
        }

    }
    
    
    public static Entity generate(EnumEntityType eet, String in){
        String[] data = in.split(",");
        switch(eet){
            case TORCH:
                return new EntityTorch((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case PEDESTAL:
                return new EntityPedestalButton((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case TIMER:
                return new EntityTimer((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),60).setID(data[2]);
            case DOOR:
                return new EntityDoor((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),false).setID(data[2]);
            case FLOOR_LAMP:
                return new EntityFloorLamp((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),255,0,0).setID(data[2]);
            case FLOOR_LANTERN:
                return new EntityFloorLantern((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),0,0,0).setID(data[2]);
            case GRATE:
                return new EntityGrate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case WIRE:
//                return new EntityWire(data[0],(int)Float.parseFloat(data[1]),data[2],(int)Float.parseFloat(data[3]));
            case WATER:
                return new EntityWater((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),4,3);
            case LOG:
                return new EntityLog((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),3);
            case SPAWN_POINT:
                return new EntitySpawnPlayer((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]));
            case RS_LATCH:
                return new EntityRSLatch((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case NOT_GATE:
                return new EntityNotGate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case OR_GATE:
                return new EntityORGate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case AND_GATE:
                return new EntityANDGate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case GATE:
                return new EntityGate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case BOOK:
                return new EntityBook((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]));
            case LIGHT:
                return new EntityLight((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), TileConstants.SCALE*2, (int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)).setID(data[2]);
            case ELEVATOR:
                return new EntityElevator((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case TELEPORTER:
                return new EntityTeleportPlayer((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case BUFFER:
                return new EntityBuffer((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),15).setID(data[2]);
            case SPAWN_ROOM:
                return new EntitySpawnRoom((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),"start.txt").setID(data[2]);
            case BOOK_SHELF:
                return new EntityBookShelf((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),false).setID(data[2]);
            case BLACK_HOLE:
                return new EntityBlackHole((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),TileConstants.SCALE*3).setID(data[2]);
            case WINDOW:
                return new EntityWindow((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]));
            case CHEST:
                return new EntityChest((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), false, "null;null;null;");
            case BARREL:
                return new EntityBarrel((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),"null;");
            case PARTICLE_EMITTER:
                EntityParticleEmitter pe;
                pe = new EntityParticleEmitter((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),"torch");
                return pe;
            case WARP:
                return new EntityWarp((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),1,1,"", false, 0,0, false, "a");
            case BED:
                return new EntityBed((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]));
            case TREE:
                return new EntityTree((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),0);
            case SCRIPT:
                return new EntityScript("");    
            case PEDESTAL_ITEM:
                return new EntityItemPedestal((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), "");
            case MONSTER:
                return new Mob((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), "mermaid.txt");
            case WATER_CELLS:
                return new EntityCellularWater((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]));
            case MOB_SPAWNER:
                return new EntityMobSpawner((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), "crab.txt", 1).setID("id:"+Math.random());
            case POSITIONAL_AUDIO:
                return new EntityPositionalAudio((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), "drip.wav", 1200, true);
        }
        System.err.println("Entity:"+eet.name()+" not recognised."); 
        return null;
    }
}
