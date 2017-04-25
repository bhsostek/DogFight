/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Editor.Attribute;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityBuffer extends Entity{
    
    int count = 0;
    
    private Attribute<Integer> ticks = new Attribute<Integer>("Buffer Ticks:", 0);
    
    public EntityBuffer(int x, int y, int ticks) {
        super(x, y, TileConstants.SCALE*2, TileConstants.SCALE*2, EnumEntityType.BUFFER);
        
        this.ticks.setData(ticks);
        
        Node[] nodes = new Node[]{
            new Node(-4 * 4,0),
            new Node(4 * 4,0),
        };
        
        super.addAttribute(this.ticks);
        
        super.setNodes(nodes);
        
    }

    @Override
    public void update() {
        if(super.getNodes()[0].isPowered()){
            if(count < this.ticks.getData()){
                count++;
            }
        }else{
            super.getNodes()[1].setPowered(false);
            count = 0;
        }
        
        if(count >= ticks.getData()){
            super.getNodes()[1].setPowered(true);
        }
    }

    @Override
    public void render(Graphics g) {
        super.drawNodes(g);
    }
    
    @Override
    public String save(){
        return super.getType()+"{"+super.x+","+super.y+","+this.ticks.getData()+","+super.getID()+"}";
    }
    
}
