/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteBinder;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityGrate extends Entity{

    Sprite grate;
    int total = 20;
    int countdown = total;

    EntityParticleEmitter pe1;
    
    public EntityGrate(int x, int y) {
        super(x, y, 48 * 4, 32 * 4, EnumEntityType.GRATE);
        grate = TowerOfPuzzles.spriteBinder.loadSprite("entity/grate/grate.png");
        Node[] nodes = new Node[]{
            new Node(0,0),
        };
        super.setNodes(nodes);
        
        pe1 = new EntityParticleEmitter((int)super.x,(int)super.y+(16 * 4),"water");
        pe1.setOffsetXBottom((16 * 4));
        pe1.setOffsetXTop(-(16 * 4));
    }

    @Override
    public void update() {
        pe1.setCanAdd(super.getNodes()[0].isPowered());
        if(super.getNodes()[0].isPowered()){
            if(countdown == 0){
                TowerOfPuzzles.entityManager.add(new EntityWaterDrop((int)x, (int)y+(16 * 4)));
                countdown = total;
            }else{
                countdown--;
            }
        }
        pe1.tick();
    }

    @Override
    public void render(Graphics g) {
        grate.render(x, y, width, height, g);

        pe1.render(g);
        
    }
    
}
