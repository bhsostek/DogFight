/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World.Tiles;

import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author Bailey
 */
public class Stone extends Tile{

    public Stone(int x, int y) {
        super(EnumTile.STONE_1, x, y);
        Random r = new Random();
        switch(r.nextInt(4)){
            case 0:
                super.setTile(EnumTile.STONE_1);
            case 1:
                super.setTile(EnumTile.STONE_2);
            case 2:
                super.setTile(EnumTile.STONE_3);
            case 3:
                super.setTile(EnumTile.STONE_4);
        }
    }
    
    @Override
    public void tick() {
        
    }

    @Override
    void extraRender(Graphics g) {
        
    }
    
}
