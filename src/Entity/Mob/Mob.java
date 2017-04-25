/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob;

import Base.TowerOfPuzzles;
import Base.util.DynamicCollection;
import Base.util.StringUtils;
import Editor.Attribute;
import Entity.Entity;
import Entity.EnumEntityType;
import Entity.Mob.AiComponets.AIComponent;
import Entity.Mob.AiComponets.EnumAIComponent;
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
public class Mob extends Entity{
    
    public int health = 1;
    
    private Vector3D velocity = new Vector3D(0,0,0);
    public Vector3D acceleration = new Vector3D(0,0,0);
    
    private DynamicCollection<AIComponent> components = new DynamicCollection<AIComponent>();
    
    private boolean inAir = true;
    
    private RigidBody feet;
    
    public Mob(int x, int y, String prefab) {
        super(x, y, TileConstants.SCALE, TileConstants.SCALE, EnumEntityType.MONSTER);
        String[] aiComponents = StringUtils.loadData(TowerOfPuzzles.Path+TowerOfPuzzles.PrefabPath+"monsters/"+prefab);
        for(int i = 0; i < aiComponents.length; i++){
            AIComponent component = EnumAIComponent.valueOf(aiComponents[i].split("\\{")[0]).generate(this, (aiComponents[i].split("\\{")[1]).replaceAll("\\}", "").split(","));
            this.addComponent(component);
        }
        feet = PrebuiltBodies.quad(new Point2D(super.x, super.y+(super.height/2)-4), (int) (super.width*(3.0f/4.0f)), 4);
    }

    @Override
    public void update(){
        components.tick();
        
        //update and execute all entity Actions
        for(AIComponent component : components.getCollection(AIComponent.class)){
            component.tick();
        }
        
        //calculate offset
        this.velocity.addVector(this.acceleration);
        this.acceleration = new Vector3D(0,0,0);
        this.inAir = true;
        //if starts in ground move up
        while(PhysicsEngine.intersects(this.collision, "solids")||PhysicsEngine.intersects(feet, "floor")){
            y-=1;
            this.velocity.setVelY(0);
            collision.setPosition(x, y, 0);
            feet.setPosition(x, y+(super.height/2)-4, 0);
            //        infinate friction
            this.inAir = false;
        }
        
        //Floor and Stair Code
        if(!PhysicsEngine.intersects(collision, "ladder")){
            if(!PhysicsEngine.intersects(feet, "solids")){
                if(PhysicsEngine.intersects(feet, "floor")){
                    while(PhysicsEngine.intersects(feet, "floor")){
                        RigidUtils.Move(new Vector3D(0,-1,0), collision);
                        RigidUtils.Move(new Vector3D(0,-1,0), feet);
                    }
                    RigidUtils.Move(new Vector3D(0,+1,0), collision);
                    RigidUtils.Move(new Vector3D(0,+1,0), feet);
                    velocity.setVelY(0);
                }
            }
        }
        
        
        //Move the mob while they are not in solid X dir
        for (int i = 0; i < Math.abs(velocity.getX()); i++) {
            int dir = (int) (velocity.getX() / Math.abs(velocity.getX()));
            x+=dir;
            collision.setPosition(x, y, 0);
            if(PhysicsEngine.intersects(this.collision, "solids")){
                this.velocity.setVelX(0);
                x-=dir;
            }
        }
        
        //Move the mob while they are not in solid in the Y dir
        for (int i = 0; i < Math.abs(velocity.getY()); i++) {
            int dir = (int) (velocity.getY() / Math.abs(velocity.getY()));
            y+=dir;
            collision.setPosition(x, y, 0);
            feet.setPosition(x, y+(super.height/2)-4, 0);
            if(PhysicsEngine.intersects(this.collision, "solids")||
               PhysicsEngine.intersects(this.feet, "floor")){
                this.velocity.setVelY(0);
                y-=dir;
                //infinate friction
                this.velocity.setVelY(0);
                this.inAir = false;
            }
        }
        
        //Synch the position
        collision.setPosition(x, y, 0);
        feet.setPosition(x, y+(super.height/2)-4, 0);
        
        //is dead
        if(health <=0){
            this.onKilled();
            TowerOfPuzzles.entityManager.remove(this);
        }
        
        this.velocity.setVelX(0);
    }
    
    public boolean hasComponent(EnumAIComponent componentType){
        
        for(AIComponent component : components.getCollection(AIComponent.class)){
            if(component.getType().equals(componentType)){
                return true;
            }
        }
        
        return false;
    }
    
    public AIComponent getComponent(EnumAIComponent componentType){
        
        for(AIComponent component : components.getCollection(AIComponent.class)){
            if(component.getType().equals(componentType)){
                return component;
            }
        }
        
        return null;
    }
            
    public void addComponent(AIComponent component){
        this.components.add(component);
        for(Attribute attribute: component.getAttributes()){
            super.addAttribute(attribute);
        }
    }
    
    public void removeComponent(AIComponent component){
        this.components.remove(component);
        for(Attribute attribute: component.getAttributes()){
            super.removeAttribute(attribute);
        }
    }
    
    @Override
    public void render(Graphics g){
        for(AIComponent component : components.getCollection(AIComponent.class)){
            component.render(g);
        }
        if(PhysicsEngine.intersects(feet, "solids")){
            feet.setColor(Color.RED);
        }else{
            feet.setColor(Color.GREEN);
        }
        RigidUtils.RenderWireframe(feet, g);
    }
    public void onKilled(){
        return;
    }
    
    public int componentLength(){
        return this.components.getLength();
    }
    
    public Vector3D getLookingDir(){
        return new Vector3D(TowerOfPuzzles.player.getX()-this.x,TowerOfPuzzles.player.getY(),0);
    }
    
    public boolean isInAir(){
        return this.inAir;
    }
}
