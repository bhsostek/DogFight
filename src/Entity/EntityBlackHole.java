/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.DistanceCalculator;
import Editor.Attribute;
import Physics.Vector3D;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityBlackHole extends Entity{
    
    public Attribute<Integer> radius = new Attribute<Integer>("Radius:",0);
    
    public EntityBlackHole(int x, int y, int radius) {
        super(x, y, radius*2, radius*2, EnumEntityType.BLACK_HOLE);
        this.radius.setData(radius);
        super.addAttribute(this.radius);
    }

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        if(!TowerOfPuzzles.IS_EDITOR){
            if(Keyboard.bool_1){
                g.setColor(Color.red);
                g.drawOval((int)super.x-(radius.getData()), (int)super.y-(radius.getData()), radius.getData()*2, radius.getData()*2);
            }
        }
    }
    
    public Vector3D calculateForce(Vector3D pos, Vector3D velocity, Vector3D acceleration){
        Vector3D out = new Vector3D(0,0,0);
        float force = 3f;
        
        if(DistanceCalculator.CalculateDistanceF((float)super.x, (float)super.y, pos.getX(), pos.getY())<radius.getData()){
                out.setVelX(pos.getX()-(float)super.x);
                out.setVelY(pos.getY()-(float)super.y);
                out = out.newInstance().normalize().multiplyVector(new Vector3D(radius.getData(),radius.getData(),radius.getData())).inverse().addVector(out);
                out = out.multiplyVector(new Vector3D(0.5f,0.5f,0.5f));
                out = out.normalize();
                out = out.multiplyVector(new Vector3D(force,force,force));
//                
            return velocity.addVector(out);
        }
        
        return velocity;
    }
    
    @Override
    public String extraSaveData(){
        return this.radius.getData()+"";
    }
}
