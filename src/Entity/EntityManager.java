/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Engine;
import Base.TowerOfPuzzles;
import Base.util.StringUtils;
import Editor.EntityEditor;
import Physics.RigidBody;
import Physics.RigidUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import javax.script.ScriptEngine;

/**
 *
 * @author Bailey
 */
public class EntityManager extends Engine{
    private static Entity[] entities = new Entity[]{};
    private static LinkedList<Entity> toAdd = new LinkedList<Entity>();
    private static LinkedList<Entity> toRemove = new LinkedList<Entity>();
    
    public EntityManager(){
        super("EntityManager");
    }
    
    private void updateEntities(){
        //only run if need to
        if(toRemove.size()>0 || toAdd.size()>0){
            LinkedList<Entity> newEntities = new LinkedList<Entity>();
            //remove all lights flagged to remove
            for(int i = 0; i < this.entities.length; i++){
                if(toRemove.contains(this.entities[i])){
                    this.entities[i].onRemove();
                }else{
                    newEntities.add(this.entities[i]);
                }
            }
            //clear the buffer
            toRemove.clear();
            
            //add all new lights
            for (int i = 0; i < toAdd.size();){
                Entity entity = toAdd.get(i);
                entity.onAdd();
                newEntities.add(entity);
                i++;
            }
            //clear that buffer
            toAdd.clear();
            //build new light array
            Entity[] out = new Entity[newEntities.size()];
            for(int i = 0; i < newEntities.size(); i++){
                out[i] = newEntities.get(i);
            }
            //set it
            this.entities = out;
        }
    }
    
    public void tick(){
        updateEntities();
        for(Entity e:this.entities){
            e.tick();
        }
    }
    
    public void render(Graphics g){
        updateEntities();
        for(Entity e:this.entities){
            if(e instanceof EntityWater){;}else{
                if(e instanceof EntityWire){
                    e.render(g); 
                }else{
                    if(!e.getAlwaysRender()){
                        if(RigidUtils.Collides(new Rectangle((int)TowerOfPuzzles.cam.x,(int)TowerOfPuzzles.cam.y,TowerOfPuzzles.WIDTH,TowerOfPuzzles.HEIGHT), e.collision)){
                            e.render(g); 
                        }
                    }else{
                        e.render(g); 
                    }
                }
//                RigidUtils.RenderWireframe(e.collision, g);
            }
        }
    }
    
    public void renderWater(Graphics g){
        updateEntities();
        for(Entity e:this.entities){
            if(e instanceof EntityWater){
                e.render(g);
            }
        }
    }
    
    public static int size(){
        return entities.length;
    }
    
    public void add(Entity e){
        toAdd.add(e);
    }
    
    public void remove(Entity e){
        toRemove.add(e);
    }
    
    public static Entity CreateEntity(EnumEntityType entity, String data, String extraData){
        Entity out;
        try{
            out = entity.generate(data, extraData);
        }catch(Exception e){
            System.err.println("Malformed Save for:"+entity+":"+data);
            out = EntityEditor.generate(entity, data);
        }
        return out;
    }
    
    public Entity CreateEntity(String entity, String data, String extraData){
        Entity out;
        try{
            out = EnumEntityType.valueOf(entity).generate(data, extraData);
        }catch(Exception e){
            System.err.println("Malformed Save for:"+entity+":"+data);
            out = EntityEditor.generate(EnumEntityType.valueOf(entity), data);
        }
        return out;
    }
    
    
    public Entity getFromID(String id){
        for(Entity e:entities){
            if(e.getID().equals(id)){
                return e;
            }
        }
        for(Entity e:toAdd){
            if(e.getID().equals(id)){
                return e;
            }
        }
        return null;
    }
    
    public static Entity getEntityFromCollision(RigidBody bod){
        for(Entity e : entities){
            if(e.collision != null){
                if(e.collision.equals(bod)){
                    return e;
                }
            }
        }
        return null;
    }
    
    public static Entity getEntityFromPoint(int x, int y){
        Rectangle rect = new Rectangle(x, y, 1, 1);
        for(Entity e : entities){
            if(e.collision != null){
                if(RigidUtils.Collides(rect, e.collision)){
                    return e;
                }
            }
        }
        return null;
    }
    
    public static String[] genSaveData(){
        String[] out = new String[]{""};
        for(Entity e:entities){
            if(e.getShouldSave()){
                out = StringUtils.addLine(out, e.save());
            }
        }
        return out;
    }
    
    public static Entity[] getEntities(){
        return entities;
    }
    
    public LinkedList<Entity> getEntitiesOfType(EnumEntityType eet){
        LinkedList<Entity> e = new LinkedList<Entity>();
        for(int i = 0; i < EntityManager.entities.length; i++){
            if(EntityManager.entities[i].getType().equals(eet)){
                e.add(EntityManager.entities[i]);
            }
        }
        return e;
    }
    
    public static void clearAllEnttites(){
        for(Entity e: entities){
            if(e.getType()!=EnumEntityType.PLAYER){
                toRemove.add(e);
            }
        }
    }
    
    public Entity cast(Entity base){
        switch(base.getType()){
            case PEDESTAL_ITEM:
                return (EntityItemPedestal)base;
            default:
                return null;
        }
    }

    @Override
    public void init() {
        
    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(),this);
    }

    @Override
    public void onShutdown() {
        
    }
}
