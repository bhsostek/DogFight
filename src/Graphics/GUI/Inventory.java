/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GUI;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Graphics.Sprite;
import Item.Container;
import Item.Item;
import Item.ItemStack;
import World.Tiles.TileConstants;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class Inventory extends UI{

    private Sprite inventorySprite = TowerOfPuzzles.spriteBinder.loadSprite("gui/inventory.png");
    private Sprite nullItem = TowerOfPuzzles.spriteBinder.loadSprite("gui/nullItem.png");
    private Sprite nullHighlight = TowerOfPuzzles.spriteBinder.loadSprite("gui/nullHighlight.png");
    
    private Container inventory;
    private Container container;
    
    private final int inventorySize = TileConstants.SCALE*4;
    private final int containerSize = TileConstants.SCALE*2;
    private Debouncer keyLeft = new Debouncer(Keyboard.A);
    private Debouncer keyRight = new Debouncer(Keyboard.D);
    private Debouncer keyDown = new Debouncer(Keyboard.S);
    private Debouncer keyUp = new Debouncer(Keyboard.W);
    
    private boolean innerRing = false;
    
    private int inventorySelected = 0;
    private int containerSelected = 0;
    
    private float currentDegreeOuter = 0;
    private float currentDegreeInner = 0;
    private float desiredDegreeOuter = 0;
    private float desiredDegreeInner = 0;
    
    private final float rotationalSpeed = 2;
    
    public Inventory(int x, int y, Container inventory, Container container) {
        super(x, y);
        this.container = container;
        this.inventory = inventory;
    }

    @Override
    public void tick() {
        if(keyLeft.risingAction(Keyboard.A)){
            if(innerRing){
                this.containerSelected--;
                if(this.containerSelected<0){
                    this.containerSelected = container.getLength()-1;
                    this.currentDegreeInner+=360.0f;
                }
            }else{
                this.inventorySelected--;
                if(this.inventorySelected<0){
                    this.inventorySelected = inventory.getLength()-1;
                    this.currentDegreeOuter+=360.0f;
                }
            }
        }
        if(keyRight.risingAction(Keyboard.D)){
            if(innerRing){
                this.containerSelected++;
                if(this.containerSelected>=container.getLength()){
                    this.containerSelected = 0;
                    this.currentDegreeInner-=360.0f;
                }
            }else{
                this.inventorySelected++;
                if(this.inventorySelected>=inventory.getLength()){
                    this.inventorySelected = 0;
                    this.currentDegreeOuter-=360.0f;
                }
            }
        }
        if(keyDown.risingAction(Keyboard.S)){
            if(!innerRing){
                if(Keyboard.SHIFT){
                    ItemStack shift = this.container.addItemToOpenSlotStartingAtIndex(this.inventory.getItemStack(this.inventorySelected), this.containerSelected);
                    this.inventory.putItemStack(shift, this.inventorySelected);
                    return;
                }
                innerRing = true;
            }
        }
        if(keyUp.risingAction(Keyboard.W)){
            if(innerRing){
                if(Keyboard.SHIFT){
                    ItemStack shift = this.inventory.addItemToOpenSlotStartingAtIndex(this.container.getItemStack(this.containerSelected),this.inventorySelected);
                    this.container.putItemStack(shift, this.containerSelected);
                    return;
                }
                innerRing = false;
            }
        }
        desiredDegreeOuter = ((float)this.inventorySelected)*(360.0f/((float)this.inventory.getLength()));
        desiredDegreeInner = ((float)this.containerSelected)*(360.0f/((float)this.container.getLength()));
        
        if((int)this.currentDegreeOuter > (int)desiredDegreeOuter%360){
            this.currentDegreeOuter-=rotationalSpeed;
        }
        
        if((int)this.currentDegreeOuter < (int)desiredDegreeOuter%360){
            this.currentDegreeOuter+=rotationalSpeed;
        }
        
        if((int)this.currentDegreeInner > (int)desiredDegreeInner%360){
            this.currentDegreeInner-=rotationalSpeed;
        }
        
        if((int)this.currentDegreeInner < (int)desiredDegreeInner%360){
            this.currentDegreeInner+=rotationalSpeed;
        }
        
    }

    @Override
    public void render(Graphics g) {
        
        inventorySprite.render(x, y, inventorySprite.pWidth * 4, inventorySprite.pHeight * 4, g);
        
        float degreeOffset = -currentDegreeOuter;

        for(int i = 0; i < this.inventory.getLength(); i++){
            if(!this.innerRing){
                if(i==this.inventorySelected){
                    nullHighlight.render(x +(int)((inventorySize)* Math.cos(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))),
                                y +(int)((inventorySize)* Math.sin(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))), 
                                nullItem.pWidth * 4,
                                nullItem.pHeight * 4,
                                g);
                }
            }
            
            nullItem.render(x +(int)((inventorySize)* Math.cos(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))),
                                y +(int)((inventorySize)* Math.sin(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))), 
                                nullItem.pWidth * 4,
                                nullItem.pHeight * 4,
                                g);
            
            Item item = this.inventory.getItemStack(i).item;
            if(item!=null){
                item.renderInWorld(x +(int)((inventorySize)* Math.cos(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))),
                                   y +(int)((inventorySize)* Math.sin(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))), g);           
                if(!innerRing){
                    if(i==this.inventorySelected){
                        Sprite highlight = item.getType().getHighlight();
                        highlight.render(x +(int)((inventorySize)* Math.cos(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))),
                                         y +(int)((inventorySize)* Math.sin(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))), 
                                         highlight.pWidth * 4,
                                         highlight.pHeight * 4,
                                         g);
                        TowerOfPuzzles.textRenderer.drawString(this.inventory.getItemStack(i).count+"",
                            x +(int)((inventorySize)* Math.cos(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))),
                            y +(int)((inventorySize)* Math.sin(Math.toRadians((i * (360.0f / this.inventory.getLength()))-90.0f+degreeOffset))) - (16 * 4),
                            g);
                        
                        item.renderText(
                            TileConstants.SCALE/2,
                            TileConstants.SCALE/2,
                            g);
                    }
                }
            }

        }
        
        //change it for the inner ring
        degreeOffset = -currentDegreeInner;

        for(int i = 0; i < this.container.getLength(); i++){
            Item item = this.container.getItemStack(i).item;
            
            if(this.innerRing){
                if(i == this.containerSelected){
                    nullHighlight.render(x +(int)((containerSize)* Math.cos(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))),
                            y +(int)((containerSize)* Math.sin(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))),
                            nullItem.pWidth * 4,
                            nullItem.pHeight * 4,
                            g);
                }
            }
            
            nullItem.render(x +(int)((containerSize)* Math.cos(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))),
                            y +(int)((containerSize)* Math.sin(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))),
                            nullItem.pWidth * 4,
                            nullItem.pHeight * 4,
                            g);
            if(item!=null){
                item.renderInWorld(x +(int)((containerSize)* Math.cos(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))),
                                   y +(int)((containerSize)* Math.sin(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))), g);
                if(innerRing){
                    if(i==this.containerSelected){
                        
                        //Render item sprite
                        Sprite highlight = item.getType().getHighlight();
                        highlight.render(x +(int)((containerSize)* Math.cos(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))),
                                         y +(int)((containerSize)* Math.sin(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))),
                                         highlight.pWidth * 4,
                                         highlight.pHeight * 4,
                                         g);
                        
                        //Render Item Quantity
                        TowerOfPuzzles.textRenderer.drawString(this.container.getItemStack(i).count+"",
                            x +(int)((containerSize)* Math.cos(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))),
                            y +(int)((containerSize)* Math.sin(Math.toRadians((i * (360.0f / this.container.getLength()))-90.0f+degreeOffset))) - (48 * 4),
                            g);
                        
                        item.renderText(
                            TileConstants.SCALE/2,
                            TileConstants.SCALE/2,
                            g);
                    }
                }
            }
        }
        
    }
    
    public void onOpen(){
        this.setSelectedIndex(TowerOfPuzzles.player.getSelectedIndex());
    }
    
    public void onClose(){
        TowerOfPuzzles.player.setSelectedIndex(this.getSelectedIndex());
    }
    
    public void setSelectedIndex(int i){
        inventorySelected = i;
        this.currentDegreeOuter = ((float)i )*(360.0f/((float)this.inventory.getLength()));
    }
    
    public int getSelectedIndex(){
        return this.inventorySelected;
    }
    @Override
    public void onClick(Rectangle rect) {
        
    }
    
}
