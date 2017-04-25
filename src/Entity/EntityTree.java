/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Graphics.Sprite;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityTree extends Entity{

    private Attribute<Integer> spriteIndex = new Attribute<Integer>("Tree Index:", 0);
    private Attribute<Sprite> sprite = new Attribute<Sprite>("Sprite:", null);
    
    public EntityTree(int x, int y, int index) {
        super(x, y, (48 * 4 * 2), (48 * 4 * 2), EnumEntityType.TREE);
        
        spriteIndex.setData(index);
        
        Sprite newSprite = TowerOfPuzzles.spriteBinder.loadSprite("entity/tree/tree_"+this.spriteIndex.getData()+".png");
        if(!newSprite.equals(TowerOfPuzzles.spriteBinder.loadSprite("Core/fileNotFound.png"))){
            this.sprite.setData(newSprite);
        }else{
            this.spriteIndex.setData(0);
            this.sprite.setData(TowerOfPuzzles.spriteBinder.loadSprite("entity/tree/tree_"+this.spriteIndex.getData()+".png"));
        }
        
        sprite.setCanEdit(false);
        super.addAttribute(spriteIndex);
        super.addAttribute(sprite);
    }

    
    @Override
    public void onAttributeChange(){
        Sprite newSprite = TowerOfPuzzles.spriteBinder.loadSprite("entity/tree/tree_"+this.spriteIndex.getData()+".png");
        if(!newSprite.equals(TowerOfPuzzles.spriteBinder.loadSprite("Core/fileNotFound.png"))){
            this.sprite.setData(newSprite);
        }else{
            
        }
    }
    
    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        this.sprite.getData().render(x, y, width, height, g);
    }
    
    @Override
    public String extraSaveData(){
        return ""+this.spriteIndex.getData();
    }
    
}
