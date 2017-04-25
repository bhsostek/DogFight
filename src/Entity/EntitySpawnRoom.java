/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Editor.Attribute;
import World.Room;
import World.Tiles.TileConstants;
import World.WorldManager;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntitySpawnRoom extends Entity{
    
    private Attribute<String> name = new Attribute<String>("Room:","");
    private boolean powered = false;
    private final Room room;
    
    public EntitySpawnRoom(int x, int y, String name) {
        super(x, y, TileConstants.SCALE, TileConstants.SCALE, EnumEntityType.SPAWN_ROOM);
        this.name.setData(name);
        
        Node[] nodes = new Node[]{
            new Node(0,-6 * 4),
            new Node(0,6 * 4),
            new Node(6 * 4,0),
        };
        
        super.addAttribute(this.name);
        
        super.setNodes(nodes);
        
        room = new Room(x, y, name);
    }

    @Override
    public void update() {
        if(super.getNodes()[0].isPowered() && !powered){
            powered = true;
            WorldManager.addRoom(room);
        }
        
        if(super.getNodes()[1].isPowered() && powered){
            powered = false;
            WorldManager.removeRoom(room);
        }
        
        super.getNodes()[0].setPowered(powered);
    }

    @Override
    public void render(Graphics g) {
        
    }
    
    @Override
    public String save(){
        return super.getType()+"{"+super.x+","+super.y+","+this.name.getData()+","+super.getID()+"}";
    }
    
}
