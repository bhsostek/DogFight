/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base.util;

import Quest.Chat.Message;
import java.lang.reflect.Array;
import java.util.LinkedList;

/**
 *
 * @author Bailey
 */
public class DynamicCollection <type>{
    
    private Object[] collection = new Object[]{};
    private LinkedList<type> toAdd = new LinkedList<type>();
    private LinkedList<type> toRemove = new LinkedList<type>();
    
    private void updateCollection(){
        //only run if need to
        if(toRemove.size()>0 || toAdd.size()>0){
            LinkedList<type> newCollection = new LinkedList<type>();
            //remove all lights flagged to remove
            for(int i = 0; i < this.collection.length; i++){
                if(toRemove.contains(this.collection[i])){
                    //Remove that body
                }else{
                    newCollection.add((type)this.collection[i]);
                }
            }
            //clear the buffer
            toRemove.clear();
            
            //add all new lights
            for (type object : toAdd) {
                newCollection.add(object);
            }
            //clear that buffer
            toAdd.clear();
            //build new light array
            Object[] out = new Object[newCollection.size()];
            for(int i = 0; i < newCollection.size(); i++){
                out[i] = newCollection.get(i);
            }
            //set it
            this.collection = out;
        }
    }
    
    public void tick(){
        updateCollection();  
    }
    
    public void remove(type object){
        toRemove.add(object);
    }
    
    public void add(type object){
        if(object!=null){
            toAdd.add(object);
        }
    }

    public void clearAll() {
        for(int i = 0; i < this.collection.length; i++){
            remove((type)this.collection[i]);
        }
    }
    
    public int getLength(){
        return this.collection.length;
    }
    
    public type[] getCollection(Class<type> c){
        @SuppressWarnings("unchecked")
        type[] a = (type[]) Array.newInstance(c, this.getLength());
        
        for(int i = 0; i < a.length; i++){
            a[i] = (type)this.collection[i];
        }
        
        return a;
    }
}
