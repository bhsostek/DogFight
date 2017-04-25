/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityTeleportPlayer extends Entity{
    
    private boolean lastTick = false;

    public EntityTeleportPlayer(int x, int y) {
        super(x, y, 0, 0, EnumEntityType.TELEPORTER);
        
        Node[] nodes = new Node[]{
            new Node(0,0),
        };
        
        super.setNodes(nodes);
    }

    @Override
    public void update() {
        if(lastTick!=super.getNodes()[0].isPowered()){
            if(super.getNodes()[0].isPowered()){
                System.out.println("Moving Player to:"+super.x+","+super.y);
                TowerOfPuzzles.player.movePlayerTo((int)super.x, (int)super.y);
            }
        }
        lastTick = super.getNodes()[0].isPowered();
    }

    @Override
    public void render(Graphics g) {
        
    }
    
}
