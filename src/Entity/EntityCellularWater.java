/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Graphics.ColorPixel;
import Graphics.Pixel;
import Graphics.PixelUtils;
import Graphics.Sprite;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityCellularWater extends Entity{
    
    private int pwidth;
    private int pheight;
    short[][] cells;
    
    private final short maxFluid = 256;
    private Sprite water;
    
    public EntityCellularWater(int x, int y) {
        super(x, y, 12*16*4, 7*16*4, EnumEntityType.WATER_CELLS);
        this.pwidth = width/4;
        this.pheight = height/4;
        //width and height in tiles
        cells = new short[this.pwidth][this.pheight];
        for(int j = 0; j < this.pheight; j++){
            for(int i = 0; i < this.pwidth; i++){
                cells[i][j] = (short)((float)Math.random()*(float)this.maxFluid);
            }
        }
        Pixel[] pixels = new Pixel[this.pwidth * this.pheight];
        for(int i = 0; i<pixels.length; i++){
            pixels[i] = new ColorPixel(PixelUtils.makeColor(new int[]{0, 0, 0, 0}));
        }
        water = new Sprite(this.pwidth * 4, this.pheight * 4, this.pwidth, this.pheight, pixels);
        
        for(int j = 0; j < this.pheight; j++){
            for(int i = 0; i < this.pwidth; i++){
                 this.water.setPixelColor(i, j, PixelUtils.makeColor(new int[]{0,(int)((float)cells[i][j]/(float)this.maxFluid)*255,0,255}));
            }
        }
        
        water.update();
    }

    @Override
    public void update() {
//        this.cells[this.pwidth/2][0] = this.maxFluid;
        for(int i = 0; i < this.pwidth; i++){
            for(int j = 0; j < this.pheight; j++){
                moveCellDown(i,this.pheight-j-1);
                splitCell(i,this.pheight-j-1);
            }
        }
    }
    
    //step 1
    public void moveCellDown(int x, int y){
        if(x>=0 && x<pwidth){
            if(y>=0 && y<pheight){
                //If the cell has no fluid return
                if(this.cells[x][y]<=0){
                    return;
                }
                int below = y+1;
                if((below>=0 && below<pheight)){    
                    short belowVal = this.cells[x][below];
                    //transfer water
                    if(belowVal<this.maxFluid){
                        while(this.cells[x][y]>0 && this.cells[x][below]<this.maxFluid){
                            this.cells[x][below]+=1;
                            this.cells[x][y]-=1;
                        }
                    }else{
                        splitCell(x,below);
                    }
                }
                
                if(this.cells[x][y]==this.maxFluid){
                    splitCell(x,y);
                }
            }
        }
    }
    //step 2 check for split
    public void splitCell(int x, int y){
        short splitVolumeLeft = (short) Math.floor(this.cells[x][y]/3.0f);
        short splitVolumeRight = (short) Math.floor(this.cells[x][y]/3.0f);
        this.cells[x][y] = (short) Math.floor(this.cells[x][y]/3.0f);
        if(this.inBounds(x-1, y)){
            while(this.cells[x-1][y]<this.maxFluid && splitVolumeLeft > 0){
                splitVolumeLeft--;
                this.cells[x-1][y]+=1;
            }
        }
        if(this.inBounds(x+1, y)){
            while(this.cells[x+1][y]<this.maxFluid && splitVolumeLeft > 0){
                splitVolumeLeft--;
                this.cells[x+1][y]+=1;
            }
        }
        if(this.inBounds(x+1, y)){
            while(this.cells[x+1][y]<this.maxFluid && splitVolumeRight > 0){
                splitVolumeRight--;
                this.cells[x+1][y]+=1;
            }
        }
        if(this.inBounds(x-1, y)){
            while(this.cells[x-1][y]<this.maxFluid && splitVolumeRight > 0){
                splitVolumeRight--;
                this.cells[x-1][y]+=1;
            }
        }
        this.cells[x][y] += splitVolumeLeft + splitVolumeRight;
    }
    
    public short fillCell(int x, int y, short fluid){
        if(inBounds(x,y)){
            while(fluid>0 && this.cells[x][y]<this.maxFluid){
                fluid--;
                this.cells[x][y]++;
            }
        }
        return fluid;
    }
    
    public boolean inBounds(int x, int y){
        if(x>=0 && x<pwidth){
            if(y>=0 && y<pheight){
               return true; 
            }
        }
        return false;
    }

    @Override
    public void render(Graphics g) {
        for(int j = 0; j < this.pheight; j++){
            for(int i = 0; i < this.pwidth; i++){
                 this.water.setPixelColor(i, j, PixelUtils.makeColor(new int[]{255,0,(int)(((float)cells[i][j]/(float)this.maxFluid)*255),(int)(((float)cells[i][j]/(float)this.maxFluid)*255)}));
            }
        }
        this.water.update();
        this.water.render(x, y, g);
    }
    
}
