/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Base.Engine;
import Base.Keyboard;
import Base.util.StringUtils;
import static Physics.PhysicsEngine.channels;
import java.awt.Color;
import java.awt.Graphics;
import Graphics.SpriteBinder;
import javax.script.ScriptEngine;

/**
 *
 * @author Bayjose
 */
public class PhysicsEngine extends Engine{
    public static String activeChannel = "bodies";
    public static CollisionChannel[] channels;
    public static int lastHit;
    private int numBods = 0;
    
    public PhysicsEngine(){
        super("PhysicsEngine");
        Reset();
        PhysicsEngine.addChannel("toAdd");
        PhysicsEngine.addChannel("solids");
        
        PhysicsEngine.getChannel("toAdd").setColor(Color.decode("#ff6600"));
        PhysicsEngine.getChannel("solids").setColor(Color.decode("#ff6600"));
    }
    
    public void tick(){
        for(int i=0; i<PhysicsEngine.channels.length; i++){
            PhysicsEngine.channels[i].tick();
        }
    }
    
    public static boolean collides(String c1, String c2){
        for(RigidBody obj: PhysicsEngine.getChannel(c1).collisons){
            for(int i = 0; i < PhysicsEngine.getChannel(c2).collisons.length; i++){
                if(!obj.equals(PhysicsEngine.getChannel(c2).collisons[i])){
                    if(RigidUtils.Collides(PhysicsEngine.getChannel(c2).collisons[i], obj)){
                        PhysicsEngine.lastHit = i;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void Render(Graphics g){
        for(int i=0; i<PhysicsEngine.channels.length; i++){
            if(Keyboard.bool_1){
                for(RigidBody obj: PhysicsEngine.channels[i].collisons){
                    RigidUtils.Render(obj, g);
                }
            }
        }
    }
    
    public static RigidBody addToChannel(String name, RigidBody object){
        if(!PhysicsEngine.hasChannel(name)){
            PhysicsEngine.addChannel(name);
        }
        for(int i=0; i<channels.length; i++){
            if(channels[i].name.equals(name)){
                return channels[i].add(object);
            }
        }
        return object;
    }
    
    public static void addToActiveChannel(RigidBody object){
        for(int i=0; i<channels.length; i++){
            if(channels[i].name.equals(PhysicsEngine.activeChannel)){
                channels[i].add(object);
                return;
            }
        }
    }
    
    public static boolean hasChannel(String name){
        for(int i=0; i<channels.length; i++){
            if(channels[i].name.equals(name)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean pointIntersects(int x, int y, String channel){
        if(hasChannel(channel)){
            for(RigidBody bod : PhysicsEngine.getChannel(channel).collisons){
                if(bod.getCollision().contains(x-bod.x, y-bod.y)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean pointIntersects(Point p, String channel){
        if(hasChannel(channel)){
            for(RigidBody bod : PhysicsEngine.getChannel(channel).collisons){
                if(bod.getCollision().contains(p.getX(), p.getY())){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void addChannel(String name){
        if(!hasChannel(name)){
            CollisionChannel[] newChannels = new CollisionChannel[PhysicsEngine.channels.length+1];
            for(int i=0; i<newChannels.length-1; i++){
                newChannels[i] = PhysicsEngine.channels[i];
            }
            newChannels[newChannels.length-1] = new CollisionChannel(name, new RigidBody[]{});
            PhysicsEngine.activeChannel = name;
            PhysicsEngine.channels = newChannels;
        }
    }
    
    public static boolean intersects(RigidBody bod, String name){
        if(hasChannel(name)){
            CollisionChannel channel = getChannel(name);
            for (RigidBody collison : channel.collisons) {
                if(RigidUtils.Collides(bod, collison)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static CollisionChannel getChannel(String name){
        if(channels==null){
            String[] channelNames = new String[]{};
            System.out.println("Initializing Channels.");
            channels = new CollisionChannel[channelNames.length];
            for (int i = 0; i < channelNames.length; i++) {
                channels[i] = new CollisionChannel(channelNames[i], new RigidBody[]{});
                System.out.println("Initialized Collision Channel:" + channelNames[i]);
            }
            System.out.println("Done.");
        }
        for(int i=0; i<channels.length; i++){
            if(channels[i].name.equals(name)){
                return channels[i];
            }
        }
        PhysicsEngine.addChannel(name);
        return getChannel(name);
    }
    
    public Color randomColor(){
        return (new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
    }
    
    public static void Reset(){
        channels = new CollisionChannel[]{};
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
