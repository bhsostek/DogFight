/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base.Controller;

import Base.Engine;
import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.DynamicCollection;
import java.awt.Color;
import java.awt.Graphics;
import javax.script.ScriptEngine;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author Bailey
 */
public class ControllerManager extends Engine{

    private DynamicCollection<JavaController> controllers;
    
    private final int maxTicks = 5*60;
        private int currentTicks = maxTicks;
    
    public ControllerManager() {
        super("Controllers");
    }

    @Override
    public void init() {
        controllers = new DynamicCollection<JavaController>();
        
        pollControllers();
        
        System.out.println("Connected Controllers:"+controllers.getLength());
    }

    @Override
    public void tick() {
        //every x seconds poll the controllers
//        if(currentTicks>0){
//            currentTicks--;
//        }else{
//            currentTicks = maxTicks;
//            pollControllers();
//        }
        
        controllers.tick();
        for(JavaController controller: controllers.getCollection(JavaController.class)){
            if(controller.isConnected()){
                //updates controller values
                controller.poll();
            }else{
                controllers.remove(controller);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if(Keyboard.bool_0){
            int index = 0;
            for(JavaController controller: controllers.getCollection(JavaController.class)){
                if(controller.getButton(EnumButtonType.LEFT_STICK_PRESSED)>0){
                    g.setColor(Color.GREEN);
                }else{
                    g.setColor(Color.RED);
                }
                g.drawOval(50, 50+(index*100), 100, 100);
                g.drawLine(100, 100+(index*100), (int)(controller.getLeftThumbStick().getX()*50)+100, (int)(controller.getLeftThumbStick().getY()*50)+100+(index*100));
                
                if(controller.getButton(EnumButtonType.RIGHT_STICK_PRESSED)>0){
                    g.setColor(Color.GREEN);
                }else{
                    g.setColor(Color.RED);
                }
                g.drawOval(50+(150), 50+(index*100), 100, 100);
                g.drawLine(100+(150), 100+(index*100), (int)(controller.getRightThumbStick().getX()*50)+100+(150), (int)(controller.getRightThumbStick().getY()*50)+100+(index*100));
                
                index++;
            }
        }
    }
    
    public void pollControllers(){
        
        Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
        
        for (Controller ca1 : ca) {
            if (ca1.getType().equals(Controller.Type.STICK)) {
                loop:
                {
                    System.out.println("Controller:"+ca1.hashCode());
                    boolean canAdd = true;
                    for (JavaController controller : controllers.getCollection(JavaController.class)) {
                        if (controller.equalsController(ca1)) {
                            canAdd = false;
                            break loop;
                        }
                    }
                    if (canAdd) {
                        controllers.add(new JavaController(ca1));
                    }
                }
            }   
        }
        
        controllers.tick();
    }

    @Override
    public void registerForScripting(ScriptEngine engine) {

    }

    @Override
    public void onShutdown() {

    }
    
    public JavaController getContoller(int controllerIndex){
        if(controllerIndex < this.controllers.getLength()){
            int index = 0;
            for(JavaController controller : controllers.getCollection(JavaController.class)){
                if(index == controllerIndex){
                    return controller;
                }
                index++;
            }
        }
        return null;
    }
    
}
