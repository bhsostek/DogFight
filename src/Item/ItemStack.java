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
public class ItemStack {
    public Item item;
    public int count;
    public ItemStack(Item item, int count){
        this.item = item;
        this.count = count;
    }
}
