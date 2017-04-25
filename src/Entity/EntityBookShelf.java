/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityBookShelf extends Entity{
    
    Sprite sprite;
    Sprite fullBookShelf;
    
    Attribute<Boolean> isDecorative = new Attribute<Boolean>("Is Deccoration:", false);
    
    public EntityBookShelf(int x, int y, boolean isDecorative) {
        super(x, y, TileConstants.SCALE * 2, TileConstants.SCALE * 3, EnumEntityType.BOOK_SHELF);
        sprite = TowerOfPuzzles.spriteBinder.loadSprite("entity/bookshelf/bookshelf_empty.png");
        fullBookShelf = TowerOfPuzzles.spriteBinder.loadSprite("entity/bookshelf/bookshelf_full.png");
        
        this.isDecorative.setData(isDecorative);
        
        super.addAttribute(this.isDecorative);
    }

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        if(!isDecorative.getData()){
            sprite.render(x, y, width, height, g);
        }else{
            fullBookShelf.render(x, y, width, height, g);
        }
    }
    
    @Override
    public String save(){
        return super.getType().name()+"{"+(x)+","+(y)+","+isDecorative.getData()+"}";
    }
    
}
