/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import java.awt.Color;
import java.util.LinkedList;

/**
 *
 * @author Bayjose
 */
public class CollisionChannel {
    public final String name;

    public RigidBody[] collisons = new RigidBody[]{};
    private LinkedList<RigidBody> toAdd = new LinkedList<RigidBody>();
    private LinkedList<RigidBody> toRemove = new LinkedList<RigidBody>();
    
    public Color color = Color.decode(""+(int)(Math.random() * 16777215));
    
    public CollisionChannel(String name, RigidBody[] collisons){
        this.name = name;
        this.collisons = collisons;
    }
    
    private void updateBodies(){
        //only run if need to
        if(toRemove.size()>0 || toAdd.size()>0){
            LinkedList<RigidBody> newBodies = new LinkedList<RigidBody>();
            //remove all lights flagged to remove
            for(int i = 0; i < this.collisons.length; i++){
                if(toRemove.contains(this.collisons[i])){
                    //Remove that body
                }else{
                    newBodies.add(this.collisons[i]);
                }
            }
            //clear the buffer
            toRemove.clear();
            
            //add all new lights
            for (RigidBody body : toAdd) {
                body.setColor(this.color);
                newBodies.add(body);
            }
            //clear that buffer
            toAdd.clear();
            //build new light array
            RigidBody[] out = new RigidBody[newBodies.size()];
            for(int i = 0; i < newBodies.size(); i++){
                out[i] = newBodies.get(i);
            }
            //set it
            this.collisons = out;
        }
    }
    
    public void tick(){
        updateBodies();  
    }
    
    public void remove(RigidBody object){
        toRemove.add(object);
    }
    
    public RigidBody add(RigidBody object){
        if(object!=null){
            toAdd.add(object);
        }
        return object;
    }

    public void setColor(Color color){
        this.color = color;
    }
    
    public void clearAll() {
        for(RigidBody bod : collisons){
            remove(bod);
        }
    }
}
