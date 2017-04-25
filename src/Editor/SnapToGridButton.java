/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

/**
 *
 * @author Bailey
 */
public class SnapToGridButton extends Button{
    
    
    public SnapToGridButton(int x, int y) {
        super(x, y, "freeForm.png");
    }

    @Override
    public void event() {
        Editor.SNAP_TO_GRID = !Editor.SNAP_TO_GRID;
    }
    
}
