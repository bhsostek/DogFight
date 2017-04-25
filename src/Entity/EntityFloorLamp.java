/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import java.awt.Graphics;
import World.Tiles.TileConstants;

/**
 *
 * @author Bailey
 */
public class EntityFloorLamp extends Entity{

    private Sprite sprite;
    
    int initialSize = 24;
    float spread = 6.0f;
    
    float target = initialSize;
    
    private Attribute<Integer> r = new Attribute<Integer>("red:", 0);
    private Attribute<Integer> g = new Attribute<Integer>("green:", 0);
    private Attribute<Integer> b = new Attribute<Integer>("blue:", 0);
    
    EntityParticleEmitter pe1;
    EntityParticleEmitter pe2;
    EntityParticleEmitter pe3;
    
    EntityPositionalAudio epa;
    
    EntityLight light1;
    EntityLight light2;
    EntityLight light3;
    
    public EntityFloorLamp(int x, int y, int r, int g, int b) {
        super(x-(8 * 4), y-(40 * 4), TileConstants.SCALE, TileConstants.SCALE*3, EnumEntityType.FLOOR_LAMP);
        sprite = TowerOfPuzzles.spriteBinder.loadSprite("entity/light/floorLamp_0.png");
        
        pe1 = new EntityParticleEmitter((int)super.x,(int)super.y-22*4+2,"torch");
        pe2 = new EntityParticleEmitter((int)super.x-6*4,(int)super.y-17*4-4,"torch");
        pe3 = new EntityParticleEmitter((int)super.x+6*4,(int)super.y-17*4-4,"torch");
        
        epa = new EntityPositionalAudio((int)super.x, (int)super.y-(16 * 4), "torch.wav", 300, true);
        
        this.r.setData(r);
        this.g.setData(g);
        this.b.setData(b);
        
        light1 = new EntityLight((int)super.x-2,(int)super.y-22*4+2,initialSize, this.r.getData(),this.g.getData(),this.b.getData());
        light2 = new EntityLight((int)super.x-6*4-2,(int)super.y-17*4-4,initialSize, this.r.getData(),this.g.getData(),this.b.getData());
        light3 = new EntityLight((int)super.x+6*4-2,(int)super.y-17*4-4,initialSize, this.r.getData(),this.g.getData(),this.b.getData());
        
        pe1.link(this);
        pe2.link(this);
        pe3.link(this);
        epa.link(this);
        light1.link(this);
        light2.link(this);
        light3.link(this);
        
        super.addAttribute(this.r);
        super.addAttribute(this.g);
        super.addAttribute(this.b);
    }
    
    @Override
    public void onMove(){
        pe1.offsetToLinked();
        pe2.offsetToLinked();
        pe3.offsetToLinked();
        epa.offsetToLinked();
        light1.offsetToLinked();
        light2.offsetToLinked();
        light3.offsetToLinked();
    }

    @Override
    public void onAdd(){
        pe1.onAdd();
        pe2.onAdd();
        pe3.onAdd();
        epa.onAdd();
        light1.onAdd();
        light2.onAdd();
        light3.onAdd();
    }
    
    @Override
    public void onRemove(){
        pe1.onRemove();
        pe2.onRemove();
        pe3.onRemove();
        epa.onRemove();
        light1.onRemove();
        light2.onRemove();
        light3.onRemove();
    }
    
    @Override
    public void onAttributeChange(){
        light1.setColor(new int[]{r.getData(),g.getData(),b.getData()});
        light2.setColor(new int[]{r.getData(),g.getData(),b.getData()});
        light3.setColor(new int[]{r.getData(),g.getData(),b.getData()});
    }
    
    @Override
    public void update() {
        pe1.tick();
        pe2.tick();
        pe3.tick();
        epa.tick();
        light1.setSize(initialSize+(int)((Math.random() * this.spread * 2) - this.spread));
        light2.setSize(initialSize+(int)((Math.random() * this.spread * 2) - this.spread));
        light3.setSize(initialSize+(int)((Math.random() * this.spread * 2) - this.spread));
        light1.tick();
        light2.tick();
        light3.tick();
    }

    @Override
    public void render(Graphics g) {
        sprite.render(x, y, width, height, g);
        pe1.render(g);
        pe2.render(g);
        pe3.render(g);
        epa.render(g);
        light1.render(g);
        light2.render(g);
        light3.render(g);
    }
    
    @Override
    public String save(){
        return super.getType().name()+"{"+(x+(8 * 4))+","+(y+(40 * 4))+","+r.getData()+","+g.getData()+","+b.getData()+","+super.getID()+"}";
    }
    
}
