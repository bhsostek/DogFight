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
import Graphics.SpriteUtils;
import java.awt.Graphics;
import World.Tiles.TileConstants;

/**
 *
 * @author Bailey
 */
public class EntityFloorLantern extends Entity{

    private Sprite sprite;
    
    int initialSize = 24;
    float spread = 6.0f;
    
    float target = initialSize;
    
    private Attribute<Integer> r = new Attribute<Integer>("red:", 0);
    private Attribute<Integer> g = new Attribute<Integer>("green:", 0);
    private Attribute<Integer> b = new Attribute<Integer>("blue:", 0);
    
    EntityParticleEmitter pe1;
    
    EntityPositionalAudio epa;
    
    EntityLight light1;

    
    public EntityFloorLantern(int x, int y, int r, int g, int b) {
        super(x-(8 * 4), y-(40 * 4), TileConstants.SCALE, TileConstants.SCALE*3, EnumEntityType.FLOOR_LANTERN);
        sprite = TowerOfPuzzles.spriteBinder.loadSprite("entity/light/floorLamp_1.png");
        
        pe1 = new EntityParticleEmitter((int)super.x+2,(int)super.y-(10*4)+2,"lamp");

        float scale = 0.2f;
        
        pe1.setBoundXBottom(-0.2f);
        pe1.setBoundXTop(0.2f);
        pe1.setBoundYBottom(-scale);
        pe1.setBoundYTop(-scale);
        
        epa = new EntityPositionalAudio((int)super.x+2, (int)super.y-(10 * 4)+2, "torch.wav", 300, true);
        
        this.r.setData(r);
        this.g.setData(g);
        this.b.setData(b);
        
        light1 = new EntityLight((int)super.x,(int)super.y-(10 * 4)+2,initialSize, this.r.getData(),this.g.getData(),this.b.getData());
        
        pe1.link(this);
        epa.link(this);
        light1.link(this);

        Sprite particleSprite = TowerOfPuzzles.spriteBinder.loadSprite("particles/default.png");
        pe1.setSprite(SpriteUtils.overlayGrayscale(particleSprite, new int[]{r,g,b,255}));
        
        super.addAttribute(this.r);
        super.addAttribute(this.g);
        super.addAttribute(this.b);
    }
    
    @Override
    public void onMove(){
        pe1.offsetToLinked();
        epa.offsetToLinked();
        light1.offsetToLinked();
    }

    @Override
    public void onAdd(){
        pe1.onAdd();
        epa.onAdd();
        light1.onAdd();
    }
    
    @Override
    public void onRemove(){
        pe1.onRemove();
        epa.onRemove();
        light1.onRemove();
    }
    
    @Override
    public void onAttributeChange(){
        light1.setColor(new int[]{r.getData(),g.getData(),b.getData()});
        Sprite particleSprite = TowerOfPuzzles.spriteBinder.loadSprite("particles/default.png");
        pe1.setSprite(SpriteUtils.overlayGrayscale(particleSprite, new int[]{r.getData(),g.getData(),b.getData(),255}));
    }
    
    @Override
    public void update() {
        pe1.tick();
        epa.tick();
        light1.setSize(initialSize+(int)((Math.random() * this.spread * 2) - this.spread));
        light1.tick();
    }

    @Override
    public void render(Graphics g) {
        epa.render(g);
        sprite.render(x, y, width, height, g);
        pe1.render(g);
    }
    
    @Override
    public String save(){
        return super.getType().name()+"{"+(x+(8 * 4))+","+(y+(40 * 4))+","+r.getData()+","+g.getData()+","+b.getData()+","+super.getID()+"}";
    }
    
}
