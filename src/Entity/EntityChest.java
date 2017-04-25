/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.StringUtils;
import Editor.Attribute;
import Editor.SaveManager;
import Graphics.GUI.Inventory;
import Graphics.Sprite;
import Item.Container;
import Physics.PhysicsEngine;
import World.WorldManager;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityChest extends EntityInteractable{
    
    Sprite[] frames = new Sprite[4];
    
    private final int maxTicks = 30;
    private int ticks = 0;
    
    private Container inventory;
    
    private boolean debounce = true;
    
    private Attribute<Boolean> open = new Attribute<Boolean>("Open:", false);
    
    int index = 0;
    
    float degreeOffset = 0;
    
    public EntityChest(int x, int y, boolean b, String inventoryData) {
        super(x, y, 22 * 4, 23 * 4, EnumEntityType.CHEST);
        
        super.addAttribute(open);
        
        String path = inventoryData;
        String roomPath="";
        if(!path.contains("/saves/")){
            if(WorldManager.getRoom(collision)!=null){
                roomPath = WorldManager.getRoom(collision).getName();
            }
            
            if(!path.contains(".txt")){
                path = roomPath+"/saves/"+path+".txt";
            }
        }else{
            if(!path.contains(".txt")){
                path = path+".txt";
            }
        }
        
        inventory = new Container(StringUtils.loadData(TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+"/"+path));
        inventory.getFileNameAttribute().setData(path);
        
        super.addAttribute(inventory.getFileNameAttribute());
        super.addAttribute(inventory.getLengthAttribute());

        super.setExtraSavePath(path);
        
        open.setData(b);
        
        frames[0] = TowerOfPuzzles.spriteBinder.loadSprite("entity/chest/chest_0.png");
        frames[1] = TowerOfPuzzles.spriteBinder.loadSprite("entity/chest/chest_1.png");
        frames[2] = TowerOfPuzzles.spriteBinder.loadSprite("entity/chest/chest_2.png");
        frames[3] = TowerOfPuzzles.spriteBinder.loadSprite("entity/chest/chest_3.png");

        for(int i = 0; i < inventory.getLength(); i++){
            super.addAttribute(inventory.getAttribute(i));
        }
    }

    @Override
    public void Update() {
        super.setHighlight(frames[index]);
        if(PhysicsEngine.intersects(collision, "player")){
            if(Keyboard.E){
                if(debounce){
                    this.open.setData(!this.open.getData());
                    //Open
                    if(this.open.getData()){
                        TowerOfPuzzles.player.openInventory();
                        TowerOfPuzzles.openUI(new Inventory(TowerOfPuzzles.WIDTH/2,TowerOfPuzzles.HEIGHT/2,TowerOfPuzzles.player.inventory, this.inventory));
                    }else{
                        TowerOfPuzzles.player.closeInventory();
                        TowerOfPuzzles.closeUI();
                    }
                    debounce = false;
                }
            }else{
                debounce = true;
            }
        }else{
            if(Keyboard.E){
                if(this.open.getData()){
                    TowerOfPuzzles.player.closeInventory();
                    TowerOfPuzzles.closeUI();this.open.setData(false);
                    this.open.setData(false);
                }
            }

        }
        
        if(open.getData()){
            degreeOffset+=(30.0f/60.0f);
            if(this.ticks<this.maxTicks){
                this.ticks++;
            }
        }else{
            if(this.ticks>0){
                this.ticks--;
            }
        }
        index = (int)((((float)ticks)/((float)maxTicks))*3.0f);
    }

    @Override
    public void Render(Graphics g) {
        frames[index].render(x, y, width, height, g);
    }
    
    @Override
    public String save() {
        SaveManager.saveFile(TowerOfPuzzles.RoomPath+super.getAdditionalSavePath(), this.inventory.genSave().split(";"));
        return this.getType()+"{"+this.x+","+(this.y)+","+this.open.getData()+"}"+super.getAdditionalSavePath();
    }

    @Override
    public void event() {
        
    }
    
    @Override
    public void onAttributeChange(){
        super.setExtraSavePath(this.inventory.getFileNameAttribute().getData());
        if(this.inventory.getLength()!=this.inventory.getNumberOfAttributes()){
            for(Attribute a: this.inventory.getAttributes()){
                super.removeAttribute(a);
            }
            this.inventory.setSize(this.inventory.getLength());
            for(Attribute a: this.inventory.getAttributes()){
                super.addAttribute(a);
            }
        }
    }
    
}
