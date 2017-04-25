/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Base.TowerOfPuzzles;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class RenderCall {
    
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    float rot = 0.0f;
    float opacity = 1.0f;
    String sprite = "";
    
    public RenderCall(String inData){
        if(TowerOfPuzzles.DEBUG){
//            System.out.println("Passed in:"+inData);
        }
        String[] vars = inData.split(",");
        for(int i = 0; i < vars.length; i++){
            if(i == 0){
                this.sprite = vars[0].replaceAll(",", "");
            }
            if(i == 1){
                x = (int)Float.parseFloat(vars[1].replaceAll(",", ""));
            }
            if(i == 2){
                y = (int)Float.parseFloat(vars[2].replaceAll(",", ""));
            }
            if(i == 3){
                width = (int)Float.parseFloat(vars[3].replaceAll(",", ""));
            }
            if(i == 4){
                height = (int)Float.parseFloat(vars[4].replaceAll(",", ""));
            }
            if(i == 5){
                rot = Float.parseFloat(vars[5].replaceAll(",", "")); 
            }
            if(i == 6){
                opacity = Float.parseFloat(vars[6].replaceAll(",", "")); 
            }
        }
    }
    
    public String getSprite(){
        return this.sprite;
    }
    
    public void render(Sprite sprite, Graphics g){
        if(sprite == null){
            System.err.println("Sprite not found!");
            return;
        }
        
        if(width == 0){
            width = sprite.pWidth;
        }
        if(height == 0){
            height = sprite.pHeight;
        }

        if(opacity>1){
            opacity = 1;
        }
        
        if(opacity<0){
            opacity = 0;
        }
        
        sprite.render(x, y, width, height, g, rot, opacity);
    }
}
