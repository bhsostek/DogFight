/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Base.util.DistanceCalculator;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import Physics.RigidUtils;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityBook extends EntityFallingObject{

    private Sprite book;
    
    EntityParticleEmitter pe;
    
    public EntityBook(int x, int y) {
        super(x, y, 9 * 4, 12 * 4, EnumEntityType.BOOK);
        book = TowerOfPuzzles.spriteBinder.loadSprite("entity/bookshelf/book.png");
        super.acceleration.increaseVelY((float)(-20.0f));
        super.acceleration.increaseVelX((float)(10.0f * Math.random())-5.0f);
        
        pe = new EntityParticleEmitter(x, y, "torch");
        pe.setNumParticles(400);
    }

    @Override
    public void render(Graphics g) {
        RigidUtils.Render(collision, g);
        book.render(x, y, width, height, g);
        pe.render(g);
    }

    @Override
    public void obj_update() {
        if(DistanceCalculator.CalculateDistanceF(x, y, TowerOfPuzzles.player.x, TowerOfPuzzles.player.y)<TileConstants.SCALE){
            TowerOfPuzzles.entityManager.remove(this);
        }
        pe.tick();
        pe.x = x;
        pe.y = y;
    }
    
}
