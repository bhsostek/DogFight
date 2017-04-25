/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Editor.Attribute;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class Container {
    
    private Attribute<ItemStack>[] slots;
    private Attribute<String> saveFileName = new Attribute<String>("File Name:", "container");
    private Attribute<Integer> length = new Attribute<Integer>("Container Size:", 1);
    
    public Container(String[] items){
        length.setData(items.length);
        slots = new Attribute[length.getData()];
        
        for(int i = 0; i < length.getData(); i++){
            Item item =null;
            int count = 1;
            if(!items[i].isEmpty()){
                if(!items[i].startsWith("null")){
                    String[] data = items[i].split("\\{")[1].replaceAll("}", "").split(",");
                    item = Items.valueOf(items[i].split("\\{")[0]).generate(data);
                    if(data.length>0){
                        count = Integer.parseInt(data[data.length-1]);
                    }
                }
                slots[i] = new Attribute<ItemStack>("Item at:"+i, new ItemStack(item, count));
            }else{
                slots[i] = new Attribute<ItemStack>("Item at:"+i, new ItemStack(null, 0));
            }            
        }
    }
    
    public String genSave(){
        String out = "";
        for(int i = 0; i < slots.length; i++){
            if(slots[i].getData().item!=null){
                out = out+""+slots[i].getData().item.genSave()+","+slots[i].getData().count+";";
            }else{
                out = out+"null"+";";
            }
        }
        return out;
    }
    
    public ItemStack getItemStack(int index){
        if(index >= 0 && index < this.slots.length && this.slots.length>0){
            return this.slots[index].getData();
        }
        return new ItemStack(null, 0);
    }
    
    public Attribute getAttribute(int index){
        if(index>=0 && index<=this.length.getData()){
            return this.slots[index];
        }
        return null;
    }
    
    public void renderAt(int x, int y, Graphics g){
        for(int i = 0; i < this.length.getData(); i++){
            Item item = this.getItemStack(i).item;
            if(item!=null){
                item.renderInWorld(x +(int)((this.length.getData() * 16)* Math.cos(Math.toRadians((i * (360.0f / this.length.getData()))-90.0f))),
                                        y +(int)((this.length.getData() * 16)* Math.sin(Math.toRadians((i * (360.0f / this.length.getData()))-90.0f))), g);
            }
        }
    }
    
    public void putItemStack(ItemStack item, int slot){
        if(slot >= 0 && slot < length.getData()){
            this.slots[slot].setData(item);
        }
    }
    
    public ItemStack takeItem(int index){
        if(index >= 0 && index < length.getData()){
            if(this.slots[index].getData().item!=null){
                this.slots[index].getData().count--;
                if(this.slots[index].getData().count<=0){
                    this.slots[index].setData(new ItemStack(null,0));
                }
                return new ItemStack(this.slots[index].getData().item,1);
            }
        }
        return new ItemStack(null,0);
    }
    
    public ItemStack addItemToOpenSlotStartingAtIndex(ItemStack itemStack, int index){
        Item item = itemStack.item;
        if(item == null){
            return new ItemStack(null,0);
        }
        for(int i = 0; i < this.length.getData(); i++){
            Item tmpItem = this.slots[i].getData().item;
            if(tmpItem!=null){
                if(tmpItem.getType().equals(item.getType())){
                    if(tmpItem.genSave().equals(item.genSave())){
                        this.slots[i].getData().count+=itemStack.count;
                        return new ItemStack(null, 0);
                    }
                }
            }
        }
        for(int i = 0; i < this.length.getData(); i++){
            Item tmpItem = this.slots[index].getData().item;
            if(tmpItem==null){
                this.slots[index].setData(itemStack);
                return new ItemStack(null, 0);
            }
            if(index>=this.length.getData()-1){
                index = 0;
            }else{
                index++;
            }
        }
        
        return itemStack;
    }
    
    public ItemStack addItemToOpenSlot(ItemStack itemStack){
        Item item = itemStack.item;
        if(item == null){
            return new ItemStack(null,0);
        }
        for(int i = 0; i < this.length.getData(); i++){
            Item tmpItem = this.slots[i].getData().item;
            if(tmpItem!=null){
                if(tmpItem.getType().equals(item.getType())){
                    if(tmpItem.genSave().equals(item.genSave())){
                        this.slots[i].getData().count+=itemStack.count;
                        return new ItemStack(null, 0);
                    }
                }
            }
        }
        
        for(int i = 0; i < this.length.getData(); i++){
            Item tmpItem = this.slots[i].getData().item;
            if(tmpItem==null){
                this.slots[i].setData(itemStack);
                return new ItemStack(null, 0);
            }
        }
        
        return itemStack;
    }
    
    public int getLength(){
        return this.length.getData();
    }
    
    public int getNumberOfAttributes(){
        return this.slots.length;
    }
    
    public Attribute<Integer> getLengthAttribute(){
        return this.length;
    }
    
    public Attribute<String> getFileNameAttribute(){
        return this.saveFileName;
    }
    
    public void setSize(int length){
        Attribute<ItemStack>[] out = new Attribute[length];
        for(int i = 0; i < length; i++){
            ItemStack item = this.getItemStack(i);
            out[i] = new Attribute<ItemStack>("Item at:"+i, item);
        }
        this.slots = out;
    }
    
    public Attribute<ItemStack>[] getAttributes(){
        return this.slots;
    }
}
