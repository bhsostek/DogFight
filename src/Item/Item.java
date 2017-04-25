/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Graphics.PixelUtils;
import java.awt.Color;
import java.awt.Graphics;


/**
 *
 * @author Bailey
 */
public abstract class Item {
    
    protected final Items eit;
    protected String[] data;
    
    public Item(Items eit, String[] data){
        this.eit = eit;
        this.data = data;
    }
    
    public abstract String[] writeDataToBuffer(); 
    
    public String genSave(){
        this.data = this.writeDataToBuffer();
        String out = "";
        for(int i = 0; i < data.length; i++){
            if(i < data.length-1){
                //comma
                out = out+data[i]+",";
            }else{
                //no comma
                out = out+data[i];
            }
        }
        return eit.name()+"{"+out+"}";
    }
    
    public void tickInWorld(int x, int y){
        return;
    }
    
    public void tickInHand(){
        return;
    }
    
    public void renderInWorld(int x, int y, Graphics g){
        eit.getImage().render(x, y, eit.getImage().pWidth * 4, eit.getImage().pHeight * 4, g);
    }

    //dir 0 = left
    //dir 1 = right
    public void renderInHand(int x, int y, Graphics g, int dir, int offset){
        eit.getImage().render(x, y, eit.getImage().pWidth * 4, eit.getImage().pHeight * 4, g);
    }
    
    public void renderText(int x, int y, Graphics g){
        TowerOfPuzzles.textRenderer.setColor(PixelUtils.makeColor(PixelUtils.getRGBA(Color.CYAN)));
        TowerOfPuzzles.textRenderer.drawString(eit.name, x, y, g);
    }
    
    
    public String getName(){
        return this.eit.name;
    }
    
    public Items getType(){
        return this.eit;
    }
    
    public boolean instanceOf(String string){
        Items item = Items.valueOf(string);
        if(item!=null){
            return this.getType().equals(item);
        }
        return false;
    }
}
