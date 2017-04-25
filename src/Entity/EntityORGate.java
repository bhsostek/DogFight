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
public class EntityORGate extends Entity{

    
    public EntityORGate(int x, int y) {
        super(x, y, TileConstants.SCALE*2, TileConstants.SCALE*2, EnumEntityType.OR_GATE);
        
        Node[] nodes = new Node[]{
            new Node(0, -12 * 4),
            new Node(0, -4 * 4),
            new Node(0, 4 * 4),
            new Node(0, 12 * 4),
            new Node(6 * 4, 0),
        };
        
        super.setNodes(nodes);
        
    }

    @Override
    public void update() {
        super.getNodes()[4].setPowered(super.getNodes()[0].isPowered()||super.getNodes()[1].isPowered()||super.getNodes()[2].isPowered()||super.getNodes()[3].isPowered());
    }

    @Override
    public void render(Graphics g) {
        super.drawNodes(g);
    }
    
}
