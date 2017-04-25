/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import World.Room;
import World.WorldManager;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Bailey
 */
public class SaveButton extends Button{

    TileChooser tc;
    
    public SaveButton(int x, int y, TileChooser tc) {
        super(x, y, "saveButton.png");
        this.tc = tc;
    }

    @Override
    public void event() {
        String dir = JOptionPane.showInputDialog("Save the current Room", tc.getRoom().getName());
        if(dir!=null){
            File file = new File(dir);

            if(file.exists()){
                int reply = JOptionPane.showConfirmDialog(null, "That room already exists, do you wish to overwrite it with this room?", "That room already exists!", JOptionPane.YES_NO_OPTION);

                if (reply == JOptionPane.YES_OPTION) {
                    SaveManager.saveRoomAs(dir, tc.getRoom());
                    SaveManager.saveInventory();
                    SaveManager.saveDynamicEntities();
                }
            }else{
                SaveManager.saveRoomAs(dir, tc.getRoom());
            }
        }
    }
    
}
