/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Base.Controller.EnumButtonType;
import Base.Controller.JavaController;
import Base.TowerOfPuzzles;
import Editor.Attribute;
import Entity.Mob.Mob;

/**
 *
 * @author Bailey
 */
public class ControllerComponent extends AIComponent{

    private Attribute<Integer> controllerIndex = new Attribute<Integer>("Controller Index:", 0);
    private Attribute<Float> speed = new Attribute<Float>("Speed:", 6.4f);
    
    private JavaController controller;
    
    public ControllerComponent(Mob mob, int controllerIndex) {
        super(mob, EnumAIComponent.CONTROLLER);
        this.controllerIndex.setData(controllerIndex);
        this.controller = TowerOfPuzzles.controllerManager.getContoller(controllerIndex);
        if(this.controller!=null){
            super.mob.removeComponent(this);
        }
        super.addAttribute(this.controllerIndex);
        super.addAttribute(this.speed);
    }

    @Override
    public String extraData() {
        return (controllerIndex.getData())+"";
    }
    
    @Override
    public void tick() {
        mob.acceleration.setVelX(controller.getLeftThumbStick().getX()*speed.getData());
        if(!mob.isInAir()){    
            if(controller.getLeftThumbStick().getY()<-0.9||controller.getButton(EnumButtonType.A)>0.0f){
                mob.acceleration.setVelY(-8.5f);
            }
        }
    }
    
    public float getKey(EnumButtonType button){
        if(controller!=null){
            return controller.getButton(button);
        }
        return 0.0f;
    }
}
