/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World.Tiles;

import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class TileFloor extends Tile{

    RigidBody bod;
    
    public TileFloor(int x, int y, EnumTile et) {
        super(et, x, y);
        bod = PrebuiltBodies.quad(new Point2D((x+(8*4)), (y)), TileConstants.SCALE, 2*4);
        PhysicsEngine.addChannel("floor");
    }

    @Override
    public void tick() {

    }

    @Override
    void extraRender(Graphics g) {
        
    }
    
    @Override
    public void onAdd(){
        PhysicsEngine.addToChannel("floor", bod);
    }
    
    @Override
    public void onRemove(){
        PhysicsEngine.getChannel("floor").remove(bod);
    }
    
}
