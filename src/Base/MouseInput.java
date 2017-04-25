package Base;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.script.ScriptEngine;


/**
 *
 * @author Bayjose
 */
public class MouseInput extends Engine implements MouseListener {
    

    public Rectangle Mouse= new Rectangle(0, 0, 1, 1);
    public boolean IsPressed=false;
    public boolean IsRightClick=false;
            
    public MouseInput(){
        super("Mouse");
    }

    public void mouseClicked(MouseEvent e) {
         
    }
    
    public void mousePressed(MouseEvent e) {
        Mouse.x=(e.getX());
        Mouse.y=(e.getY());
    
        IsPressed=true;
        IsRightClick=false;
//            
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            IsRightClick=true;
        }

        if(!IsRightClick){
            
        }
    }

    public void mouseReleased(MouseEvent e) {
        IsPressed=false;
        IsRightClick=false;
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(),this);
    }

    @Override
    public void onShutdown() {

    }
}
