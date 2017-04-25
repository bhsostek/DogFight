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
public class IncreaseXSize extends Button{
    
    TileChooser tc;
    
    public IncreaseXSize(int x, int y, TileChooser tc) {
        super(x, y, "x.png");
        this.tc = tc;
    }

    @Override
    public void event() {
        int x = -1;
        try{
            x = Integer.parseInt(JOptionPane.showInputDialog("Current Width", ""+tc.getRoom().getWidth()));
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        if(x>0){
            tc.changeSize(x, tc.getRoom().getHeight());
        }
    }
    
}
