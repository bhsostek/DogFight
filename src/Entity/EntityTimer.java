/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.awt.Graphics;

import Editor.Attribute;
import World.Tiles.TileConstants;


/**
 *
 * @author Bailey
 */
public class EntityTimer extends Entity{
    
    
    private Attribute<Integer> countdown = new Attribute<Integer>("Count:", 0).setCanEdit(false);
    public Attribute<Integer> initialCountdown = new Attribute<Integer>("Coundown:", 0);

    public EntityTimer(int x, int y, int ticks) {
        super(x, y, TileConstants.SCALE*2, TileConstants.SCALE*2, EnumEntityType.TIMER);
        countdown.setData(ticks);
        initialCountdown.setData(ticks);
        
        Node[] nodes = new Node[]{
            new Node(0,-4*4),
            new Node(0,4*4),
        };
        
        super.addAttribute(initialCountdown);
        super.addAttribute(countdown);
        
        super.setNodes(nodes);
        
    }

    @Override
    public void update() {
        if(!super.getNodes()[0].isPowered()){
            countdown.setData(initialCountdown.getData());
        }
        
        if(super.getNodes()[0].isPowered() && countdown.getData() > 0){
            super.getNodes()[1].setPowered(true);
            countdown.setData(countdown.getData()-1);
        }else{
            super.getNodes()[1].setPowered(false);
        }
    }

    @Override
    public void render(Graphics g) {
        super.drawNodes(g);
    }
    
    @Override
    public String save(){
    	return super.getType()+"{"+super.x+","+super.y+","+initialCountdown.getData()+","+super.getID()+"}";
    }
    
    @Override
    public void onAttributeChange(){
        countdown.setData(initialCountdown.getData());
    }
}
