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
public class EntityBed extends EntityInteractable{

    Sprite bedframe;
    Sprite blanket;
    
    private boolean playerInBed = false;
    
    public EntityBed(int x, int y) {
        super(x, y, TileConstants.SCALE*4, TileConstants.SCALE*2, EnumEntityType.BED);
        bedframe = TowerOfPuzzles.spriteBinder.loadSprite("entity/bed/bedframe.png");
        blanket = TowerOfPuzzles.spriteBinder.loadSprite("entity/bed/blanket.png");
        super.setHighlight(bedframe);
    }

    @Override
    public void Update() {
        
    }

    @Override
    public void render(Graphics g) {
        bedframe.render(x, y, width, height, g);
        if(playerInBed){
            TowerOfPuzzles.player.left.render(x+(8*4), y-(4*4), TowerOfPuzzles.player.left.pWidth*4, TowerOfPuzzles.player.left.pHeight*4, g, 75);
            blanket.render(x, y, width, height, g);
        }else{
            if(super.highlight != null){
                if(super.isHighlighted()){
                   highlight.render(x, y, width, height, g);
                }
            }
        }
    }
    
    @Override
    public void Render(Graphics g) {

    }

    @Override
    public void event() {
        playerInBed = !playerInBed;
        TowerOfPuzzles.player.setCanMove(!playerInBed);
    }
    
}
