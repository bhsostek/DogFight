/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Base.MouseInput;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Entity.Mob.Attack;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.RigidUtils;
import Physics.Vector3D;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class ItemTrident extends ItemWeapon{
    
    Debouncer mouse = new Debouncer(false);
    
    private final int degree = 45;
    
    int offset = 0;
    int maxOffset = 32;
    
    private RigidBody collision;
    
    private int lastDir = 0;
    
    public ItemTrident(Items eit, String[] data) {
        super(eit, data);
    }

    @Override
    public String[] writeDataToBuffer() {
        return new String[]{};
    }
    
    @Override
    public void renderInHand(int x, int y, Graphics g, int dir, int offset){
        if(dir==0){
            super.eit.getImage().render(x+(int)(Math.cos(Math.toRadians(-degree-90))*offset), y+(int)(Math.sin(Math.toRadians(-degree-90))*offset), eit.getImage().pWidth * 4, eit.getImage().pHeight * 4, g, -degree);
        }
        if(dir==1){
            super.eit.getImage().render(x+(int)(Math.cos(Math.toRadians(degree-90))*offset), y+(int)(Math.sin(Math.toRadians(degree-90))*offset), eit.getImage().pWidth * 4, eit.getImage().pHeight * 4, g, degree);
        }
        lastDir = dir;
    }
    
    @Override
    public void tickInHand(){
        if(offset > 0){
            offset--;
        }
        if(mouse.risingAction(TowerOfPuzzles.mouseInput.IsPressed)){
            offset=maxOffset;
            RigidBody bod = PrebuiltBodies.quad(new Point2D(TowerOfPuzzles.player.getX(), TowerOfPuzzles.player.getY()), this.getType().getImage().pWidth*4, this.getType().getImage().pHeight*4);
            if(lastDir==0){
                RigidUtils.RotateZOnlyPoints(bod, -Math.toRadians(degree));
//                RigidUtils.Update(bod);
                TowerOfPuzzles.attackManager.attacks.add(new Attack(bod, 2.3f, new Vector3D(-5,-5,0)));
            }else{
                RigidUtils.RotateZOnlyPoints(bod, Math.toRadians(degree));
//                RigidUtils.Update(bod);
                TowerOfPuzzles.attackManager.attacks.add(new Attack(bod, 2.3f, new Vector3D(5,-5,0)));   
            }
        }
    }
}
