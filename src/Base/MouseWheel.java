/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author Bayjose
 */
public class MouseWheel implements MouseWheelListener{


    public static int scrollIndex = 0;
    
    public MouseWheel(){

    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scrollIndex += (e.getUnitsToScroll());
        scrollIndex = Math.max(-16, scrollIndex);
        scrollIndex = Math.min( 16, scrollIndex);
    }
    
    public void tick(){
        if(scrollIndex>0){
            scrollIndex--;
        }
        
        if(scrollIndex<0){
            scrollIndex++;
        }
    }
    
}
