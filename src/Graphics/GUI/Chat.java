/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GUI;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Base.util.DynamicCollection;
import Graphics.PixelUtils;
import Graphics.Sprite;
import Quest.Chat.Message;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class Chat extends UI{

    Sprite sprite;
    
    private DynamicCollection<Message> messages = new DynamicCollection<Message>();
    
    private int[] indexes = new int[]{0,0,0};
    private int line = 0;
    private float offset = 0;
    private float offsetRate = 2.4f;
    
    private Debouncer bounce = new Debouncer(Keyboard.E);
    
    public Chat(int x, int y) {
        super(x, y);
        sprite = TowerOfPuzzles.spriteBinder.loadSprite("gui/chat.png");
    }

    @Override
    public void tick() {
        messages.tick();
        if(this.messages.getLength()>0){
            if(offset < sprite.pHeight){
                offset+=offsetRate;
                if(offset>sprite.pHeight){
                    offset = sprite.pHeight;
                }
                return;
            }
            
            Message msg = messages.getCollection(Message.class)[0];
                        
            //once ui is fully up
            loop:{
                if((line+0) < msg.getMessage().length){
                    if(indexes[0] < msg.getMessage()[line+0].length()){
                        indexes[0] += 1;
                        break loop;
                    }
                }else{
                    //pop the top message
                    this.messages.remove(this.messages.getCollection(Message.class)[0]);
                    line = 0;
                }
                if((line+1) < msg.getMessage().length){
                    if(indexes[1] < msg.getMessage()[line+1].length()){
                        indexes[1] += 1;
                        break loop;
                    }
                }
                if((line+2) < msg.getMessage().length){
                    if(indexes[2] < msg.getMessage()[line+2].length()){
                        indexes[2] += 1;
                        break loop;
                    }
                }
            }

            if(this.messages.getLength()>0){
                if(bounce.risingAction(Keyboard.E)){
                    //if there are more lines, advance the line index
                    if(line<msg.getMessage().length){
                        line+=3;
                        indexes = new int[]{0,0,0};
                    }else{
                        //pop the top message
                        this.messages.remove(this.messages.getCollection(Message.class)[0]);
                        line = 0;
                    }
                }
            }
        }else{
            if(offset>0){
                offset-=offsetRate;
                if(offset<0){
                    offset = 0;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if(offset>0){
            sprite.render(x, TowerOfPuzzles.HEIGHT-((offset*4) - (sprite.pHeight*2)), sprite.pWidth * 4, sprite.pHeight * 4, g);
        }
        if(offset >= sprite.pHeight){
            Message msg = messages.getCollection(Message.class)[0];
            TowerOfPuzzles.textRenderer.setColor(PixelUtils.makeColor(PixelUtils.getRGBA(msg.getNameColor())));
            TowerOfPuzzles.textRenderer.drawString(msg.getName(), x-128, TowerOfPuzzles.HEIGHT-((sprite.pHeight*4)/2)-(16 * 4), g);
            TowerOfPuzzles.textRenderer.setColor(PixelUtils.makeColor(PixelUtils.getRGBA(msg.getMessageColor())));
            
            int index = 0;
            for(int i = 0; i < 3; i++){
                if(line+i < msg.getMessage().length){
                    String string = msg.getMessage()[line+i];
                    TowerOfPuzzles.textRenderer.drawString(string.substring(0, indexes[index]), x - 256, TowerOfPuzzles.HEIGHT-((sprite.pHeight*4)/2)+(index * (18 * 2)), g);
                }
                index++;
            }
        }
    }

    @Override
    public void onClick(Rectangle rect) {
     
    }
    
    public boolean hasMessage(){
        return (this.messages.getLength()>0);
    }
    
    public void renderMessage(){
        
    }
    
    public void addMessage(Message message){
        this.messages.add(message);
        System.out.println("Messag added");
    }
    
}
