/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Controller.EnumButtonType;
import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Base.util.StringUtils;
import Editor.Attribute;
import Graphics.GUI.Inventory;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import Item.Container;
import Item.Item;
import Item.ItemStack;
import World.Room;
import World.Tiles.TileConstants;
import World.WorldManager;
import java.awt.Graphics;
import java.io.File;

/**
 *
 * @author Bailey
 */
public class EntityItemPedestal extends EntityInteractable{
    
    private Sprite sprite;
    
    private Container inventory;
    private boolean open = false;
    private Debouncer debounce = new Debouncer(false);
    
    private float sin_offset = 0.0f;
    
    public EntityItemPedestal(int x, int y, String inventoryData) {
        super(x, y, TileConstants.SCALE * 2, TileConstants.SCALE * 2, EnumEntityType.PEDESTAL_ITEM);
        
        sprite = TowerOfPuzzles.spriteBinder.loadSprite("entity/pedestal/base_alternate.png");
        
        super.setHighlight(sprite);
        
        
        if(super.getID().equals("")){
            super.setID("id:"+Math.random());
        }
        
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
        
        Node[] nodes = new Node[]{
            new Node(0,0),
        };
        
        super.setNodes(nodes);

        super.setExtraSavePath(path);
        
    }

    @Override
    public void Update(){
        Item i = this.inventory.getItemStack(0).item;
        super.getNodes()[0].setPowered(i!=null);
        
        if(super.isHighlighted()){
            if(debounce.risingAction((Keyboard.E||(TowerOfPuzzles.controllerManager.getContoller(0).getButton(EnumButtonType.B)>0.0f)))){
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
            if(debounce.risingAction((Keyboard.E||(TowerOfPuzzles.controllerManager.getContoller(0).getButton(EnumButtonType.B)>0.0f)))){
                if(open){
                    open = false;
                    TowerOfPuzzles.closeUI();
                    TowerOfPuzzles.player.closeInventory();
                }
            } 
        }
        sin_offset+=8.0f;
        sin_offset%=1080.0f;
    }

    @Override
    public void Render(Graphics g) {
        sprite.render(x, y, sprite.pWidth * 4, sprite.pHeight * 4, g);
        Item i = this.inventory.getItemStack(0).item;
        if(i!=null){
            i.renderInWorld((int)x, (int)y-(i.getType().getImage().pHeight * 4)+(int)((Math.sin(sin_offset/180.0f))*(4*4)), g);
        }
    }

    @Override
    public String save() {
        String tmpPath = TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+super.getAdditionalSavePath();
        File f = new File(tmpPath);
        if(!f.exists()) { 
            System.out.println("File:"+tmpPath+" does not exits... Creating file.");
            StringUtils.saveData(tmpPath.replaceFirst(TowerOfPuzzles.Path, ""), saveAdditionalData());
        }
        return this.getType()+"{"+this.x+","+(this.y)+","+super.getID()+"}"+super.getAdditionalSavePath();
    }

    @Override
    public void event() {
        
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
    
    public ItemStack getItem(){
        return this.inventory.getItemStack(0);
    }
    
}
