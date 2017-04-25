/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lighting;

/**
 *
 * @author Bailey
 */
public class Light {
    int color;
    int size;
    float x;
    float y;
    boolean moved = true;
    
    public Light(int x, int y, int size, int color){
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }
    
    public float getX(){
        return this.x;
    }
    
    public float getY(){
        return this.y;
    }
    
    public int getSize(){
        return this.size;
    }
    
    public int getColor(){
        return this.color;
    }
    
    public void setX(float i){
        if(i!=x){
            this.x = i;
            moved = true;
        }
    }
    
    public void setY(float i){
        if(i!=y){
            this.y = i;
            moved = true;
        }
    }
    
    public void setSize(int i){
        this.size = i;
    }
    
    public void setColor(int i){
        this.color = i;
    }
    
    public boolean hasMoved(){
        return moved;
    }
    
    public void resetMoved(){
        moved = false;
    }
}
