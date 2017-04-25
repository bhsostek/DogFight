/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Mob.AiComponets;

import Editor.Attribute;
import Entity.Mob.Mob;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public abstract class AIComponent{
    private Attribute[] attributes = new Attribute[]{};
    public Mob mob;
    private final EnumAIComponent component;
    
    public AIComponent(Mob mob, EnumAIComponent component){
        this.mob = mob;
        this.component = component;
    }
    
    public void addAttribute(Attribute a){
        Attribute[] tempAttributes = new Attribute[attributes.length+1];
        for(int i = 0; i < attributes.length; i++){
            tempAttributes[i] = attributes[i];
        }
        tempAttributes[attributes.length] = a;
        attributes = tempAttributes;
    }
    
    public void removeAttribute(Attribute a){
        Attribute[] tempAttributes = new Attribute[attributes.length-1];
        boolean hasAttribute = false;
        int index = 0;
        for(int i = 0; i < attributes.length; i++){
            loop:{
                if(attributes[i].equals(a)){
                    hasAttribute = true;
                    break loop;
                }
                tempAttributes[index] = attributes[i];
                index++;
            }
        }
        if(hasAttribute){
            attributes = tempAttributes;
        }
    }
    
    public Attribute[] getAttributes(){
        return this.attributes;
    }
    
    public void render(Graphics g){
        return;
    }
    
    public abstract String extraData();
    
    public final String genSave(){
        return this.component.name()+"{}";
    }
    
    public EnumAIComponent getType(){
        return this.component;
    }
    
    public abstract void tick();
}
