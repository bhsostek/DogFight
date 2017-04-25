/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityANDGate extends Entity{

    
    public EntityANDGate(int x, int y) {
        super(x, y, TileConstants.SCALE, TileConstants.SCALE, EnumEntityType.AND_GATE);
        
        Node[] nodes = new Node[]{
            new Node(0,-8*4),
            new Node(0,8*4),
            new Node(8 * 4,0),
        };
        
        super.setNodes(nodes);
        
    }

    @Override
    public void update() {
        if(super.getNodes()[0].isPowered()&&super.getNodes()[1].isPowered()){
            super.getNodes()[2].setPowered(true);
        }else{
            super.getNodes()[2].setPowered(false);
        }
    }
    
    @Override
    public void render(Graphics g) {
        super.drawNodes(g);
    }
    
}
