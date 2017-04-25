/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Animation;

import Base.MousePositionLocator;
import Base.TowerOfPuzzles;
import Base.util.StringUtils;
import Base.util.XMLParser;
import Graphics.Sprite;
import Graphics.SpriteUtils;
import Physics.Point2D;
import java.awt.Graphics;
import org.w3c.dom.Node;

/**
 *
 * @author Bailey
 */
public class Animation {
    
    private String path;
    private Node animation;
    
    private Sprite[] sprites;
    private Timeline[] timelines;
    
    private int index=0;
    //these are in terms of 1000ths of a second
    private int maxCount=0;
    
    private float x = 0.0f;
    private float y = 0.0f;
    
    public Animation(String path){
        this.path = path;
        if(!path.contains(".scml")){
            this.path = path+".scml";
        }
        this.animation = XMLParser.loadFile(this.path);
        
        //Load the sprites
        Node[] sprites = XMLParser.getAllNodesWithName("file", XMLParser.getFirstNodeWithName("folder", animation));
        this.sprites = new Sprite[sprites.length];
        for(int i = 0; i < sprites.length; i++){
            String[] spriteAttributes = XMLParser.getAttributes(sprites[i]);
            this.sprites[i] = SpriteUtils.flipSpriteHorisontal(SpriteUtils.flipSpriteVertical(TowerOfPuzzles.spriteBinder.loadSprite(XMLParser.getDataFromAttribute("name", spriteAttributes))));
        }
        
        //Load the length of the animation file
        maxCount = Integer.parseInt(XMLParser.getDataFromAttribute("length", XMLParser.getAttributes(XMLParser.getFirstNodeWithName("animation", animation))));
        System.out.println("Length:"+this.maxCount);
        
        //determine how many timelines there are 
        Node[] timelineNodes = XMLParser.getAllNodesWithName("timeline", animation);
        System.out.println("timelineNodes.length="+timelineNodes.length);
        this.timelines = new Timeline[timelineNodes.length];
        for(int i = 0; i < timelineNodes.length; i++){
            String[] attributes = XMLParser.getAttributes(XMLParser.getFirstNodeWithName("object", XMLParser.getFirstNodeWithName("key", timelineNodes[i])));
            int tmp_spriteIndex = 0;

            if(!XMLParser.getDataFromAttribute("file", attributes).isEmpty()){
                tmp_spriteIndex = Integer.parseInt(XMLParser.getDataFromAttribute("file", attributes));
            }
            Node[] frameNodes = XMLParser.getAllNodesWithName("key", timelineNodes[i]);
            KeyFrame[] frames = new KeyFrame[frameNodes.length];
            for(int j = 0; j < frames.length; j++){
                float frame_x = 0;
                float frame_y = 0;
                float frame_rotation = 0;
                int frame_time = 0;
                int frame_spin = 1;
                
                if(!XMLParser.getDataFromAttribute("time", XMLParser.getAttributes(frameNodes[j])).isEmpty()){
                    frame_time = Integer.parseInt(XMLParser.getDataFromAttribute("time", XMLParser.getAttributes(frameNodes[j])));
                }
                
                if(!XMLParser.getDataFromAttribute("spin", XMLParser.getAttributes(frameNodes[j])).isEmpty()){
                    frame_spin = Integer.parseInt(XMLParser.getDataFromAttribute("spin", XMLParser.getAttributes(frameNodes[j])));
                    if(frame_spin==0){
                        frame_spin = 1;
                    }
                }
                
                if(!XMLParser.getDataFromAttribute("x", XMLParser.getAttributes(XMLParser.getFirstNodeWithName("object", frameNodes[j]))).isEmpty()){
                    frame_x = Float.parseFloat(XMLParser.getDataFromAttribute("x", XMLParser.getAttributes(XMLParser.getFirstNodeWithName("object", frameNodes[j]))));
                }
                
                if(!XMLParser.getDataFromAttribute("y", XMLParser.getAttributes(XMLParser.getFirstNodeWithName("object", frameNodes[j]))).isEmpty()){
                    frame_y = Float.parseFloat(XMLParser.getDataFromAttribute("y", XMLParser.getAttributes(XMLParser.getFirstNodeWithName("object", frameNodes[j]))));
                }
                
                if(!XMLParser.getDataFromAttribute("angle", XMLParser.getAttributes(XMLParser.getFirstNodeWithName("object", frameNodes[j]))).isEmpty()){
                    frame_rotation = Float.parseFloat(XMLParser.getDataFromAttribute("angle", XMLParser.getAttributes(XMLParser.getFirstNodeWithName("object", frameNodes[j]))));
                } 
                frames[j] = new KeyFrame(frame_x, frame_y * -1, frame_rotation, frame_time, frame_spin);
            }
            timelines[i] = new Timeline(tmp_spriteIndex, frames);
        }
    }
    
    public void tick(){
        //get all timelines and create a timeline object
        //for each timeline object get the next key frame
        //translate the object towards its next key position 
        //increase the index by 1000/60
        if(this.index < this.maxCount){
            this.index+=(int)(1000.0f/60.0f);
            if(index>maxCount){
                index = maxCount;
            }
        }else{
            index = 0;
        }
//        index = 900;
//        for(Timeline t:this.timelines){
//            System.out.println("Index:"+t.getFrame(index).time+" -> Next Index:"+t.getNextFrame(index).time);
//        }
    }
    
    public void render(Graphics g){
        for(Timeline t:this.timelines){
            float percent = (((float)index-t.getFrame(index).time)/(t.getNextFrame(index).time-t.getFrame(index).time));
            if(Float.isInfinite(percent)){
                percent = 0;
            }
            if(Float.isNaN(percent)){
                percent = 1;
            }
            if(percent < 0){
                percent*=-1;
            }

            float rotation = (linearFade(t.getFrame(index).angle,t.getNextFrame(index).angle,percent))+180;
            
            sprites[t.spriteIndex].render(x+(linearFade(t.getFrame(index).x,t.getNextFrame(index).x,percent)*4)+MousePositionLocator.MouseLocation.x, y+(linearFade(t.getFrame(index).y,t.getNextFrame(index).y,percent)*4)+MousePositionLocator.MouseLocation.y, sprites[t.spriteIndex].pWidth*4, sprites[t.spriteIndex].pHeight*4, g, rotation);
//            System.out.println("Roation:"+rotation);
        }
        //for each timeline, render the sprite at its position
    }
    
    public float linearFade(float num1, float num2, float percent){
        return (num2 * (percent))+(num1 * (1.0f-percent));
    }
}
