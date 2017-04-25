/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GUI;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class HUD extends UI{
    
    private UI gui = null;
    private UI change = null;
    
    private Debouncer exit = new Debouncer(false);

    public HUD() {
        super(0,0);
    }


    public void Open(UI ui){
        this.change = ui;
        this.change.onOpen();
    }
    
    public void Close(){
        if(this.change!=null){
            this.change.onClose();
            this.change = null;
        }
    }
    
    @Override
    public void tick() {
        if(exit.risingAction(Keyboard.ESC)){
            Close();
            TowerOfPuzzles.player.closeInventory();
        }
        if(change!=gui){
            gui = change;
        }
        if(this.gui!=null){
            this.gui.tick();
        }
        change = gui;
    }

    @Override
    public void render(Graphics g) {
        if(this.gui!=null){
            this.gui.render(g);
        }
    }

    @Override
    public void onClick(Rectangle rect) {
        if(this.gui!=null){
            this.gui.onClick(rect);
        }    
    }
    

}
