/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Base.TowerOfPuzzles;
import Editor.Attribute;
import Entity.EntityText;
import Entity.Mob.Mob;
import Graphics.Sprite;
import Graphics.SpriteUtils;
import Physics.PhysicsEngine;
import Physics.RigidBody;
import Physics.RigidUtils;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class FlipingSpriteComponent extends AIComponent{
    
    private Attribute<String> sprite = new Attribute<String>("Sprite:", null);
    
    private Sprite left;
    private Sprite right;
    
    public FlipingSpriteComponent(Mob entity, String path) {
        super(entity, EnumAIComponent.SPRITE);
        sprite.setData(path);
        super.addAttribute(sprite);
        this.left = TowerOfPuzzles.spriteBinder.loadSprite("entity/mob/"+path);
        this.right = SpriteUtils.flipSpriteHorisontal(this.left);
    }

    @Override
    public void tick() {
        
    }
    
    @Override
    public void render(Graphics g) {
        if(super.mob.getLookingDir().getX()<=0){
            left.render(super.mob.getX(), super.mob.getY(), left.pWidth* 4, left.pHeight*4, g);
        }else{
            right.render(super.mob.getX(), super.mob.getY(), left.pWidth* 4, left.pHeight*4, g);
        }
    }

    @Override
    public String extraData() {
        return this.sprite.getData();
    }
    
}
