/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Graphics.GUI.UI;
import Base.MouseInput;
import Base.MousePositionLocator;
import Base.TowerOfPuzzles;
import Graphics.Sprite;
import World.Room;
import World.Tiles.EnumTile;
import World.Tiles.Tile;
import World.Tiles.TileConstants;
import World.WorldManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class TileChooser extends UI{
    
    int index = 0;
    Sprite[] sprites;
    
    int width = 96;
    Room room;
    
    public TileChooser(int x, int y) {
        super(x, y);
        sprites = new Sprite[EnumTile.values().length];
        for(int i = 0; i < sprites.length; i++){
            sprites[i] = EnumTile.values()[i].getImage();
        }
        
//        room = new Room(0,0,128,128);
          room = new Room(0,0,"Moriarty");
//          room = new Room(0,0,"water_temple");
//          room = new Room(0,0,"Tower");
        
        WorldManager.addRoom(room);
        
        TowerOfPuzzles.cam.gotoPos(TowerOfPuzzles.player.getX(), TowerOfPuzzles.player.getY(), 1);
    }

    public Room getRoom(){
        return room;
    }
    
    @Override
    public void tick() {
        for(int i = 0 ; i < this.sprites.length; i++){
           Rectangle check = new Rectangle(x+((i%(width/(TileConstants.SCALE/2))) * (int)(TileConstants.SCALE/1.5f))-TileConstants.SCALE/4, y+(i/(width/(TileConstants.SCALE/2)) * (TileConstants.SCALE)) + TileConstants.SCALE/4, TileConstants.SCALE/2, TileConstants.SCALE/2); 
           if(MousePositionLocator.MouseLocation.intersects(check)){
               return;
           }
        }
        
        if(TowerOfPuzzles.mouseInput.IsPressed){
            if(!MousePositionLocator.MouseLocation.intersects(Editor.MENU_BAR)){
                int i = (MousePositionLocator.MouseLocation.x + (int)TowerOfPuzzles.cam.x)/TileConstants.SCALE;
                int j = (MousePositionLocator.MouseLocation.y + (int)TowerOfPuzzles.cam.y)/TileConstants.SCALE;
                if(!TowerOfPuzzles.mouseInput.IsRightClick){
                    for (Room room : WorldManager.getRooms()) {
                        room.setTile(j, i, 1,EnumTile.values()[index]);
                    }
                }else{
                    for (Room room : WorldManager.getRooms()) {
                        room.setTile(j, i, 0,EnumTile.values()[index]);
                    }
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {    
        //Tile selector
        g.setColor(Color.lightGray);
        g.fillRect(TowerOfPuzzles.WIDTH-160, y, 160, TowerOfPuzzles.HEIGHT);
        
        for(int i = 0 ; i < this.sprites.length; i++){
            sprites[i].render(x+((i%(width/(TileConstants.SCALE/2))) * (int)(TileConstants.SCALE/1.5f)), y+(i/(width/(TileConstants.SCALE/2)) * (TileConstants.SCALE)) + TileConstants.SCALE/2, TileConstants.SCALE/2, TileConstants.SCALE/2, g);
            if(index == i){
                g.setColor(Color.CYAN);
            }else{
            	g.setColor(Color.black);
            }
            g.drawRect(x+((i%(width/(TileConstants.SCALE/2))) * (int)(TileConstants.SCALE/1.5f))-TileConstants.SCALE/4, y+(i/(width/(TileConstants.SCALE/2)) * (TileConstants.SCALE)) + TileConstants.SCALE/4, TileConstants.SCALE/2, TileConstants.SCALE/2);
        }
        
        
        
        
    }

    @Override
    public void onClick(Rectangle rect) {
        for(int i = 0 ; i < this.sprites.length; i++){
           Rectangle check = new Rectangle(x+((i%(width/(TileConstants.SCALE/2))) * (int)(TileConstants.SCALE/1.5f))-TileConstants.SCALE/4, y+(i/(width/(TileConstants.SCALE/2)) * (TileConstants.SCALE)) + TileConstants.SCALE/4, TileConstants.SCALE/2, TileConstants.SCALE/2); 
           if(rect.intersects(check)){
               index = i;
               return;
           }
        }
        
        int i = (rect.x + (int)TowerOfPuzzles.cam.x)/TileConstants.SCALE;
        int j = (rect.y + (int)TowerOfPuzzles.cam.y)/TileConstants.SCALE;
        
        if(!MousePositionLocator.MouseLocation.intersects(Editor.MENU_BAR)){
            for (Room room : WorldManager.getRooms()) {
                if(TowerOfPuzzles.mouseInput.IsRightClick){
                    room.setTile(j, i, 0,EnumTile.values()[index]);
                }else{
                    room.setTile(j, i, 1,EnumTile.values()[index]);
                }
            }
        }
        
    }
    
    public void changeSize(int newW, int newH){
        WorldManager.removeRoom(room);
        int height = room.getHeight();
        int width = room.getWidth();
        Tile[][][] old = new Tile[height][width][2];
        
        Room newRoom = new Room(0,0,newW, newH);
        for(int j = 0; j < height; j++){
            for(int i = 0; i < width; i++){
                for(int k = 0; k < 2; k++){
                    old[j][i][k] = room.getTiles()[j][i][k];
                    if(j < newH){
                        if(i < newW){
                            newRoom.setTile(j, i, k, old[j][i][k].getEnumTile());
                        }
                    }
                }
            }
        }
        this.room = newRoom;
        WorldManager.addRoom(room);
    }
    
    public void loadNewRoom(){
        
    }
    
}
