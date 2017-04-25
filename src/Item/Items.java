/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteUtils;
import java.util.HashMap;

/**
 *
 * @author Bailey
 */
public enum Items {
    KEY("Key", "key.png"),
    BERRY("Berry", "berry.png"),
    PAPER("Paper", "paper.png"),
    
    //Weapons
    TRIDENT("Trident","weapons/trident.png"),
    
    //Easter Egg Items
    //Jayne Hat
    //Undertale Dog
    PETUNIA("Petunia", "petunia.png"),
    //Penguin(for trafficing Purposes) 
    PENGUIN("Traffic Cone", "penguin.png"),
    GUN("gun", "weapons/trident.png");
    ;
    protected final Sprite sprite;
    protected final Sprite highlight;
    protected final String name;
    Items(String name, String image){
        this.sprite = TowerOfPuzzles.spriteBinder.loadSprite("items/"+image);
        this.highlight = SpriteUtils.outlineSprite(sprite);
        this.name = name;
    }
    
    public Sprite getImage(){
        return this.sprite;
    }
    
    public Sprite getHighlight(){
        return this.highlight;
    }
    
    public Item generate(String data){
        String[] parsedData = data.split(",");
        return generate(parsedData);
    }
    
    public Item generate(String[] data){
        switch(this){
            case TRIDENT:
                return new ItemTrident(this,data);
            case PAPER:
                return new RuneScript(this, data);
            case GUN:
                return new ItemGun(this, data);
            default:
                return new BasicItem(this);
        }
    }
    
    public static HashMap<String, Items> getItems(){
        HashMap<String, Items> out = new HashMap<String, Items>();
        for(int i = 0; i < Items.values().length; i++){
            out.put(Items.values()[i].name(), Items.values()[i]);
        }
        return out;
    }
    
}
