/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Physics.PhysicsEngine;
import Physics.Point2D;
import World.Tiles.TileConstants;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityText extends Entity{

    private String data;
    
    //2 seconds
    private int lifespan = 120;
    
    private float velY = -10.0f-(float)(5.0f*Math.random());
    private float velX = (10.0f * (float)Math.random())-5.0f;
    
    private boolean landed = false;
    
    public EntityText(int x, int y, String data) {
        super(x, y, TileConstants.SCALE/2, TileConstants.SCALE/2, EnumEntityType.TEXT);
        this.data = data;
    }

    @Override
    public void update() {
        if(!landed){
            this.y+=this.velY;
            this.x+=this.velX;
            this.velY+=TowerOfPuzzles.GRAVITY;
            super.moveTo((int)x, (int)y);
            if(PhysicsEngine.intersects(collision, "solids")){
                this.landed = true;
                this.velX = 0;
                this.velY = 0;
            }
        }
        lifespan--;
        if(lifespan<=0){
            TowerOfPuzzles.entityManager.remove(this);
        }
    }

    @Override
    public void render(Graphics g) {
        TowerOfPuzzles.textRenderer.drawString(data, (int)x, (int)y, g);
        g.setColor(Color.red);
        g.drawOval((int)x-1, (int)y-1, 3, 3);
    }
    
}
