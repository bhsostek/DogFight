/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Base.util.DistanceCalculator;
import Editor.Attribute;
import Physics.PhysicsEngine;
import Physics.Point;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.RigidUtils;
import Physics.Spring;
import Physics.Vector3D;
import Sound.SoundManager;
import World.Tiles.TileConstants;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityWater extends Entity{

    private Spring[] springs;
    private boolean debounce = true;
    private RigidBody collision;
    
    private boolean moving = false;
    private boolean lastMoving = moving;
    
    public float volume = TileConstants.SCALE;
    
    private final float initialY;
    
    EntityParticleEmitter pe;
    
    int t_width;
    int t_height;
    
    Attribute<Integer> widthAttribute;
    Attribute<Integer> heightAttribute;
    
    public EntityWater(int x, int y, int width, int height) {
        super(x+(TileConstants.SCALE*width)/2, y+(TileConstants.SCALE*height)/2, TileConstants.SCALE*width, TileConstants.SCALE*height, EnumEntityType.WATER);
        int numSprings = (int)(width * 4)+1;
        springs = new Spring[numSprings];
        for(int i = 0; i < springs.length; i++){
            springs[i] = new Spring(x+((TileConstants.SCALE/4)*i), y);
        }
        
        initialY = (y+(height * TileConstants.SCALE));

        t_width = width;
        t_height = height;
        
        PhysicsEngine.addChannel("water");
        PhysicsEngine.activeChannel = "water";
        
        Point[] waterPoints = new Point[this.springs.length+2];
        for(int i = 0; i < springs.length; i++){
            waterPoints[i] = new Point2D(springs[i].getX(), springs[i].getY());
        }
        waterPoints[waterPoints.length-2] = new Point2D(x+width, y+height);
        waterPoints[waterPoints.length-1] = new Point2D(x, y+height);
        collision = new RigidBody(waterPoints);
        collision.setColor(new Color(0, 255, 255, 128));
        PhysicsEngine.addToChannel("water", collision);

        PhysicsEngine.getChannel("water").setColor(new Color(0, 255, 255, 128));
        
        pe = new EntityParticleEmitter(x, y,"water");
        
        pe.setLifespan(180);
        
        pe.setOffsetXBottom(0.0f);
        pe.setOffsetXTop(super.width);
        pe.setOffsetYBottom(0.0f);
        pe.setOffsetYTop(super.height);
        
        pe.setNumParticles(30);
        
        pe.link(this);
        
        pe.setBoundXBottom(0);
        pe.setBoundXTop(0);
        pe.setBoundYBottom(0);
        pe.setBoundYTop(0);
        
        widthAttribute = new Attribute<Integer>("Width:", t_width);
        heightAttribute = new Attribute<Integer>("Height:", t_height);
         
        super.addAttribute(widthAttribute);
        super.addAttribute(heightAttribute);
    }
    
    @Override
    public void onRemove(){
        PhysicsEngine.getChannel("water").remove(super.collision);
        pe.onRemove();
    }

    @Override
    public void onMove(){
        moving = true;
        pe.offsetToLinked();
    }
    
    public void buildBody(){
        if(collision!=null){
            PhysicsEngine.getChannel("water").remove(collision);
        }
        Point[] waterPoints = new Point[this.springs.length+2];
        for(int i = 0; i < springs.length; i++){
            waterPoints[i] = new Point2D(springs[i].getX(), springs[i].getY());
        }
        waterPoints[waterPoints.length-2] = new Point2D(springs[0].getX()+(t_width * TileConstants.SCALE), initialY);
        waterPoints[waterPoints.length-1] = new Point2D(springs[0].getX(), initialY);
        collision = new RigidBody(waterPoints);
        collision.setColor(new Color(0, 255, 255, 128));
        PhysicsEngine.addToChannel("water", collision);
    }
    
    @Override
    public void onAttributeChange(){
        t_width = widthAttribute.getData();
        t_height =heightAttribute.getData();
        this.setWidth(t_width);
        super.collision = PrebuiltBodies.quad(new Point2D(0,0), t_width * TileConstants.SCALE, t_height * TileConstants.SCALE);
        RigidUtils.MoveTo(new Vector3D(collision.x, collision.y,0), super.collision);
        this.moving=true;
    }

    @Override
    public void update() {
        widthAttribute.setData(t_width);
        heightAttribute.setData(t_height);
        
        if(lastMoving != moving){
            if(!moving){
                for(int i = 0; i < springs.length; i++){
                    springs[i].setX(super.x +((TileConstants.SCALE/4)*i) - (t_width * TileConstants.SCALE / 2));
                    springs[i].setY(super.y- (t_height * TileConstants.SCALE / 2));
                    springs[i].killInertia();
                }
            }
        }
        if(debounce){
            if(PhysicsEngine.intersects(collision, "player")){
                this.Splash(pickClosestNode((int)TowerOfPuzzles.player.x,(int)TowerOfPuzzles.player.y), TowerOfPuzzles.player.getDownwardVel() * 4);
                debounce = false;
            }
        }else{
            if(TowerOfPuzzles.player.getDownwardVel() == 0){
                if(!PhysicsEngine.collides("player", "water")){
                    debounce = true;
                }
            }
        }
        
        pe.setOffsetYBottom((springs[0].getTargetHight() - super.y));
//        System.out.println("Height:"+super.height+" Target:"+(springs[0].getTargetHight() - super.y));
        pe.tick();
        
        for(int i = 0; i < PhysicsEngine.getChannel("waterDrop").collisons.length; i++){
            RigidBody bod = PhysicsEngine.getChannel("waterDrop").collisons[i];
            if(RigidUtils.Collides(bod, this.collision)){
                this.Splash(pickClosestNode((int)bod.x,(int)bod.y), 8.0f);
                PhysicsEngine.getChannel("waterDrop").remove(bod);
                for(int j = 0; j < springs.length; j++){
                    springs[j].increaseTargetHeight(-1.0f * 4);
                }
            }
        }
        
        for (int i = 0; i < springs.length; i++) {
            springs[i].tick();
        }

        float[] leftDeltas = new float[springs.length];
        float[] rightDeltas = new float[springs.length];

        // do some passes where springs pull on their neighbours

        for (int i = 0; i < springs.length; i++) {
            if (i > 0) {
                leftDeltas[i] = (springs[i].getHeight() - springs[i - 1].getHeight())/2;
                springs[i - 1].getAcceleration().increaseVelY(leftDeltas[i]);
            }
            if (i < springs.length - 1) {
                rightDeltas[i] = (springs[i].getHeight() - springs[i + 1].getHeight())/2;
                springs[i + 1].getAcceleration().increaseVelY(rightDeltas[i]);
            }
        }

        for (int i = 0; i < springs.length; i++) {
            if (i > 0) {
                springs[i - 1].increaseHeight(leftDeltas[i]);
            }
            if (i < springs.length - 1) {
                springs[i + 1].increaseHeight(rightDeltas[i]);
            }
        }
        
        buildBody();
        
        lastMoving = moving;
        moving = false;
    }
    
    public void setWidth(int j){
        this.t_width += j-t_width;
        int numSprings = (int)(j * 4)+1;
        springs = new Spring[numSprings];
        for(int i = 0; i < springs.length; i++){
            springs[i] = new Spring((int)x+((TileConstants.SCALE/4)*i) - super.width/2, (int)y - super.height/2);
        }
        this.buildBody();
    }
    
    @Override
    public void render(Graphics g) {
        RigidUtils.Render(collision, g);
//        g.setColor(Color.red);
//        for(Spring s : springs){
//            g.drawOval((int)s.getX() -1 , (int)s.getY() -1, 3, 3);
//        }
        pe.render(g);
    }
    
    public void Splash(int index, float speed){
        if(speed > 20.0f){
            EntityPositionalAudio audio = new EntityPositionalAudio((int)springs[index].getX(), (int)springs[index].getY(), "splash.wav", speed*20, false);
            audio.setKillOnEnd(true);
            audio.setShouldSave(false);
            TowerOfPuzzles.entityManager.add(audio);
        }else{
            EntityPositionalAudio audio = new EntityPositionalAudio((int)springs[index].getX(), (int)springs[index].getY(), "drip.wav", speed*20, false);
            audio.setKillOnEnd(true);
            audio.setShouldSave(false);
            TowerOfPuzzles.entityManager.add(audio);
        }
        if (index >= 0 && index < springs.length){
            springs[index].setSpeed(speed);
        }
    }
    
    public int pickClosestNode(int x, int y){
        int smallest = Integer.MAX_VALUE;
        int index = 0;
        for(int i = 0; i < this.springs.length; i++){
            int temp = DistanceCalculator.CalculateDistance(x, y, (int)this.springs[i].getX(), (int)this.springs[i].getY());
            if(temp <= smallest){
                smallest = temp;
                index = i;
            }
        }
        return index;
    }
    
    @Override
    public String save(){
        return super.getType().name()+"{"+(super.x-(this.widthAttribute.getData()*TileConstants.SCALE/2))+","+(super.y-(this.heightAttribute.getData()*TileConstants.SCALE/2))+","+this.widthAttribute.getData()+","+this.heightAttribute.getData()+"}";
    }
}
