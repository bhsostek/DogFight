/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Bayjose
 */
public class Window {
    
    public Window(TowerOfPuzzles game){

        JFrame frame = new JFrame(TowerOfPuzzles.NAME);
        Dimension dim = new Dimension(TowerOfPuzzles.WIDTH,TowerOfPuzzles.HEIGHT);
        
        game.setPreferredSize(dim);
        game.setMaximumSize(dim);
        game.setMinimumSize(dim);
        
        frame.setSize(dim);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.requestFocus();
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                game.saveAll();
            }
        });

        frame.pack();
        frame.setVisible(true);
        
        game.start();
    }
}
