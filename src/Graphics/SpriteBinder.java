/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Graphics;

import Base.Engine;
import Base.TowerOfPuzzles;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import javax.script.ScriptEngine;
import javax.swing.ImageIcon;

/**
 *this is a file that contains all the loaded sprites 
 * @author Bayjose
 */
public class SpriteBinder extends Engine{
    //current resources.png file in the dir you are looking at
    
    public static Image preview;
    public static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    public static LinkedList<RegisteredImage> loadedImages = new LinkedList<RegisteredImage>();
    
    public SpriteBinder(){
        super("SpriteBinder");
        SpriteBinder.loadedImages.add(new RegisteredImage("Core/fileNotFound.png"));
    }
    
    public Image checkImage(String id){
        for(int i=0; i<SpriteBinder.loadedImages.size(); i++){
            if(SpriteBinder.loadedImages.get(i).id.equals(id)){
                return SpriteBinder.loadedImages.get(i).image;
            }
        }
        try{
            RegisteredImage temp = new RegisteredImage(id);
            if(!temp.broken){
                SpriteBinder.loadedImages.add(temp);
                return temp.image;
            }else{
                System.out.println("Image:"+id+" not found");
                return SpriteBinder.loadedImages.getFirst().image;
            }
        }catch(java.lang.NullPointerException e){
            System.out.println("Image:"+id+" not found");
            return SpriteBinder.loadedImages.getFirst().image;
        }
    }

    public Sprite loadSprite(String image){
        if(sprites.containsKey(image)){
            return new Sprite(sprites.get(image).width, sprites.get(image).height, sprites.get(image).pWidth, sprites.get(image).pHeight, sprites.get(image).pixels);
        }
        
        Image img = this.checkImage(image);
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        
        Pixel[] pixels = new Pixel[bimage.getWidth() * bimage.getHeight()];
        int[] alpha = new int[bimage.getWidth() * bimage.getHeight()];
        for(int j = 0; j<bimage.getHeight(); j++){
            for(int i = 0; i<bimage.getWidth(); i++){
                pixels[(i + (j * bimage.getWidth()))] = new ColorPixel(bimage.getRGB(i, j));
            }
        }
        
        Sprite out = new Sprite(bimage.getWidth(), bimage.getHeight(), bimage.getWidth(), bimage.getHeight(), pixels);
        sprites.put(image, out);
        return sprites.get(image);
    }
    
    public Sprite loadSpriteDirect(String image){
        System.out.println("Loading Resource:"+TowerOfPuzzles.Path+TowerOfPuzzles.ImagePath+image);
        ImageIcon imgcon = new ImageIcon(TowerOfPuzzles.Path+TowerOfPuzzles.ImagePath+image);
        Image img = imgcon.getImage();
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
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

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(),this);
    }


    @Override
    public void onShutdown() {

    }
}
