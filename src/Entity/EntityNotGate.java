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
public class EntityNotGate extends Entity{

    
    public EntityNotGate(int x, int y) {
        super(x, y, TileConstants.SCALE*2, TileConstants.SCALE*2, EnumEntityType.NOT_GATE);
        
        Node[] nodes = new Node[]{
            new Node(-4 * 4,0),
            new Node(4 * 4,0),
        };
        
        super.setNodes(nodes);
        
    }

    @Override
    public void update() {
        super.getNodes()[1].setPowered(!super.getNodes()[0].isPowered());
    }

    @Override
    public void render(Graphics g) {
        super.drawNodes(g);
    }
    
}
