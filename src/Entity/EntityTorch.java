/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import Lighting.Light;
import java.awt.Graphics;
import Graphics.PixelUtils;
import World.Tiles.TileConstants;

/**
 *
 * @author Bailey
 */
public class EntityTorch extends Entity{

    private Sprite torch;
    private Light[] lights;
    
    
    int initialSize = 64;
    float spread = 6.0f;
    
    float target = initialSize;
    
    EntityParticleEmitter pe;
    
    final int[] colors = new int[]{
        PixelUtils.makeColor(new int[]{255, 0, 0, 128}),
//        PixelUtils.makeColor(new int[]{242, 138, 68, 0}),
//        PixelUtils.makeColor(new int[]{250, 101, 0, 0}),
//        PixelUtils.makeColor(new int[]{255, 210, 0, 0}),
//        PixelUtils.makeColor(new int[]{255, 240, 0, 0}),
//        PixelUtils.makeColor(new int[]{255, 247, 117, 0}),
    };
    
    public EntityTorch(int x, int y) {
        super(x, y, TileConstants.SCALE*2, TileConstants.SCALE*2, EnumEntityType.TORCH);
        torch = TowerOfPuzzles.spriteBinder.loadSprite("entity/light/torch.png");
        lights = new Light[]{new Light(x,y-16,initialSize,colors[(int)(Math.random() * colors.length)])};
        
        pe = new EntityParticleEmitter(x+(1 * 4), y-16,"torch");
        pe.setOffsetXBottom(-2 * 4);
        pe.setOffsetXTop(2 * 4);
        pe.setNumParticles(30);
        pe.setBoundXBottom(-0.3f);
        pe.setBoundXTop(0.3f);
        pe.setBoundYTop(-1.0f);
        
        pe.link(this);
    }
    
    @Override
    public void onMove(){
        pe.offsetToLinked();
    }

    @Override
    public void onAdd(){
//        for(Light light:lights){
//            LightingEngine.addLight(light);
//        }
        pe.onAdd();
    }
    
    @Override
    public void onRemove(){
//        for(Light light:lights){
//            LightingEngine.removeLight(light);
//        }
        pe.onRemove();
    }
    
    @Override
    public void update() {
        for(Light light:lights){
            light.setSize(initialSize+(int)((Math.random() * this.spread * 2) - this.spread));
            light.setColor(colors[(int)(Math.random() * colors.length)]);
        }
        pe.update();
    }

    @Override
    public void render(Graphics g) {
        torch.render(x, y, torch.pWidth*4, torch.pHeight*4, g);
        pe.render(g);
    }
    
}
