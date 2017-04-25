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
public class TileLadderLeft extends Tile{

    RigidBody bod;
    
    public TileLadderLeft(int x, int y) {
        super(EnumTile.LADDER_LEFT, x, y);
        bod = PrebuiltBodies.quad(new Point2D((x+(8 * 4)) - (8 * 4), (y+(6 * 4))), 4 * 4, 16 * 4);
    }

    @Override
    public void tick() {

    }

    @Override
    void extraRender(Graphics g) {
        
    }
    
    @Override
    public void onAdd(){
        PhysicsEngine.addToChannel("ladder", bod);
    }
    
    @Override
    public void onRemove(){
        PhysicsEngine.getChannel("ladder").remove(bod);
    }
    
}
