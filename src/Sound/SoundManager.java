/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;
import Base.Engine;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.script.ScriptEngine;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

/**
 *
 * @author Bailey
 */
public class SoundManager extends Engine{
    private static AudioContext ac;
    private static LinkedList<Music> musicObjects = new LinkedList<Music>();
    private static LinkedList<SoundEffect> soundEffects = new LinkedList<SoundEffect>();
    
    private float volume = 1.0f;
    private Gain gain;
    
    public SoundManager(){
        super("SoundManager");
	this.ac = new AudioContext();
        this.gain = new Gain(ac, 1, volume);
        this.ac.start();
    }
    
    public void setVolume(float volume){
        if(volume>=0 && volume<=2){
            this.volume = volume;
        }
        this.gain.setGain(volume);
    }
		
    public static AudioContext getAudioContext(){
        return ac;
    }
    
    public static void add(Music music){
        musicObjects.add(music);
        music.start();
    }
    
    public void playSound(String file){
        SoundEffect effect = new SoundEffect(file, ac);
        soundEffects.add(effect);
    }
    
    public static void playSoundAt(String file, int x, int y){
        SoundEffect effect = new SoundEffect(file, ac);
        soundEffects.add(effect);
    }
    
    public Music addMusic(String file){
        Music music = new Music(file, ac);
        musicObjects.add(music);
        music.start();
        return music;
    }
    
    public void removeMusic(Music music){
        music.terminate();
        musicObjects.remove(music);
    }
    
    public static void pauseMusic(String file){
        for(int i = 0; i < musicObjects.size(); i++){
            Music music = musicObjects.get(i);
            if(music.getSourcePath().equals(file)){
                music.pause();
                return;
            }
        }
    }
    
    public static void killMusic(String file){
        for(int i = 0; i < musicObjects.size(); i++){
            Music music = musicObjects.get(i);
            if(music.getSourcePath().equals(file)){
                music.terminate();
                return;
            }
        }
    }
    
    public static Music getMusic(String file){
        for(int i = 0; i < musicObjects.size(); i++){
            Music music = musicObjects.get(i);
            if(music.getSourcePath().equals(file)){
                return music;
            }
        }
        return null;
    }
    
    public static void setMusicVolume(String file, float volume){
        for(int i = 0; i < musicObjects.size(); i++){
            Music music = musicObjects.get(i);
            if(music.getSourcePath().equals(file)){
                music.fadeToVolume(volume, 1);
                return;
            }
        }
    }
    
    public void tick(){
        for(int i = 0; i < musicObjects.size(); i++){
            Music music = musicObjects.get(i);
            if(music.isTerminated()){
                musicObjects.remove(music);
                i--;
            }else{
                music.tick();
            }
        }
    }
    
    //sets the volume of music1 to (fade) and volume of (music2) to 1-(fade)
    public static void crossfade(Music music1, Music music2, float fade){
        music1.fadeToVolume(fade, 1);
        music2.fadeToVolume((1.0f-fade), 1);
    }

    @Override
    public void init() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(),this);
    }


    @Override
    public void onShutdown() {

    }
}
