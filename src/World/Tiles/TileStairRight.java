/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World.Tiles;

import Physics.PhysicsEngine;
import Physics.Point;
import Physics.Point2D;
import Physics.Point3D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class TileStairRight extends Tile{

    RigidBody temp;
    
    public TileStairRight(int x, int y,EnumTile eet) {
        super(eet, x, y);
        temp = new RigidBody(
           new Point[]{
               new Point3D(+ (TileConstants.SCALE/2), - (TileConstants.SCALE/2), 0),
               new Point3D(+ (TileConstants.SCALE/2), + (TileConstants.SCALE/2), 0),
               new Point3D(- (TileConstants.SCALE/2), + (TileConstants.SCALE/2), 0),
           }
        );
        temp.Translate(x+(TileConstants.SCALE/2), y+(TileConstants.SCALE/2), 0);
        PhysicsEngine.addChannel("floor");
        PhysicsEngine.addToChannel("floor",temp);
    }

    @Override
    public void tick() {
        
    }

    @Override
    void extraRender(Graphics g) {
        
    }
    
    @Override
    public void onAdd(){
        PhysicsEngine.addToChannel("floor",temp);
    }
    
    @Override
    public void onRemove(){
        PhysicsEngine.getChannel("floor").remove(temp);
    }
    
}
