/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Controller.EnumButtonType;
import Base.Controller.JavaController;
import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Base.util.StringUtils;
import Editor.Attribute;
import Graphics.GUI.PlayerInventory;
import Graphics.Sprite;
import Item.Container;
import Item.Item;
import Item.ItemStack;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.RigidUtils;
import Physics.Vector3D;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class Player extends Entity{

    Sprite left;
    Sprite right;
    int dir = 0;
    
    RigidBody head;
    RigidBody hitBox;
    RigidBody feet;
    
    private Attribute<Float> speed = new Attribute<Float>("Speed:", 6.4f);
    float climbSpeed = 5;
    float jumpVector = 8.5f;
    float velY = 0.0f;
    
    private boolean canMove = true;
            
    Container inventory;
    private int selectedIndex = 0;
    private boolean inventoryOpen = false;
    private boolean couldMove = canMove;
    private Debouncer openInventory = new Debouncer(inventoryOpen);
    private Debouncer openKey = new Debouncer(Keyboard.TAB);
    private boolean externallyOpened = false;
    private JavaController controller;
    
    public Player(int x, int y, JavaController controller) {
        super(x, y, 18 * 4, 32 * 4, EnumEntityType.PLAYER);

        left = TowerOfPuzzles.spriteBinder.loadSprite("player/left.png");
        right = TowerOfPuzzles.spriteBinder.loadSprite("player/right.png");
        
        PhysicsEngine.addChannel("feet");
        feet = PrebuiltBodies.quad(new Point2D(x, y), 14*4, 8);
        PhysicsEngine.addToActiveChannel(feet);
        PhysicsEngine.addChannel("player");
        hitBox = PrebuiltBodies.quad(new Point2D(x, y), 15*4, super.height-(8*4));
        PhysicsEngine.addToActiveChannel(hitBox);
        PhysicsEngine.addChannel("head");
        head = PrebuiltBodies.quad(new Point2D(x, y), 14*4, 8);
        PhysicsEngine.addToActiveChannel(head);
        feet.ImageIndex = -1;
        
        super.setID("player");
        
        inventory = new Container(StringUtils.loadData(TowerOfPuzzles.Path+TowerOfPuzzles.SavePath+"/inventory.txt"));
        
        for(int i = 0; i < this.inventory.getNumberOfAttributes(); i++){
            super.addAttribute(inventory.getAttribute(i));
        }
        
        this.controller = controller;

    }

    @Override
    public void update() {
        //Inventory
        if(!externallyOpened){
            if(openKey.risingAction(Keyboard.Q)){
                if(!inventoryOpen){
                    TowerOfPuzzles.openUI(new PlayerInventory(TowerOfPuzzles.WIDTH/2, TowerOfPuzzles.HEIGHT/2, this.inventory));
                    inventoryOpen = true;
                }else{
                    TowerOfPuzzles.closeUI();
                    inventoryOpen = false;
                }
            }
        }
        //External open call
        if(openInventory.risingAction(inventoryOpen)){
            this.couldMove = this.canMove;
            this.canMove = false;
            System.out.println("Open Inventory: couldMove:"+couldMove);
        }
        
        if(openInventory.fallingAction(inventoryOpen)){
            this.canMove = this.couldMove;
            System.out.println("Close Inventory: couldMove:"+couldMove);
        }
        
        if(PhysicsEngine.collides("head", "solids")){
            velY = 0;
            while(PhysicsEngine.collides("head", "solids")){
                RigidUtils.Move(new Vector3D(0,1,0), head);
                RigidUtils.Move(new Vector3D(0,1,0), hitBox);
                RigidUtils.Move(new Vector3D(0,1,0), feet);
                y++;
            }
        }
        
        //if not on ladder
        if(!PhysicsEngine.collides("player", "ladder")){
            if(!PhysicsEngine.collides("feet", "solids")){
                if(!PhysicsEngine.collides("feet", "floor")){
                    velY += TowerOfPuzzles.GRAVITY;
                }else{
                    if(Keyboard.S){
                        velY += TowerOfPuzzles.GRAVITY;
                    }else{
                        while(PhysicsEngine.collides("feet", "floor")){
                            RigidUtils.Move(new Vector3D(0,-1,0), feet);
                        }
                        RigidUtils.Move(new Vector3D(0,+1,0), feet);
                        velY = 0;
                        if(canMove){
                            if(Keyboard.W||Keyboard.SPACE||(controller.getButton(EnumButtonType.A)>0.0f)){
                                velY-=jumpVector;
                            }
                        }
                    }
                }
            }else{
                while(PhysicsEngine.collides("feet", "solids")){
                    RigidUtils.Move(new Vector3D(0,-1,0), feet);
                }
                RigidUtils.Move(new Vector3D(0,+1,0), feet);
                velY = 0;
                if(canMove){
                    if(Keyboard.W||Keyboard.SPACE||(controller.getButton(EnumButtonType.A)>0.0f)){
                        velY-=jumpVector;
                    }
                }
            }

            //Gravity
            if(!PhysicsEngine.collides("feet", "water")){
                //Out of Water
                for (int i = 0; i < Math.abs(velY); i++) {
                    int dir = (int) (velY / Math.abs(velY));
                    RigidUtils.Move(new Vector3D(0, dir, 0), feet);
                    
                    if(!Keyboard.S){
                        if(velY>0){
                            if(PhysicsEngine.intersects(feet, "floor")){
                                velY=0;
                            }
                        }
                    }
                    
                    if(PhysicsEngine.intersects(feet, "solids")){
                        velY=0;
                    }
                }
            }else{
                //In water
                for (int i = 0; i < Math.abs(velY); i++) {
                    int dir = (int) (velY / Math.abs(velY));
                    RigidUtils.Move(new Vector3D(0, (float)(dir)/4.0f, 0), feet);
                    if(PhysicsEngine.intersects(feet, "solids")){
                        velY=0;
                    }
                }
            }
        }else{
            //while on ladder
            velY = 0;
            if(canMove){
                if(Keyboard.W||Keyboard.SPACE||(controller.getButton(EnumButtonType.A)>0.0f)){
                    y-=climbSpeed;
                    RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
                    feet.y = y +(16 * 4);
                    head.y = y;
                }
                if(Keyboard.S){
                    if(!PhysicsEngine.collides("feet", "solids")){
                        y+=climbSpeed;
                        RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
                        feet.y = y+(16 * 4);
                        head.y = y;
                    }else{
                        while(PhysicsEngine.collides("feet", "solids")){
                            RigidUtils.Move(new Vector3D(0,-1,0), feet);
                        }
                        RigidUtils.Move(new Vector3D(0,+1,0), feet);
                        velY = 0;
                        if(Keyboard.W){
                            velY-=10.0f;
                        }
                    }
                }
                if(Keyboard.A||(controller.getButton(EnumButtonType.LEFT_STICK_X)<0.0f)){
                    if(!PhysicsEngine.collides("player", "solids")){
                        RigidUtils.Move(new Vector3D(-speed.getData()*Math.abs((controller.getButton(EnumButtonType.LEFT_STICK_X))),0,0), feet);
                    }else{
                        while(PhysicsEngine.collides("player", "solids")){
                            RigidUtils.Move(new Vector3D(1,0,0), hitBox);
                        }
                    }
                    dir = 0;
                }
                if(Keyboard.D||(controller.getButton(EnumButtonType.LEFT_STICK_X)>0.0f)){
                    if(!PhysicsEngine.collides("player", "solids")){
                        RigidUtils.Move(new Vector3D(speed.getData()*(controller.getButton(EnumButtonType.LEFT_STICK_X)),0,0), feet);
                    }else{
                        while(PhysicsEngine.collides("player", "solids")){
                            RigidUtils.Move(new Vector3D(-1,0,0), hitBox);
                        }
                    }
                    dir = 1;
                }
            }
            x = (int)feet.x;
            y = (int)(feet.y - (16 * 4));
            //move head
            RigidUtils.MoveTo(new Vector3D(x,y-(15 * 4),0), head);
            //move chest
            RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
            if(dir == 1){
                while(PhysicsEngine.collides("player", "solids")){
                    x--;
                    RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
                    feet.x = x;
                    head.x = x;
                }
            }
            if(dir == 0){
                while(PhysicsEngine.collides("player", "solids")){
                    x++;
                    RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
                    feet.x = x;
                    head.x = x;
                }
            }
            
            return;
        }
        if(canMove){
            if(Keyboard.S){
                RigidUtils.Move(new Vector3D(0,0,0), feet);
            }
            if(Keyboard.A||(controller.getButton(EnumButtonType.LEFT_STICK_X)<0.0f)){
                if(!PhysicsEngine.collides("player", "solids")){
                    RigidUtils.Move(new Vector3D(-speed.getData()*Math.abs((controller.getButton(EnumButtonType.LEFT_STICK_X))),0,0), feet);
                }else{
                    while(PhysicsEngine.collides("player", "solids")){
                        RigidUtils.Move(new Vector3D(1,0,0), hitBox);
                    }
                }
                dir = 0;
            }
            if(Keyboard.D||(controller.getButton(EnumButtonType.LEFT_STICK_X)>0.0f)){
                if(!PhysicsEngine.collides("player", "solids")){
                    RigidUtils.Move(new Vector3D(speed.getData()*(controller.getButton(EnumButtonType.LEFT_STICK_X)),0,0), feet);
                }else{
                    while(PhysicsEngine.collides("player", "solids")){
                        RigidUtils.Move(new Vector3D(-1,0,0), hitBox);
                    }
                }
                dir = 1;
            }
        }

        x = (int)feet.x;
        y = (int)(feet.y - 16*4);
        //move head
        RigidUtils.MoveTo(new Vector3D(x,y-(15 * 4),0), head);
        //move chest
        RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
        if(dir == 1){
            while(PhysicsEngine.collides("player", "solids")){
                x--;
                RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
                feet.x = x;
                head.x = x;
            }
        }
        if(dir == 0){
            while(PhysicsEngine.collides("player", "solids")){
                x++;
                RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
                feet.x = x;
                head.x = x;
            }
        }
        
        //item stuff
        if(this.inventory.getItemStack(selectedIndex).item != null){
            this.inventory.getItemStack(selectedIndex).item.tickInHand();
        }
    }

    @Override
    public void render(Graphics g) {
        if(dir == 0){
            this.left.render(x, y+(1 * 4), super.width, super.height, g);
        }
        if(dir == 1){
            this.right.render(x, y+(1 * 4), super.width, super.height, g);
        }
        
        //Inventory Render
        if(this.inventoryOpen){
            
        }else{
            Item i = this.inventory.getItemStack(selectedIndex).item;
            if(i != null){
                if(this.dir<=0){
                    i.renderInHand((int)x-(8 * 4), (int)y, g, dir, -(8 * 4));
                }else{
                    i.renderInHand((int)x+(8 * 4), (int)y, g, dir, (8 * 4));
                }
            }
        }

    }
    
    public float getDownwardVel(){
        if(PhysicsEngine.collides("feet", "log")){
            return this.velY/2;
        }
        return this.velY;
    }
    
    public void setDownwardVel(float f){
        this.velY = f;
    }
    
    public void movePlayerTo(int n_x, int n_y){
        //setFeet
        feet.x = n_x;
        feet.y = n_y;
        //MovePlayer
        x = (int)feet.x;
        y = (int)(feet.y - 16*4);
        //move head
        RigidUtils.MoveTo(new Vector3D(x,y-(15 * 4),0), head);
        //move chest
        RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
        this.velY = 0;
    }
    
    public void movePlayerBy(int n_x, int n_y){
        //setFeet
        feet.x += n_x;
        //MovePlayer
        x = (int)feet.x;
        //move head
        RigidUtils.MoveTo(new Vector3D(x,y-(15 * 4),0), head);
        //move chest
        RigidUtils.MoveTo(new Vector3D(x,y,0), hitBox);
        if(n_x>0){
            dir = 1;
        }else{
            dir = 0;
        }
    }
    
    public void setCanMove(boolean b){
        this.canMove = b;
    }
    
    public void addItem(Item item, int count){
        this.inventory.addItemToOpenSlotStartingAtIndex(new ItemStack(item, count), this.selectedIndex);
    }
    
    public void putItem(ItemStack item, int slot){
        this.inventory.putItemStack(item, slot);
    }
    
    public void openInventory(){
        this.inventoryOpen = true;
        externallyOpened = true;
    }
    
    public void closeInventory(){
        this.inventoryOpen = false;
        externallyOpened = false;
    }
    
    public void setSelectedIndex(int i){
        selectedIndex = i;
    }
    
    public int getSelectedIndex(){
        return this.selectedIndex;
    }
    
    public boolean takeItem(Item item, int count){
        int remaining = count;
        if(hasItem(item)>=count){
            for(int i = 0; i < this.inventory.getLength(); i++){
                if(this.inventory.getItemStack(i).item!=null){
                    if(item.genSave().equals(this.inventory.getItemStack(i).item.genSave())){
                        while(remaining>0 && this.inventory.getItemStack(i).count>0){
                            this.inventory.takeItem(i);
                            remaining--;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    public int hasItem(Item item){
        int out = 0;
        for(int i = 0; i < this.inventory.getLength(); i++){
            if(this.inventory.getItemStack(i).item!=null){
                if(item.genSave().equals(this.inventory.getItemStack(i).item.genSave())){
                    out+=this.inventory.getItemStack(i).count;
                }
            }
        }
        return out;
    }
    
    public String genInventorySave(){
        return this.inventory.genSave();
    }
}
