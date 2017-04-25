/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Base.util.StringUtils;
import Editor.Attribute;
import Editor.SaveManager;
import Graphics.GUI.Inventory;
import Graphics.Sprite;
import Item.Container;
import Quest.Chat.Message;
import World.Tiles.TileConstants;
import World.WorldManager;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityBarrel extends EntityInteractable{

    private final Sprite barrel;
    private Container inventory;
    private boolean open = false;
    private Debouncer debounce = new Debouncer(false);
    
    public EntityBarrel(int x, int y, String inventoryData) {
        super(x, y, TileConstants.SCALE*2, TileConstants.SCALE*2, EnumEntityType.BARREL);
        barrel = TowerOfPuzzles.spriteBinder.loadSprite("entity/barrel/barrel.png");
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
        super.setHighlight(barrel);
    }

    @Override
    public void Update(){
        if(super.isHighlighted()){
            if(debounce.risingAction(Keyboard.E)){
                open = !open;
                if(open){
                    TowerOfPuzzles.openUI(new Inventory(TowerOfPuzzles.WIDTH/2, TowerOfPuzzles.HEIGHT/2, TowerOfPuzzles.player.inventory, this.inventory));
                    TowerOfPuzzles.player.openInventory();
                }else{
                    TowerOfPuzzles.closeUI();
                    TowerOfPuzzles.player.closeInventory();
                }
            } 
        }else{
            if(debounce.risingAction(Keyboard.E)){
                if(open){
                    open = false;
                    TowerOfPuzzles.closeUI();
                    TowerOfPuzzles.player.closeInventory();
                }
            } 
        }
    }

    @Override
    public void Render(Graphics g) {
        barrel.render(x,y,width,height,g);
    }

    @Override
    public void event() {
        TowerOfPuzzles.chat.addMessage(new Message().setName("Barrel").setNameColor(Color.decode("#545021")).setMessage(new String[]{"Hi", "I", "Am", "a", "Barrel!"}));
    }
    @Override
    public String save() {
        SaveManager.saveFile(TowerOfPuzzles.RoomPath+super.getAdditionalSavePath(), this.inventory.genSave().split(";"));
        return this.getType()+"{"+this.x+","+(this.y)+"}"+super.getAdditionalSavePath();
    }
    
    @Override
    public String[] saveAdditionalData(){
        return this.inventory.genSave().split(";");
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
