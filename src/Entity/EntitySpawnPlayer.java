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
public class EntitySpawnPlayer extends Entity{

    public EntitySpawnPlayer(int x, int y) {
        super(x, y, 0, 0, EnumEntityType.SPAWN_POINT);
    }
    
    @Override
    public void onAdd(){
        System.out.println("Moving Player to:"+super.x+","+super.y);
        TowerOfPuzzles.player.movePlayerTo((int)super.x, (int)super.y);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        
    }
    
}
