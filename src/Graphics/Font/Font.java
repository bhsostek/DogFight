/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Font;

import Base.TowerOfPuzzles;
import Graphics.PixelUtils;
import Graphics.Sprite;
import Graphics.SpriteUtils;
import java.util.HashMap;

/**
 *
 * @author Bailey
 */
public class Font {
    
    private final HashMap<String, Sprite> characters = new HashMap<String,Sprite>();
    
    public Font(String path){
        for(int i = 0; i < Characters.CHARACTERS.length(); i++){
            String key = Characters.CHARACTERS.charAt(i)+"";
            if(key.equals(" ")){
                characters.put(Characters.CHARACTERS.charAt(i)+"", TowerOfPuzzles.spriteBinder.loadSprite("font/"+path+"/SPACE.png"));
            }else if(key.toLowerCase().equals(key)){
                characters.put(Characters.CHARACTERS.charAt(i)+"", TowerOfPuzzles.spriteBinder.loadSprite("font/"+path+"/"+Characters.CHARACTERS.charAt(i)+".png"));
            }else{
                characters.put(Characters.CHARACTERS.charAt(i)+"", TowerOfPuzzles.spriteBinder.loadSprite("font/"+path+"/"+Characters.CHARACTERS.charAt(i)+"^.png"));
            }
        }
    }
    
    public Sprite getFromCharacter(String key){
        if(characters.containsKey(key)){
            return characters.get(key);
        }else{
            return null;
        }
    }
    
    public void setColor(int color){
        int index = 0;
        for(Sprite s : this.characters.values()){
            s = SpriteUtils.overlayGrayscale(s, PixelUtils.getRGBA(color));
            this.characters.put((String)this.characters.keySet().toArray()[index], s);
            index ++;
        }
    }
}
