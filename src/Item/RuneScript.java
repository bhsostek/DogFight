/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Editor.Attribute;

/**
 *
 * @author Bailey
 */
public class RuneScript extends Item{

    String data1 ="";
    
    public RuneScript(Items eit, String[] data) {
        super(eit, data);
        data1 = data[0];
    }
    
    @Override
    public String[] writeDataToBuffer() {
        return new String[]{data1};
    }
    
    public String getDestination(){
        return this.data1;
    }

    
}
