/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

/**
 *
 * @author Bailey
 */
public class BasicItem extends Item{
    
    public BasicItem(Items eit) {
        super(eit,new String[]{});
    }

    @Override
    public String[] writeDataToBuffer() {
        return new String[]{};
    }
    
}
