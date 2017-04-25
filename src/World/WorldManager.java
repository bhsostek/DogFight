/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Base.Engine;
import Base.util.DynamicCollection;
import Physics.RigidBody;
import Physics.RigidUtils;
import ScriptingEngine.ScriptingEngine;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.script.ScriptEngine;

/**
 *
 * @author Bailey
 */
public class WorldManager extends Engine{
    private static Room[] rooms = new Room[]{};
    private static LinkedList<Room> toAdd = new LinkedList<Room>();
    private static LinkedList<Room> toRemove = new LinkedList<Room>();
    

    public WorldManager(){
        super("WorldManager");
        //create the collision channel
        Physics.PhysicsEngine.addChannel("solids");

    }
    
    
    private static void updateCollection(){
        //only run if need to
        if(toRemove.size()>0 || toAdd.size()>0){
            LinkedList<Room> newCollection = new LinkedList<Room>();
            //remove all lights flagged to remove
            for(int i = 0; i < rooms.length; i++){
                if(toRemove.contains(rooms[i])){
                    //Remove that body
                    rooms[i].onRemove();
                }else{
                    newCollection.add((Room)rooms[i]);
                }
            }
            //clear the buffer
            toRemove.clear();
            
            //add all new lights
            for (Room object : toAdd) {
                newCollection.add(object);
                object.onAdd();
            }
            //clear that buffer
            toAdd.clear();
            //build new light array
            Room[] out = new Room[newCollection.size()];
            for(int i = 0; i < newCollection.size(); i++){
                out[i] = newCollection.get(i);
            }
            //set it
            rooms = out;
        }
    }

    public void tick(){
        //tick rooms
        this.updateCollection();
        for(Room r : rooms){
            r.tick();
        }
    }
    
    public void render(Graphics g){
        for(Room r : rooms){
            r.render(g);
        }
    }
    
    public static void addRoom(Room room){
        toAdd.add(room);
    }
    
    public void add(String room){
        toAdd.add(new Room(0,0,room));
    }
    
    public static void removeRoom(Room room){
        toRemove.add(room);
    }
    
    public void clearAllRooms(){
        for(Room r : rooms){
            removeRoom(r);
        }
        ScriptingEngine.killAllScripts();
        updateCollection();
    }
    
    public static Room getRoom(RigidBody colision){
        for(Room r : rooms){
            if(RigidUtils.Collides(r.getCollision(), colision)){
                return r;
            }
        }
        return null;
    }
    
    public static Room[] getRooms(){
        return rooms;
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
