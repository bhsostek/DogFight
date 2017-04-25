/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Font;

import Base.Engine;
import Graphics.PixelUtils;
import Graphics.Sprite;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import javax.script.ScriptEngine;

/**
 *
 * @author Bailey
 */
public class TextRenderer extends Engine{
    private Font mainFont = null;
    private int color = PixelUtils.makeColor(new int[]{255,255,255,255});
    private HashMap<Integer, Font> fonts = new HashMap<Integer, Font>();
    private final String fontPath;
    
    public TextRenderer(String mainFont){
        super("TextRenderer");
        this.mainFont = new Font(mainFont);
        fontPath = mainFont;
    }
    
    public void setColor(int color){
        if(this.color!=color){
            this.color = color;
            if(fonts.containsKey(color)){
                mainFont = fonts.get(color);
            }else{
                Font f = new Font(fontPath);
                f.setColor(color);
                fonts.put(color, f);
                this.mainFont = f;
            }
        }
    }
    
    public void drawString(String line, int x, int y, Graphics g){
        int length = 0;
        if(mainFont!=null){
            for(int i = 0; i < line.length(); i++){
                Sprite character = mainFont.getFromCharacter(line.charAt(i)+"");
                if(character != null){
                    character.render(x+(length), y, (character.pWidth*2), (character.pHeight*2), g);
                    length+=(character.pWidth*2)+(1 * 4);
                }
            }
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(), this);
    }
    
    @Override
    public void onShutdown() {

    }
    
    
}
