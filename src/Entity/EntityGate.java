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
public class EntityGate extends Entity{
    
    private RigidBody gate;
    private Sprite sprite;
    
    private float offset = 0;
    private float speed = 2.3f;
    private final int maxOffset= TileConstants.SCALE * 3;
    
    public EntityGate(int x, int y) {
        super(x, y, 4 * 4, 4 * TileConstants.SCALE, EnumEntityType.GATE);
        
        Node[] nodes = new Node[]{
          new Node(0,-(TileConstants.SCALE * 2)),  
        };
        
        gate = PrebuiltBodies.quad(new Point2D(super.x, super.y), width, height);
        
        sprite = TowerOfPuzzles.spriteBinder.loadSprite("entity/gate/gate.png");
        
        super.setNodes(nodes);
    }
    
    @Override
    public void onAdd(){
        PhysicsEngine.addToChannel("solids",gate);
    }
    
    @Override
    public void onRemove(){
        PhysicsEngine.getChannel("solids").remove(gate);
    }

    @Override
    public void update() {
        if(super.getNodes()[0].isPowered()){
            if(offset<maxOffset){
                offset+=speed;
            }
        }else{
            if(offset>0){
                offset -= speed;
            }
        }
        RigidUtils.MoveTo(new Vector3D(super.x, super.y-offset,0), gate);
    }

    @Override
    public void render(Graphics g) {
        g.setClip((int)x-width/2, (int)y-height/2, width, height);
        sprite.render(x, y-(int)offset, width, height, g);
        g.setClip(null);
        super.drawNodes(g);
    }
    
}
