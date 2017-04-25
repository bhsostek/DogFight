/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityWindow extends Entity{

    Sprite sprite;
    
    public EntityWindow(int x, int y) {
        super(x, y, 2 * TileConstants.SCALE, 3 * TileConstants.SCALE, EnumEntityType.WINDOW);
        sprite = TowerOfPuzzles.spriteBinder.loadSprite("entity/window/library_window_3.png");
    }

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        sprite.render(x, y, width, height, g);
    }
    
}
