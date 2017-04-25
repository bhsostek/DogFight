/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Base.TowerOfPuzzles;
import Base.util.StringUtils;
import Editor.SaveManager;
import Entity.Entity;
import Entity.EntityManager;
import Entity.EnumEntityType;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import World.Tiles.BasicTile;
import World.Tiles.EnumTile;
import World.Tiles.Tile;
import World.Tiles.TileConstants;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Bailey
 */
public class Room {
    int x;
    int y;
    private int width;
    private int height;
    private Tile[][][] tiles;
    
    private LinkedList<RigidBody> obj_collisions = new LinkedList<RigidBody>();
    
    String path;
    private final String name;
    
    //Data
    String[] data;
    String[] init       = new String[]{};
    String[] background = new String[]{};
    String[] foreground = new String[]{};
    String[] entities   = new String[]{};
    LinkedList<Entity> obj_entities = new LinkedList<Entity>();
    String[] collisions = new String[]{};
    
    HashMap<String,String> macros = new HashMap<String,String>();
    
//    EntityParticleEmitter pe;
    public Room(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        tiles = new Tile[height][width][2];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                for(int k = 0; k < 2; k++){
                    tiles[j][i][k] = new BasicTile(EnumTile.NULL, (i+this.x)*TileConstants.SCALE, (j+this.y)*TileConstants.SCALE);
                }
            }
        }
        name = "Undefined";
    }
    
    public Room(int x, int y, String path){
        this.x = x;
        this.y = y;
        //add the.txt
   

        this.path = path;
        
        if(this.path.contains("/")){
            name = this.path.split("/")[1].replace(".txt", "");
        }else{
            name = this.path.replace(".txt", "");
        }
        //remove all tabs from the front
        try{
            this.data = StringUtils.loadData(TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+"/"+this.path+"/room.txt");
            for(int i=0; i<this.data.length; i++){

//                System.err.println("Data at["+i+"]"+this.data[i]);
                
                this.data[i] = StringUtils.removeFrontSpacing(data[i]);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //Determine the init
        for(int i = 0; i < data.length; i++){
            if(data[i].startsWith("init:{")){
                i++;
                while(!data[i].startsWith("}")){
                    this.init = StringUtils.addLine(init, data[i]);
                    i++;
                }
            }
            if(data[i].startsWith("background:{")){
                i++;
                while(!data[i].startsWith("}")){
                    this.background = StringUtils.addLine(background, data[i]);
                    i++;
                }
            }
            if(data[i].startsWith("foreground:{")){
                i++;
                while(!data[i].startsWith("}")){
                    this.foreground = StringUtils.addLine(foreground, data[i]);
                    i++;
                }
            }
            if(data[i].startsWith("collisions:{")){
                i++;
                while(!data[i].startsWith("}")){
                    this.collisions = StringUtils.addLine(collisions, data[i]);
                    i++;
                }
            }
            if(data[i].startsWith("entities:{")){
                i++;
                while(!data[i].startsWith("}")){
                    this.entities = StringUtils.addLine(entities, data[i]);
                    i++;
                }
            }
        }
        //Analize init
        for(int i = 0; i < init.length; i++){
            if(init[i].startsWith("width=")){
                this.width = Integer.parseInt(init[i].replaceAll("width=", ""));
            }
            if(init[i].startsWith("height=")){
                this.height = Integer.parseInt(init[i].replaceAll("height=", ""));
            }
            if(init[i].startsWith("macro:")){
                String tmpData = init[i].replaceAll("macro:", "");
                macros.put(tmpData.split(",")[0], tmpData.split(",")[1]);
            }
        }
        //set Dust spawn
//        pe.setOffsetXBottom(this.x * TileConstants.SCALE);
//        pe.setOffsetYBottom(this.y * TileConstants.SCALE);
//        pe.setOffsetXTop((this.width + this.x) * TileConstants.SCALE);
//        pe.setOffsetYTop((this.height + this.y) * TileConstants.SCALE);
//        pe.setNumParticles(this.width * this.height);
        
        //Process Entity Data
        PhysicsEngine.activeChannel = "solids";
        for(int i = 0; i < collisions.length; i++){
            String metadata = collisions[i];
            {
                //add in this x and this y to the position of x and y
                //T formatting
                String[] deconstructedMetadata = metadata.split(",");
                for(int j = 0; j < deconstructedMetadata.length; j++){
                    if(deconstructedMetadata[j].endsWith("t")){
                        deconstructedMetadata[j] =deconstructedMetadata[j].replaceAll("t", "");
                        deconstructedMetadata[j] = ""+(Integer.parseInt(deconstructedMetadata[j])*TileConstants.SCALE);
                    }
                }
                //x
                deconstructedMetadata[0] = ""+(Integer.parseInt(deconstructedMetadata[0])+(this.x * TileConstants.SCALE));
                //y
                deconstructedMetadata[1] = ""+(Integer.parseInt(deconstructedMetadata[1])+(this.y * TileConstants.SCALE));
                //reconstruct
                String newLine = "";
                for(int j = 0; j < deconstructedMetadata.length; j++){
                    if(j>0){
                        newLine = newLine+","+deconstructedMetadata[j];
                    }else{
                        newLine = newLine+deconstructedMetadata[j];
                    }
                }
                //setIt
                metadata = newLine;
            }
            String[] collisionData = metadata.split(",");
            obj_collisions.add(PrebuiltBodies.quad(new Point2D(Integer.parseInt(collisionData[0])+Integer.parseInt(collisionData[2])/2,Integer.parseInt(collisionData[1])+Integer.parseInt(collisionData[3])/2), Integer.parseInt(collisionData[2]), Integer.parseInt(collisionData[3])));
        }
        
        //Process Entity Data
        for(int i = 0; i < entities.length; i++){
            String line = entities[i];
            String type = line.split("\\{")[0];
            String metadata = line.split("\\{")[1].split("\\}")[0];
            int hasExtraData = line.split("\\{")[1].split("\\}").length;
            String extraData;
            if(hasExtraData>=2){
                extraData = line.substring(line.indexOf("}"), line.length()).replaceFirst("}", "");
            }else{
                extraData = "";
            }

            {
                //add in this x and this y to the position of x and y
                //T formatting
                if(!type.equals("WIRE")){
                    String[] deconstructedMetadata = metadata.split(",");
                    for(int j = 0; j < deconstructedMetadata.length; j++){
                        if(deconstructedMetadata[j].endsWith("t")){
                            if(StringUtils.isNumber(deconstructedMetadata[j].replaceAll("t", ""))){
                                deconstructedMetadata[j] =deconstructedMetadata[j].replaceAll("t", "");
                                deconstructedMetadata[j] = ""+(Integer.parseInt(deconstructedMetadata[j])*TileConstants.SCALE);   
                            }
                        }
                    }

                    //x
                    try{
                        deconstructedMetadata[0] = ""+(Integer.parseInt(deconstructedMetadata[0])+(this.x * TileConstants.SCALE));
                    }catch(java.lang.NumberFormatException e){

                    }

                    //y
                    try{
                        deconstructedMetadata[1] = ""+(Integer.parseInt(deconstructedMetadata[1])+(this.y * TileConstants.SCALE));
                    }catch(java.lang.NumberFormatException e){

                    }catch(java.lang.ArrayIndexOutOfBoundsException e){
                        //Only 1 paramater input
                    }
                    //reconstruct
                    String newLine = "";
                    for(int j = 0; j < deconstructedMetadata.length; j++){
                        if(j>0){
                            newLine = newLine+","+deconstructedMetadata[j];
                        }else{
                            newLine = newLine+deconstructedMetadata[j];
                        }
                    }
                    //setIt
                    metadata = newLine;
                }
            }
            EnumEntityType eet = EnumEntityType.valueOf(type);
            if(eet != null){
                Entity e = EntityManager.CreateEntity(EnumEntityType.valueOf(type), metadata, extraData);
                if(e!=null){
                    obj_entities.add(e);
                }
            }
        }

        tiles = new Tile[height][width][2];
        
        //foreground
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                String tmpData = foreground[j].split(",")[i];
                if(macros.containsKey(tmpData)){
                    tmpData=macros.get(tmpData);
                }
                tiles[j][i][1] = EnumTile.valueOf(tmpData).generate((i+this.x)*TileConstants.SCALE, (j+this.y)*TileConstants.SCALE);
            }
        }
        
        //background
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                String tmpData = background[j].split(",")[i];
                if(macros.containsKey(tmpData)){
                    tmpData=macros.get(tmpData);
                }
                tiles[j][i][0] = EnumTile.valueOf(tmpData).generate((i+this.x)*TileConstants.SCALE, (j+this.y)*TileConstants.SCALE);
            }
        }
    }
    
    public void tick(){

    }
    
    public void render(Graphics g){
       for(int j = -2; j < (TowerOfPuzzles.HEIGHT / TileConstants.SCALE)+2; j++){
           for(int i = -2; i < (TowerOfPuzzles.WIDTH / TileConstants.SCALE)+2; i++){
               if(j+((int)TowerOfPuzzles.cam.y / TileConstants.SCALE) -y > -1 && j+((int)TowerOfPuzzles.cam.y / TileConstants.SCALE)-y <= height-1){
                   if(i+((int)TowerOfPuzzles.cam.x / TileConstants.SCALE) -x > -1 && i+((int)TowerOfPuzzles.cam.x / TileConstants.SCALE)-x <= width-1){
                       tiles[j+((int)TowerOfPuzzles.cam.y / TileConstants.SCALE)-y][i+((int)TowerOfPuzzles.cam.x / TileConstants.SCALE)-x][0].render(g);
                   }
               }
           }
       }
            
       for(int j = -2; j < (TowerOfPuzzles.HEIGHT / TileConstants.SCALE)+2; j++){
           for(int i = -2; i < (TowerOfPuzzles.WIDTH / TileConstants.SCALE)+2; i++){
               if(j+((int)TowerOfPuzzles.cam.y / TileConstants.SCALE) -y > -1 && j+((int)TowerOfPuzzles.cam.y / TileConstants.SCALE)-y <= height-1){
                   if(i+((int)TowerOfPuzzles.cam.x / TileConstants.SCALE) -x > -1 && i+((int)TowerOfPuzzles.cam.x / TileConstants.SCALE)-x <= width-1){
                       tiles[j+((int)TowerOfPuzzles.cam.y / TileConstants.SCALE)-y][i+((int)TowerOfPuzzles.cam.x / TileConstants.SCALE)-x][1].render(g);
                   }
               }
           }
       }
   }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public void onRemove(){
        //Add entities to the world
        for(Entity e:obj_entities){
            TowerOfPuzzles.entityManager.remove(e);
        }
        for(RigidBody e:obj_collisions){
            PhysicsEngine.getChannel("solids").remove(e);
            PhysicsEngine.getChannel("toAdd").remove(e);
        }
        try{
            for(int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    for(int k = 0; k < 2; k++){
                        tiles[i][j][k].onRemove();
                    }
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
//        EntityManager.remove(pe);
    }
    
    public void onAdd(){
        //Add entities to the world
        for(Entity e:obj_entities){
            TowerOfPuzzles.entityManager.add(e);
        }
        for(RigidBody e:obj_collisions){
            PhysicsEngine.addToChannel("solids", e);
            PhysicsEngine.addToChannel("toAdd", e);
        }
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                for(int k = 0; k < 2; k++){
                    tiles[j][i][k].onAdd();
                }
            }
        }
//        EntityManager.add(pe);
    }
    
    public void save(){
        SaveManager.saveRoomAs(this.getName(), this);
    }
    
    public void load(){
        
    }
    
    public void setTile(int j, int i, int k, EnumTile et){
        if(j<this.height && j >= 0 ){
            if(i<this.width && i >= 0 ){
                this.tiles[j][i][k].setTile(et);
            }
        }
    }
    
    public Tile[][][] getTiles(){
        return this.tiles;
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getXInWorld(){
        return this.x;
    }
    
    public int getYInWorld(){
        return this.y;
    }
    
    public Rectangle getCollision(){
        return new Rectangle(x, y, this.width * TileConstants.SCALE, this.height * TileConstants.SCALE);
    }
}
