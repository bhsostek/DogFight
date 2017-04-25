/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Graphics.PixelUtils;
import Lighting.Light;
import Lighting.LightingEngine;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityLight extends Entity{

    Light light;
    
    boolean last = false;
    
    Attribute<Integer> size = new Attribute<Integer>("Size:",0);
    
    
    Attribute<Integer> red = new Attribute<Integer>("R:",0);
    Attribute<Integer> green = new Attribute<Integer>("G:",0);
    Attribute<Integer> blue = new Attribute<Integer>("B:",0);

    
    public EntityLight(int x, int y, int size, int r, int g, int b) {
       super(x, y, TileConstants.SCALE*2, TileConstants.SCALE*2, EnumEntityType.LIGHT);
       
       Node[] nodes = new Node[]{
           new Node(0,0),
       };
       
       super.setNodes(nodes);
       
       //set Data
       this.size.setData(size);
       this.red.setData(r);
       this.green.setData(g);
       this.blue.setData(b);
       
       light = new Light((int)super.x,(int)super.y,this.size.getData(), PixelUtils.makeColor(new int[]{r,g,b,255}));
       
       super.addAttribute(this.size);
       super.addAttribute(red);
       super.addAttribute(green);
       super.addAttribute(blue);
    }
    
    @Override
    public void onAdd(){
        TowerOfPuzzles.lightingEngine.addLight(light);
    }
    
    @Override
    public void onRemove(){
        TowerOfPuzzles.lightingEngine.removeLight(light);
    }

    @Override
    public void update() {
        if(super.getNodes()[0].isPowered() != last){
            if(super.getNodes()[0].isPowered()){
                TowerOfPuzzles.lightingEngine.removeLight(light);
            }else{
                TowerOfPuzzles.lightingEngine.addLight(light);
            }
        }
        last = super.getNodes()[0].isPowered();
    }

    @Override
    public void onAttributeChange(){
        light.setSize(size.getData());
        light.setColor(PixelUtils.makeColor(new int[]{red.getData(),green.getData(),blue.getData(),255}));
    }
    
    public void setSize(int size){
        this.size.setData(size);
        this.onAttributeChange();
    }
    
    public void setColor(int[] color){
        this.red.setData(color[0]);
        this.green.setData(color[1]);
        this.blue.setData(color[2]);
        this.onAttributeChange();
    }
    
    public void setColor(int color){
        int[] colors = PixelUtils.getRGBA(color);
        this.red.setData(colors[0]);
        this.green.setData(colors[1]);
        this.blue.setData(colors[2]);
        this.onAttributeChange();
    }
    
    @Override
    public void onMove(){
        light.setX((int)x);
        light.setY((int)y);
    }
    
    @Override
    public void render(Graphics g) {
        
    }
    
    @Override
    public String save(){
        return super.getType()+"{"+super.x+","+super.y+","+size.getData()+","+red.getData()+","+green.getData()+","+blue.getData()+","+super.getID()+"}";
    }
    
}
