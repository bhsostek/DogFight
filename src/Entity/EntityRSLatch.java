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
public class EntityRSLatch extends Entity{

    boolean output = false;
    
    public EntityRSLatch(int x, int y) {
        super(x, y, TileConstants.SCALE*2, TileConstants.SCALE*2, EnumEntityType.RS_LATCH);
        
        Node[] nodes = new Node[]{
            new Node(0,-8*4),
            new Node(0,8*4),
            new Node(8 * 4,0),
        };
        
        super.setNodes(nodes);
        
    }

    @Override
    public void update() {
        onLoop:{
            if(super.getNodes()[0].isPowered()){
                output = true;
                break onLoop;
            }

            if(super.getNodes()[1].isPowered()){
                output = false;
                break onLoop;
            }
        }
       
        super.getNodes()[2].setPowered(output);
    }

    @Override
    public void render(Graphics g) {
        super.drawNodes(g);
    }
    
}
