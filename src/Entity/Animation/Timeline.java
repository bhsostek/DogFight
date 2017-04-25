/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Animation;

/**
 *
 * @author Bailey
 */
public class Timeline {
    public int spriteIndex = 0;
    
    private int index = 0;
    private KeyFrame[] frames; 
   
    public Timeline(int spriteIndex, KeyFrame[] frames){
        this.spriteIndex = spriteIndex;
        this.frames = frames;
    }
    
    public KeyFrame getFrame(int ticks){
        for(int i = 0; i < this.frames.length; i++){
            if(i < this.frames.length-1){
                if(ticks>=this.frames[i].time && ticks < this.frames[i+1].time){
                    return frames[i];
                }
            }else{
                if(ticks>=this.frames[i].time){
                    return frames[i];
                }
            }
        }
        return this.frames[0];
    }
    
    public KeyFrame getNextFrame(int ticks){
        for(int i = 0; i < this.frames.length; i++){
            if(i < this.frames.length-1){
                if(ticks>=this.frames[i].time && ticks < this.frames[i+1].time){
                    return frames[i+1];
                }
            }else{
                if(ticks>=this.frames[i].time){
                    return frames[0];
                }
            }
        }
        return this.frames[0];
    }
}
