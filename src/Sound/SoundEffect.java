/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import Base.TowerOfPuzzles;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

/**
 *
 * @author Bailey
 */
public class SoundEffect {
    
    
    private final Gain g;
    private final SamplePlayer player;
    private boolean paused = false;
    private final String file;
    
    private float gain = 1.0f;
    private boolean terminate = false;

    //how many ticks left in this gain change
    private int gainTicks = 0;
    //float to modify the current gain by per tick for (gainTicks) ticks
    private float gainModifier = 0;
    
    public SoundEffect(String file, AudioContext ac){
        //file stored in the sound directory
        this.file = TowerOfPuzzles.Path+TowerOfPuzzles.SoundPath+file;
        String audioFile = this.file;
        // SampleManager.setBufferingRegime(Sample.Regime.newStreamingRegime(1000));
        player = new SamplePlayer(ac, SampleManager.sample(audioFile));

        //Set up dynamic gain 1.0f is initial gain value 1.of = 100%
        g = new Gain(ac, 1, gain);
        g.addInput(player);
        ac.out.addInput(g);
        
        player.start();
    }
    
    //can be called on an object, when the object is created to modify the initial gain of said object
    public SoundEffect setInitialGain(float gain){
        if(gain<0){
            gain = 0.0f;
        }
        if(gain>2.0f){
            gain = 2.0f;
        }
        this.gain = gain;
        this.g.setGain(this.gain);
        return this;
    }
    
    
    //stops the music from playing, clears memory for this object, cannot be played again
    public void terminate(){
        player.pause(true);
        terminate = true;
    }
    
    //return true if the object should be removed
    boolean isTerminated() {
        return this.terminate;
    }
    
    //toggles the pause state of the music
    public void pause(){
        if(!this.terminate){
            this.paused = !this.paused;
            player.pause(this.paused);
        }
    }
    
    //fades the current gain value from (gain)f to new (newGain)f in (ticks)60ths of a second;
    public void fadeToVolume(float newGain, int ticks){
        if(newGain>2.0f){
            newGain = 2.0f;
        }
        if(newGain<0.0f){
            newGain = 0.0f;
        }
        this.gainModifier = (newGain - this.gain)/(float)ticks;
        this.gainTicks = ticks;
    }
    
    //ticks 60times per second
    public void tick(){
        if(this.gainModifier!=0){
            if(this.gainTicks>0){
                this.gain+=this.gainModifier;
                this.gainTicks--;
//                System.out.println("Gain:"+this.file+":"+this.gain);
            }else{
                this.gainModifier = 0;
            }
        }
        if(gain<=2.0f){
            this.g.setGain(gain);
        }
    }
    
    
}
