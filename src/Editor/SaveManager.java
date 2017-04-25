/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Base.TowerOfPuzzles;
import Base.util.StringUtils;
import Entity.Entity;
import Entity.EntityManager;
import Physics.PhysicsEngine;
import Physics.RigidBody;
import World.Room;
import World.Tiles.EnumTile;
import World.Tiles.Tile;
import World.Tiles.TileConstants;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Bailey
 */
public class SaveManager {
    
    
    public static void saveRoomAs(String string, Room room){
        //check if room folder exists
        File dir = new File(TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+string);
        if(!dir.exists()){
            dir.mkdir();
            File saves = new File(TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+string+"/saves");
            File scripts = new File(TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+string+"/scripts");
            saves.mkdir();
            scripts.mkdir();
        }
        System.out.println("Saving room as:"+TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+string+"/room.txt");
        try{
            PrintWriter writer = new PrintWriter(TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+string+"/room.txt", "UTF-8");
            //Room init info
            writer.println("init:{");
                writer.println("    width="+room.getWidth());
                writer.println("    height="+room.getHeight());
                //Macro calculations here
                HashMap<String, EnumTile> macros = calculateMacros(room.getTiles(), room.getWidth(), room.getHeight());
                Iterator it = macros.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    writer.println("    macro:" + pair.getKey() + "," + pair.getValue());
                    it.remove(); // avoids a ConcurrentModificationException
                }     
                macros = calculateMacros(room.getTiles(), room.getWidth(), room.getHeight());
            writer.println("}");
            writer.println("");
            
            //Background
            writer.println("background:{");
                String[] bgData = calculateBackground(macros, room.getTiles(), room.getWidth(), room.getHeight());
                for(int i = 0; i < bgData.length; i++){
                    writer.println("    "+bgData[i]);
                }
            writer.println("}");
            writer.println("");
            
            //Foreground
            writer.println("foreground:{");
                String[] fgData = calculateForeground(macros, room.getTiles(), room.getWidth(), room.getHeight());
                for(int i = 0; i < fgData.length; i++){
                    writer.println("    "+fgData[i]);
                }
            writer.println("}");
            writer.println("");
            
            //Collisions
            writer.println("collisions:{");
                RigidBody[] bodies = PhysicsEngine.getChannel("toAdd").collisons;
                for(RigidBody bod : bodies){
                    writer.println("    "+(int)((bod.x-(bod.getCollision().getBounds().width/2))/TileConstants.SCALE)+"t,"
                                         +(int)((bod.y-(bod.getCollision().getBounds().height/2))/TileConstants.SCALE)+"t,"
                                         +(int)(((bod.getCollision().getBounds().width))/TileConstants.SCALE)+"t,"
                                         +(int)(((bod.getCollision().getBounds().height))/TileConstants.SCALE)+"t");
                }
            writer.println("}");
            writer.println("");
            
            //Entities
            writer.println("entities:{");
                String[] entityOut = EntityManager.genSaveData();
                for(int i = 1; i < entityOut.length; i++){
                    writer.println("    "+entityOut[i]);
                }
            writer.println("}");
            writer.println("");
            
            writer.close();
        } catch (Exception e) {
           System.err.println("The File cannot be saved to that Destination");
           e.printStackTrace();
        }
    }
    
    public static void saveInventory(){
        try{
            PrintWriter writer = new PrintWriter(TowerOfPuzzles.Path+TowerOfPuzzles.SavePath+"/inventory.txt", "UTF-8");
            String[] outData = TowerOfPuzzles.player.genInventorySave().split(";");
            for(int i = 0; i < outData.length; i++){
                writer.println(outData[i]);   
            }
            writer.close();
        } catch (Exception e) {
           System.err.println("The File cannot be saved to that Destination");
           e.printStackTrace();
        }
    }
    
    private static HashMap<String, EnumTile> calculateMacros(Tile[][][] tiles, int width, int height){
        HashMap<String, EnumTile> out = new HashMap<String, EnumTile>();
        int index = 65;
        
        for(int j = 0; j < height; j++){
            for(int i = 0; i < width; i++){
                for(int k = 0; k < 2; k++){
                    EnumTile et = tiles[j][i][k].getEnumTile();
                    if(!out.containsValue(et)){
                        out.put(Character.toString((char)index), et);
                        index++;
                    }
                }
            }
        }
        
        return out;
    }
    
    private static String[] calculateBackground(HashMap<String, EnumTile> macros, Tile[][][] tiles, int width, int height){
        
        String[] out = new String[height];
        
        for(int i = 0; i < out.length; i++){
            out[i] = "";
        }
        
        for(int j = 0; j < height; j++){
            for(int i = 0; i < width; i++){
                int index = 0;
                for(int l = 0; l < macros.size(); l++){
                    EnumTile test = (EnumTile) macros.values().toArray()[l]; 
                    if(test.equals(tiles[j][i][0].getEnumTile())){
                        index = l;
                        break;
                    }
                }
                out[j]+=Character.toString((char)(index+65));
                if(i<width-1){
                    out[j]+=",";
                }
            }
        }
        
        return out;
    }
    
    public static void saveFile(String path, String[] data){
        try{
            PrintWriter writer = new PrintWriter(TowerOfPuzzles.Path+path, "UTF-8");
            for(int i = 0; i < data.length; i++){
                writer.println(data[i]);
            }
            writer.close();
        } catch (Exception e) {
           System.err.println("The File:"+path+" cannot be saved to that Destination");
           e.printStackTrace();
        }
    }
    
    public static boolean fileExists(String path){
        File file = new File(TowerOfPuzzles.Path+path);
        return file.exists();
    }
    
    private static String[] calculateForeground(HashMap<String, EnumTile> macros, Tile[][][] tiles, int width, int height){
        
        String[] out = new String[height];
        
        for(int i = 0; i < out.length; i++){
            out[i] = "";
        }
        
        for(int j = 0; j < height; j++){
            for(int i = 0; i < width; i++){
                int index = 0;
                for(int l = 0; l < macros.size(); l++){
                    EnumTile test = (EnumTile) macros.values().toArray()[l]; 
                    if(test.equals(tiles[j][i][1].getEnumTile())){
                        index = l;
                        break;
                    }
                }
                out[j]+=Character.toString((char)(index+65));
                if(i<width-1){
                    out[j]+=",";
                }
            }
        }
        
        return out;
    }
    
//saves chests and anything that requires additional save data
    public static void saveDynamicEntities(){
        for(Entity e : EntityManager.getEntities()){
            if(e.isDynamic()){
                String path = e.getAdditionalSavePath();
                if(!path.contains(".txt")){
                    path = path+".txt";
                }
                StringUtils.saveData(TowerOfPuzzles.SavePath+path, e.saveAdditionalData());
            }
        }
    }
}
