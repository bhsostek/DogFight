/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Base.TowerOfPuzzles;
import Entity.Entity;
import Entity.EntityManager;
import Physics.PhysicsEngine;
import World.Room;
import World.WorldManager;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Bailey
 */
public class LoadButton extends Button{

    TileChooser tc;
    
    public LoadButton(int x, int y, TileChooser tc) {
        super(x, y, "loadButton.png");
        this.tc = tc;
    }

    @Override
    public void event() {
        String dir = JOptionPane.showInputDialog("Load a new Room?", "");
        if(dir!=null){
            File file = new File(dir);

            int reply = JOptionPane.showConfirmDialog(null, "Did you save the current room?", "Your level will be lost!", JOptionPane.YES_NO_OPTION);

            if (reply == JOptionPane.YES_OPTION) {
                WorldManager.removeRoom(tc.getRoom());
                for(Entity e: EntityManager.getEntities()){
                    TowerOfPuzzles.entityManager.remove(e);
                }
                PhysicsEngine.Reset();
                Room r = new Room(0, 0, dir); 
                tc.room = r;
                WorldManager.addRoom(tc.room);
            }

        }
    }
    
}
