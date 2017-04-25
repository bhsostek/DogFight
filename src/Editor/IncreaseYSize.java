/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Base.MouseInput;
import javax.swing.JOptionPane;

/**
 *
 * @author Bailey
 */
public class IncreaseYSize extends Button{
    
    TileChooser tc;
    
    public IncreaseYSize(int x, int y, TileChooser tc) {
        super(x, y, "y.png");
        this.tc = tc;
    }

    @Override
    public void event() {
        int y = -1;
        try{
            y = Integer.parseInt(JOptionPane.showInputDialog("Current Height", ""+tc.getRoom().getHeight()));
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        if(y>0){
            tc.changeSize(tc.getRoom().getWidth(),y);
        }
    }
    
}
