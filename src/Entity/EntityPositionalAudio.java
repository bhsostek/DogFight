/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Editor.Attribute;
import World.Tiles.TileConstants;
import java.awt.Graphics;
import Base.TowerOfPuzzles;
import Base.util.DistanceCalculator;
import Sound.SoundManager;
import java.awt.Color;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;
import net.beadsproject.beads.ugens.SamplePlayer;


/**
 *
 * @author Bailey
 */
public class EntityPositionalAudio extends Entity{
    private Attribute<Float> size = new Attribute<Float>("Size:", 128.0f);
    private Attribute<String> fileExtension = new Attribute<String>("Sound File:", "");
    private Attribute<Boolean> looping = new Attribute<Boolean>("Looping:", true);
    private Attribute<Float> loopStart = new Attribute<Float>("Loop Start:", 0.0f);
    
    private final String base;
    private boolean killOnEnd = false;
    
    private final Gain g;
    private SamplePlayer player;
    
    private float gain = 1.0f;
    private Panner pan;
    
    public EntityPositionalAudio(int x, int y, String sound, float size, boolean loop) {
        super(x, y, 2 * TileConstants.SCALE, 2 * TileConstants.SCALE, EnumEntityType.POSITIONAL_AUDIO);
        //file stored in the sound directory
        this.base = TowerOfPuzzles.Path+TowerOfPuzzles.SoundPath;
        fileExtension.setData(sound);
        this.size.setData(size);
        this.looping.setData(loop);
//         SampleManager.setBufferingRegime(Sample.Regime.newStreamingRegime(1000));
        player = new SamplePlayer(SoundManager.getAudioContext(), SampleManager.sample(base+fileExtension.getData()));
        player.setKillOnEnd(false);
        //Set up dynamic gain 1.0f is initial gain value 1.of = 100%
        g = new Gain(SoundManager.getAudioContext(), 1, gain);
        g.addInput(player);
        
        //Set up dymanic pan
        pan = new Panner(SoundManager.getAudioContext());
        pan.setPos(0);
        pan.addInput(g);

        SoundManager.getAudioContext().out.addInput(pan);
        
        super.addAttribute(this.size);
        super.addAttribute(this.fileExtension);
        super.addAttribute(this.loopStart);
        super.addAttribute(this.looping);
        
        calculateGain();
        
        player.start();
    }
    
    @Override
    public void onRemove(){
        SoundManager.killMusic(fileExtension.getData());
    }
    
    public void setKillOnEnd(boolean b){
        this.killOnEnd = b;
    }
   
    @Override
    public void update() {
        calculateGain();

        if(this.looping.getData()){
            if(player.getPosition()>=SampleManager.sample(base+fileExtension.getData()).getLength()){
                player.setPosition(this.loopStart.getData());
                if(this.killOnEnd){
                    TowerOfPuzzles.entityManager.remove(this);
                }
            }
        }else{
            if(player.getPosition()>=SampleManager.sample(base+fileExtension.getData()).getLength()){
                if(this.killOnEnd){
                    TowerOfPuzzles.entityManager.remove(this);
                }
            }
        }
    }
    
    public void calculateGain(){
        int listenerX = (int)TowerOfPuzzles.cam.x+(TowerOfPuzzles.WIDTH/2);
        int listenerY = (int)TowerOfPuzzles.cam.y+(TowerOfPuzzles.HEIGHT/2);
        if(DistanceCalculator.CalculateDistanceF(listenerX, listenerY, super.x, super.y)<=this.size.getData()){
            if(listenerX<x){
                //left
                this.pan.setPos((DistanceCalculator.CalculateDistanceF(listenerX, listenerY, x, y))/this.size.getData());
            }else{
                //right
                this.pan.setPos(-(DistanceCalculator.CalculateDistanceF(listenerX, listenerY, x, y))/this.size.getData());
            }
        }
        gain = Math.max(0.0f, 1.0f-((DistanceCalculator.CalculateDistanceF(listenerX, listenerY, this.x, this.y))/this.size.getData()));
        
        if(gain<=2.0f){
            this.g.setGain(gain);
        }else{
            gain = 2.0f;
            this.g.setGain(gain);
        }
    }
    
    @Override
    public void onAttributeChange(){
        if(fileExtension.wasEdited()){
            try{
                player.setSample(SampleManager.sample(base+fileExtension.getData()));
                player.setLoopEnd(new Envelope(SoundManager.getAudioContext(), (float) SampleManager.sample(base+fileExtension.getData()).getLength()));
                player.start();
            }catch(java.lang.NullPointerException e){
                player.pause(true);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if(TowerOfPuzzles.IS_EDITOR){
            g.setColor(Color.red);
            g.drawOval((int)x-1, (int)y-1, 3, 3);
            g.setColor(Color.magenta);
            g.drawOval((int)x-(size.getData().intValue()),(int)y-(size.getData().intValue()),size.getData().intValue()*2, size.getData().intValue()*2);
        }
    }
    
    @Override
    public String save(){
        return super.getType()+"{"+super.x+","+super.y+","+this.fileExtension.getData()+","+this.size.getData()+","+this.looping.getData()+"}";
    }
}
