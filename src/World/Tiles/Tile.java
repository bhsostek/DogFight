/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World.Tiles;

import Base.Keyboard;
import Base.MousePositionLocator;
import Base.TowerOfPuzzles;
import Base.util.DistanceCalculator;
import Graphics.ColorPixel;
import Graphics.Pixel;
import Graphics.PixelUtils;
import Graphics.Sprite;
import Graphics.SpriteUtils;
import Lighting.Light;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public abstract class Tile {
    private EnumTile enumTile;
    private Sprite sprite;
    
    public int x;
    public int y;
    //rectangle
    
    protected Tile(EnumTile et, int x, int y){
        this.x = x;
        this.y = y;
        this.enumTile = et;
        sprite = enumTile.getImage();
        calculateLighting();
    }
    
    public void setTile(EnumTile et ){
        if(et==null)return;
        enumTile = et;
    }
    
    public EnumTile getEnumTile(){
        return this.enumTile;
    }
    
    public abstract void tick();
    
    abstract void extraRender(Graphics g);
    
    public void render(Graphics g){
        
        calculateLighting();
        
        sprite.render(x+ (TileConstants.SCALE/2), y+ (TileConstants.SCALE/2), TileConstants.SCALE, TileConstants.SCALE, g);
        
        if(Keyboard.bool_1){
            g.setColor(Color.cyan);
            g.drawRect(x, y, TileConstants.SCALE, TileConstants.SCALE);
        }
        
        extraRender(g);
    }
    
    public void calculateLighting(){
        boolean updateLighting = false;
        for(Light light: TowerOfPuzzles.lightingEngine.getLights()){
            if(light.hasMoved()){
                updateLighting = true;
                break;
            }
        }
        if(updateLighting){
            Pixel[] pixelData = new Pixel[16*16];
            for(int i = 0; i < pixelData.length; i++){
                pixelData[i] = new ColorPixel(PixelUtils.makeColor(new int[]{0,0,0,0}));
            }
            for(Light light: TowerOfPuzzles.lightingEngine.getLights()){
                int[] lightColor = PixelUtils.getRGBA(light.getColor());
                float distance = DistanceCalculator.CalculateDistanceF(x, y, light.getX(), light.getY());
                if(distance < light.getSize()+TileConstants.SCALE ){
                    for(int i = 0; i < 16; i++){
                        for(int j = 0; j < 16; j++){
                            float pixelDistance = DistanceCalculator.CalculateDistanceF(x+(i*4), y+(j*4), light.getX(), light.getY());
                            float outPixel = ((1.0f-(pixelDistance/(light.getSize()))));
                            if(outPixel<0){
                                outPixel = 0;
                            }
                            if(outPixel > 255){
                                outPixel = 255;
                            }
                            int[] colors = new int[]{(int)(lightColor[0] * outPixel),(int)(lightColor[1] * outPixel),(int)(lightColor[2] * outPixel),255};
                            pixelData[i+(j*16)] = new ColorPixel(PixelUtils.averageColors(pixelData[i+(j*16)].getColor(), PixelUtils.makeColor(colors)));
                        } 
                    }
                }
            }
            sprite = SpriteUtils.overlaySprite(enumTile.getImage(), pixelData);
//                            System.out.println("Update Light");
        }
    }
    
    
    public void onAdd(){
        return;
    }
    
    public void onRemove(){
        return;
    }
}
