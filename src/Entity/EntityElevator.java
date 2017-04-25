/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.RigidUtils;
import Physics.Vector3D;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityElevator extends Entity{
    
    RigidBody floor;
    
    Sprite body;
    
    float speed = 2.6f;
    
    public EntityElevator(int x, int y) {
        super(x, y - (9 * 4), 32 * 4, 50 * 4, EnumEntityType.ELEVATOR);
        
        floor = PrebuiltBodies.quad(new Point2D(x, y+(24 * 4)- (9 * 4)), 32 * 4, 2 * 4);
        body = TowerOfPuzzles.spriteBinder.loadSprite("entity/elevator/elevator.png");
        
        Node[] nodes = new Node[]{
            new Node(0,0),
            new Node(0,12 * 4),
            new Node(0,-12 * 4),
        };
        
        super.setNodes(nodes);
    }

     @Override
    public void onAdd(){
        PhysicsEngine.addToChannel("solids",floor);
    }
    
    @Override
    public void onRemove(){
        PhysicsEngine.getChannel("solids").remove(floor);
    }
    
    @Override
    public void update() {
        if(PhysicsEngine.intersects(super.collision, "player")){
            super.getNodes()[2].setPowered(true);
        }else{
            super.getNodes()[2].setPowered(false);
        }
        if(super.getNodes()[0].isPowered()){
            y-=speed;
        }
        if(super.getNodes()[1].isPowered()){
            y+=speed;
        }
        RigidUtils.MoveTo(new Vector3D(x, y,0), super.collision);
        RigidUtils.MoveTo(new Vector3D(x, y+(24 * 4),0), floor);
    }

    @Override
    public void render(Graphics g) {
       body.render(x, y, width, height, g);
    }
    
    @Override
    public String save(){
        return super.getType().name()+"{"+super.x+","+(super.y + (9 * 4))+","+super.getID()+"}";
    }
    
}
