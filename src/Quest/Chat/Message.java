/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Quest.Chat;

import Graphics.Sprite;
import java.awt.Color;

/**
 *
 * @author Bailey
 */
public class Message {
    private Sprite speaker = null;
    private String name = "";
    private Color color_name = Color.CYAN;
    private Color color_message = Color.WHITE;
    private String[] message = new String[]{};
    
    public Message setSpeaker(Sprite sprite){
        this.speaker = sprite;
        return this;
    }
    
    public Message setName(String name){
        this.name = name;
        return this;
    }
    
    public Message setNameColor(Color color){
        this.color_name = color;
        return this;
    }
    
    public Message setMessageColor(Color color){
        this.color_message = color;
        return this;
    }
    
    public Message setMessage(String[] data){
        this.message = data;
        return this;
    }
    
    public String getName(){
        return this.name;
    }
    
    public Color getNameColor(){
        return this.color_name;
    }
    
    public String[] getMessage(){
        return this.message;
    }
    
    public Color getMessageColor(){
        return this.color_message;
    }
    
    public Sprite getSpeaker(){
        return this.speaker;
    }
}
