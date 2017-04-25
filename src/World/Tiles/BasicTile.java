/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World.Tiles;

import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class BasicTile extends Tile{

    public BasicTile(EnumTile et, int x, int y) {
        super(et, x, y);
    }
    
    @Override
    public void tick() {
        
    }

    @Override
    void extraRender(Graphics g) {
        
    }
    
}
