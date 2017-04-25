/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Bailey
 */
public class Sprite {
    public int width;
    public int height;
    
    public final int pWidth;
    public final int pHeight;
    public Pixel[] pixels;
    private BufferedImage image;
    
    public Sprite(int width, int height, int pWidth, int pHeight, Pixel[] pixels){
        this.width = width;
        this.height = height;
        
        this.pWidth = pWidth;
        this.pHeight = pHeight;
        
        if(pixels == null){
            pixels = new Pixel[pWidth * pHeight];
            for(int i = 0; i<pixels.length; i++){
                pixels[i] = new ColorPixel(PixelUtils.makeColor(new int[]{0, 0, 0, 0}));
            }
        }
        this.pixels = pixels;
        
        image = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, pWidth, pHeight, writeToIntArray(), 0, pWidth);
    }
    
    public Sprite(int width, int height, int pWidth, int pHeight, Color color){
        this.width = width;
        this.height = height;
        
        this.pWidth = pWidth;
        this.pHeight = pHeight;

        pixels = new Pixel[pWidth * pHeight];
        for(int i = 0; i<pixels.length; i++){
            pixels[i] = new ColorPixel(PixelUtils.makeColor(new int[]{color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()}));
        }
        
        image = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, pWidth, pHeight, writeToIntArray(), 0, pWidth);
    }
    
    private int[] writeToIntArray(){
        int[] imageData = new int[pWidth * pHeight];
        
        for(int j = 0; j< pHeight; j++){
            for(int i = 0; i< pWidth; i++){
                imageData[i+(j * pWidth)] = pixels[i+(j * pWidth)].getColor();
            }
        }
        
        return imageData;
    }
    
    public void overlay(Sprite display){
        if(display.pWidth == this.pWidth && display.pHeight == this.pHeight){
            for(int j = 0; j< pHeight; j++){
                for(int i = 0; i< pWidth; i++){
                    if(display.pixels[i+(j * pWidth)].getColor()<=16777215){
                        pixels[i+(j * pWidth)].setColor(display.pixels[i+(j * pWidth)].getColor());
                    }
                }
            }
            this.update();
        }
    }
    
    public Sprite getSubDisplay(int x, int y, int width, int height){
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(this.image, -x, -y, null);
        bGr.dispose();
        
        Pixel[] pixels = new Pixel[bimage.getWidth() * bimage.getHeight()];
        for(int j = 0; j<bimage.getHeight(); j++){
            for(int i = 0; i<bimage.getWidth(); i++){
                pixels[(i + (j * bimage.getWidth()))] = new ColorPixel(bimage.getRGB(i, j));
            }
        }
        
        Sprite out = new Sprite(bimage.getWidth(), bimage.getHeight(), bimage.getWidth(), bimage.getHeight(), pixels);
        return out;
    }
    
    public int getPixelColor(int x, int y){
        if(x>=0 && x<this.pWidth){
            if(y>=0 && y<this.pHeight){
                return this.pixels[x+(y * this.pWidth)].getColor();
            } 
        }
        return 0;
    }

    public void setPixelColor(int x, int y, int color){
        if(x>=0 && x<this.pWidth){
            if(y>=0 && y<this.pHeight){
                this.pixels[x+(y * this.pWidth)].setColor(color);
            } 
        }
    }
    
    public void update(){
        image = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, pWidth, pHeight, writeToIntArray(), 0, pWidth);
    }
    
    public void render(int x, int y, Graphics g){
        g.translate(x, y);
            g.drawImage(image, -width/2, -height/2, width, height, null);
        g.translate(-x, -y);
    }
    
    public void render(float x, float y, Graphics g){
        g.translate((int)x, (int)y);
            g.drawImage(image, -width/2, -height/2, width, height, null);
        g.translate(-(int)x, -(int)y);
    }
    
    public void render(int x, int y, Graphics g, int rotation){
        Graphics2D g2d = (Graphics2D)g;
        g.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));
                g.drawImage(image, -width/2, -height/2, width, height, null);
            g2d.rotate(-Math.toRadians(rotation));
        g.translate(-x, -y);
    }
    
    public void render(int x, int y, Graphics g, int rotation, int offsetX, int offsetY){
        Graphics2D g2d = (Graphics2D)g;
        g.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));
                g.drawImage(image, (-width/2)+offsetX, (-height/2)+offsetY, width, height, null);
            g2d.rotate(-Math.toRadians(rotation));
        g.translate(-x, -y);
    }
    
    public void render(int x, int y, int width, int height, Graphics g){
        g.translate(x, y);
            g.drawImage(image, -width/2, -height/2, width, height, null);
        g.translate(-x, -y);
    }
    
    public void render(float x, float y, int width, int height, Graphics g){
        g.translate((int)x, (int)y);
            g.drawImage(image, -width/2, -height/2, width, height, null);
        g.translate(-(int)x, -(int)y);
    }
    
    public void render(int x, int y, int width, int height, Graphics g, float rotation){
        Graphics2D g2d = (Graphics2D)g;
        g.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));
                g.drawImage(image, -width/2, -height/2, width, height, null);
            g2d.rotate(-Math.toRadians(rotation));
        g.translate(-x, -y);
    }
    
    public void render(float x, float y, int width, int height, Graphics g, float rotation){
        Graphics2D g2d = (Graphics2D)g;
        g.translate((int)x, (int)y);
            g2d.rotate(Math.toRadians(rotation));
                g.drawImage(image, -width/2, -height/2, width, height, null);
            g2d.rotate(-Math.toRadians(rotation));
        g.translate(-(int)x, -(int)y);
    }
    
    public void render(int x, int y, int width, int height, Graphics g, float rotation, float opacity){
        Graphics2D g2d = (Graphics2D)g;
        
        
        
        g.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g.drawImage(image, -width/2, -height/2, width, height, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.rotate(-Math.toRadians(rotation));
        g.translate(-x, -y);
    }
    
    public void overwriteColor(Color oldColor, Color newColor){
        for(int i = 0; i < this.pixels.length; i++){
            
        }
        this.update();
    }
    
}
