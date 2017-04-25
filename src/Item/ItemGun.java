/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Base.Controller.EnumButtonType;
import Base.Engine;

import Base.MousePositionLocator;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Base.util.DistanceCalculator;
import Physics.Vector3D;
import java.awt.Graphics;
import Entity.EntityBullet;
import java.awt.Color;

/**
 *
 * @author Bailey
 */
public class ItemGun extends ItemWeapon{
    
    Debouncer mouse = new Debouncer(false);
    private float wavelength = 1.0f;
    
    float lastX = 0;
    float lastY = 0;
    
    float count = 0.0f;
    
    public ItemGun(Items eit, String[] data) {
        super(eit, data);
    }

    @Override
    public String[] writeDataToBuffer() {
        return new String[]{};
    }
    
    @Override
    public void renderInHand(int x, int y, Graphics g, int dir, int offset){
        Vector3D mouse = TowerOfPuzzles.controllerManager.getContoller(0).getRightThumbStick();
        super.eit.getImage().render(x, y, eit.getImage().pWidth * 4, eit.getImage().pHeight * 4, g, mouse.getAngle()); 
        lastX = x;
        lastY = y;
    }
    
    @Override
    public void tickInHand(){
        if(mouse.risingAction(TowerOfPuzzles.mouseInput.IsPressed||(TowerOfPuzzles.controllerManager.getContoller(0).getButton(EnumButtonType.RIGHT_STICK_PRESSED)>0.0f))){
            Vector3D mouse = TowerOfPuzzles.controllerManager.getContoller(0).getRightThumbStick();
            EntityBullet b = new EntityBullet((int)lastX, (int)lastY, mouse.normalize().multiplyVector(new Vector3D(12.0f, 12.0f, 12.0f)));
            TowerOfPuzzles.entityManager.add(b);
        }
    }
}
