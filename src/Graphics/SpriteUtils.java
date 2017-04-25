package Graphics;

import java.awt.Color;

public class SpriteUtils {
	
	
    public static Sprite generateHighlight(Sprite sprite){
        Sprite out = new Sprite(sprite.width, sprite.height, sprite.pWidth, sprite.pHeight, sprite.pixels.clone());
        out.setPixelColor(0, 0, PixelUtils.makeColor(PixelUtils.getRGBA(Color.red)));
        return out;
    }

    public static Sprite replaceColor(Sprite sprite, Color col1, Color col2){
        int intCol1 = PixelUtils.makeColor(PixelUtils.getRGBA(col1));
        int intCol2 = PixelUtils.makeColor(PixelUtils.getRGBA(col2));
        Sprite out = new Sprite(sprite.width, sprite.height, sprite.pWidth, sprite.pHeight, sprite.pixels.clone());
        for(int i = 0; i < sprite.pWidth; i++){
            for(int j = 0; j < sprite.pHeight; j++){
                if(out.getPixelColor(i, j)==intCol1){
                        out.setPixelColor(i, j, intCol2);
                }
            }
        }
        return out;
    }
    
    public static Sprite overlayGrayscale(Sprite gray, int[] color){
        //init pixel array
        Pixel[] pixelData = new Pixel[gray.pixels.length];
        for(int i = 0; i < pixelData.length; i++){
            pixelData[i] = new ColorPixel(PixelUtils.makeColor(new int[]{255,0,0,255}));
        }
        
        for(int i = 0; i < gray.pWidth; i++){
            for(int j = 0; j < gray.pHeight; j++){
                int[] channels = PixelUtils.getRGBA(gray.getPixelColor(i, j));
                float[] conversion = new float[]{
                    //r
                    ((((float)channels[0])/255.0f)*(float)color[0]),
                    //g
                    ((((float)channels[1])/255.0f)*(float)color[1]),
                    //b
                    ((((float)channels[2])/255.0f)*(float)color[2]),
                    //a
                    ((((float)channels[3])/255.0f)*(float)color[3]),
                };
                for(int k = 0; k < channels.length; k++){
                    channels[k] = (int)conversion[k];
                }

                pixelData[i+(j*gray.pWidth)] = new ColorPixel(PixelUtils.makeColor(channels));
            }
        }
        
        Sprite out = new Sprite(gray.width, gray.height, gray.pWidth, gray.pHeight, pixelData);
        return out;
    }
    
    public static Sprite overlaySprite(Sprite src, Pixel[] overlay){
        //init pixel array
        Pixel[] pixelData = new Pixel[src.pixels.length];
        for(int i = 0; i < pixelData.length; i++){
            pixelData[i] = new ColorPixel(PixelUtils.makeColor(new int[]{255,0,0,255}));
        }
        
        for(int i = 0; i < src.pWidth; i++){
            for(int j = 0; j < src.pHeight; j++){
                int[] channels = PixelUtils.getRGBA(src.getPixelColor(i, j));
                int[] color = PixelUtils.getRGBA(overlay[i+(j * src.pWidth)].getColor());
                float[] conversion = new float[]{
                    //r
                    ((((float)channels[0])/255.0f)*(float)color[0]),
                    //g
                    ((((float)channels[1])/255.0f)*(float)color[1]),
                    //b
                    ((((float)channels[2])/255.0f)*(float)color[2]),
                    //a
                    ((((float)channels[3])/255.0f)*(float)color[3]),
                };
                for(int k = 0; k < channels.length; k++){
                    channels[k] = (int)conversion[k];
                }

                pixelData[i+(j*src.pWidth)] = new ColorPixel(PixelUtils.makeColor(channels));
            }
        }
        
        Sprite out = new Sprite(src.width, src.height, src.pWidth, src.pHeight, pixelData);
        return out;
    }
    
    public static Pixel[] overlaySpritePixels(Sprite src, Pixel[] overlay){
        //init pixel array
        Pixel[] pixelData = new Pixel[src.pixels.length];
        for(int i = 0; i < pixelData.length; i++){
            pixelData[i] = new ColorPixel(PixelUtils.makeColor(new int[]{255,0,0,255}));
        }
        
        for(int i = 0; i < src.pWidth; i++){
            for(int j = 0; j < src.pHeight; j++){
                int[] channels = PixelUtils.getRGBA(src.getPixelColor(i, j));
                int[] color = PixelUtils.getRGBA(overlay[i+(j * src.pWidth)].getColor());
                float[] conversion = new float[]{
                    //r
                    ((((float)channels[0])/255.0f)*(float)color[0]),
                    //g
                    ((((float)channels[1])/255.0f)*(float)color[1]),
                    //b
                    ((((float)channels[2])/255.0f)*(float)color[2]),
                    //a
                    ((((float)channels[3])/255.0f)*(float)color[3]),
                };
                for(int k = 0; k < channels.length; k++){
                    channels[k] = (int)conversion[k];
                }

                pixelData[i+(j*src.pWidth)] = new ColorPixel(PixelUtils.makeColor(channels));
            }
        }
        return pixelData;
    }
    
    public static Sprite flipSpriteHorisontal(Sprite toFlip){
        //init pixel array
        Pixel[] pixelData = new Pixel[toFlip.pixels.length];
        for(int j = 0; j < toFlip.pHeight; j++){
            for(int i = 0; i < toFlip.pWidth; i++){
                pixelData[i+(j*toFlip.pWidth)] = toFlip.pixels[(toFlip.pWidth-i-1)+(j*toFlip.pWidth)];
            }
        }
        Sprite out = new Sprite(toFlip.width, toFlip.height, toFlip.pWidth, toFlip.pHeight, pixelData);
        return out;
    }
    
    public static Sprite flipSpriteVertical(Sprite toFlip){
        //init pixel array
        Pixel[] pixelData = new Pixel[toFlip.pixels.length];
        for(int j = 0; j < toFlip.pHeight; j++){
            for(int i = 0; i < toFlip.pWidth; i++){
                pixelData[i+(j*toFlip.pWidth)] = toFlip.pixels[(i)+((toFlip.pHeight-j-1)*toFlip.pWidth)];
            }
        }
        Sprite out = new Sprite(toFlip.width, toFlip.height, toFlip.pWidth, toFlip.pHeight, pixelData);
        return out;
    }
    
    public static Sprite outlineSprite(Sprite gray){
        //init pixel array
        Pixel[] firstPass = new Pixel[(gray.pWidth+2)*(gray.pHeight+2)];
        for(int i = 0; i < firstPass.length; i++){
            firstPass[i] = new ColorPixel(PixelUtils.makeColor(new int[]{0,0,0,0}));
        }
        
        for(int i = 0; i < gray.pWidth; i++){
            for(int j = 0; j < gray.pHeight; j++){
                if(gray.getPixelColor(i, j)==0){
                    //any of the following
                    boolean Up  = false;
                    boolean Down = false;
                    boolean Left = false;
                    boolean Right = false; 
                    
                    int sampleX = i;
                    int sampleY = j-1;
                    if(sampleX>=0 && sampleX < gray.pWidth){
                        if(sampleY>=0 && sampleY < gray.pHeight){
                            if(gray.getPixelColor(sampleX, sampleY)!=0){
                                Up = true;
                            }
                        }
                    }
                    
                     sampleX = i;
                     sampleY = j+1;
                    if(sampleX>=0 && sampleX < gray.pWidth){
                        if(sampleY>=0 && sampleY < gray.pHeight){
                            if(gray.getPixelColor(sampleX, sampleY)!=0){
                                Down = true;
                            }
                        }
                    }
                    
                     sampleX = i-1;
                     sampleY = j;
                    if(sampleX>=0 && sampleX < gray.pWidth){
                        if(sampleY>=0 && sampleY < gray.pHeight){
                            if(gray.getPixelColor(sampleX, sampleY)!=0){
                                Left = true;
                            }
                        }
                    }
                    
                     sampleX = i+1;
                     sampleY = j;
                    if(sampleX>=0 && sampleX < gray.pWidth){
                        if(sampleY>=0 && sampleY < gray.pHeight){
                            if(gray.getPixelColor(sampleX, sampleY)!=0){
                                Right = true;
                            }
                        }
                    }
                    
                    if(Up||Down||Left||Right){
                        firstPass[(i)+((j)*gray.pWidth)] =  new ColorPixel(PixelUtils.makeColor(new int[]{255,255,255,255}));
                    }
                }
            }
        }
        
        Sprite out = new Sprite(gray.width, gray.height, gray.pWidth, gray.pHeight, firstPass);
        return out;
    }

	
}
