/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author Bayjose
 */
public class Background {
    
    public JFrame frame;
    
    public Background(TowerOfPuzzles game){
        frame = new JFrame("BayScript");
        frame.setUndecorated(true);
        Dimension dim = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().width);
        
        game.setPreferredSize(dim);
        game.setMaximumSize(dim);
        game.setMinimumSize(dim);
        
        frame.setSize(dim);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(game);
        
        frame.pack();
        frame.setVisible(true);
    }
}
