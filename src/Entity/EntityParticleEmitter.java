/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Base.util.StringUtils;
import Editor.Attribute;
import Editor.SaveManager;
import Graphics.Particle;
import Graphics.Sprite;
import Physics.PhysicsEngine;
import Physics.Vector3D;
import World.Tiles.TileConstants;
import java.awt.Graphics;
import javax.swing.JOptionPane;

/**
 *
 * @author Bailey
 */
public class EntityParticleEmitter extends Entity{
    
    private Particle[] particles;
    
    private Attribute<String> mainSprite = new Attribute<>("Sprite:","");
    private Attribute<Integer> pwidth = new Attribute<Integer>("Subsprite Width:",1);
    private Attribute<Integer> pheight = new Attribute<Integer>("Subsprite Height:",1);
    private Attribute<Integer> maxParticles = new Attribute<>("numParticles:",30);
    private Attribute<Integer> lifespan = new Attribute<>("Lifespan:",60);
    private Attribute<Boolean> collisionTest = new Attribute<>("CollisionTest:",false);
    private Attribute<Boolean> useGravity = new Attribute<>("Use Gravity:",false);
    private Attribute<Boolean> canAdd = new Attribute<>("canAdd:",true);
    private Attribute<Float> rotationConstant = new Attribute<>("rotation constant:",0.0f);
    
    Sprite main;
    Sprite[] subSprites;
    
    private int steps;
    
    private Attribute<Float> rangeXb = new Attribute<>("Range X Bottom:",-1.0f);
    private Attribute<Float> rangeXt = new Attribute<>("Range X Top:",1.0f);
    private Attribute<Float> rangeYb = new Attribute<>("Range Y Bottom:",-1.0f);
    private Attribute<Float> rangeYt = new Attribute<>("Range Y Top:",1.0f);
    
    
    //Cool stuff
    private Attribute<Boolean> pileUp = new Attribute<Boolean>("Pile Up:",false);
    private Attribute<String> prefabPath = new Attribute<String>("PrefabPath:","");
    
    private float offsetXb = 0;
    private float offsetXt = 0;
    private float offsetYb = 0;
    private float offsetYt = 0;
    
    private boolean setDissipate = false;
    
    public EntityParticleEmitter(int x, int y, String prefab) {
        super(x, y, TileConstants.SCALE * 2, TileConstants.SCALE * 2, EnumEntityType.PARTICLE_EMITTER);
        
        super.setAlwaysRender(true);
        
        //check extension
        if(!prefab.contains(".txt")){
            prefab = prefab+".txt";
        }
        
        String[] attributeModifiers = StringUtils.loadData(TowerOfPuzzles.Path+TowerOfPuzzles.PrefabPath+prefab);
        
        for(int i = 0; i < attributeModifiers.length; i++){
            String line = StringUtils.removeFrontSpacing(attributeModifiers[i]);
            //Integers
            if(line.startsWith("width")){
                this.pwidth.setData(Integer.parseInt(line.split("=")[1]));
            }    
            if(line.startsWith("height")){
                this.pheight.setData(Integer.parseInt(line.split("=")[1]));
            } 
            if(line.startsWith("lifespan")){
                this.lifespan.setData(Integer.parseInt(line.split("=")[1]));
            } 
            if(line.startsWith("maxParticles")){
                this.maxParticles.setData(Integer.parseInt(line.split("=")[1]));
            } 
            //Float
            if(line.startsWith("rangeXb")){
                this.rangeXb.setData(Float.parseFloat(line.split("=")[1]));
            } 
            if(line.startsWith("rangeXt")){
                this.rangeXt.setData(Float.parseFloat(line.split("=")[1]));
            } 
            if(line.startsWith("rangeYb")){
                this.rangeYb.setData(Float.parseFloat(line.split("=")[1]));
            } 
            if(line.startsWith("rangeYt")){
                this.rangeYt.setData(Float.parseFloat(line.split("=")[1]));
            } 
            if(line.startsWith("rotationConstant")){
                this.rotationConstant.setData(Float.parseFloat(line.split("=")[1]));
            } 
            //Boolean
            if(line.startsWith("useGravity")){
                this.useGravity.setData(Boolean.parseBoolean(line.split("=")[1]));
            } 
            if(line.startsWith("collisionTest")){
                this.collisionTest.setData(Boolean.parseBoolean(line.split("=")[1]));
            }
            if(line.startsWith("pileUp")){
                this.pileUp.setData(Boolean.parseBoolean(line.split("=")[1]));
            }
            if(line.startsWith("canAdd")){
                this.canAdd.setData(Boolean.parseBoolean(line.split("=")[1]));
            }
            //String
            if(line.startsWith("sprite")){
                setSprite("particles/"+line.split("=")[1]);
            } 
        }
        
        this.prefabPath.setData(prefab);
        

        particles = new Particle[maxParticles.getData()];
        
        super.addAttribute(mainSprite);
        super.addAttribute(maxParticles);
        super.addAttribute(this.lifespan);
        super.addAttribute(this.rangeXb);
        super.addAttribute(this.rangeXt);
        super.addAttribute(this.rangeYb);
        super.addAttribute(this.rangeYt);
        super.addAttribute(this.useGravity);
        super.addAttribute(this.collisionTest);
        super.addAttribute(this.canAdd);
        super.addAttribute(pwidth);
        super.addAttribute(pheight);
        super.addAttribute(this.pileUp);
        super.addAttribute(this.prefabPath);
        super.addAttribute(this.rotationConstant);
        this.onAttributeChange();
//        super.addAttribute(new Attribute<>("Lifespan:",this.lifespan));
//        super.addAttribute(new Attribute<>("Particle:",this.main));
        
    }
    
    private void setSprite(String main){
        this.mainSprite.setData(main);
        this.main = TowerOfPuzzles.spriteBinder.loadSprite(main);
        
        subSprites = new Sprite[(this.main.pHeight/pheight.getData()) * (this.main.pWidth/pwidth.getData())];
        steps = subSprites.length;
        
        for(int j = 0; j < this.main.pHeight/pheight.getData(); j++){
            for(int i = 0; i < this.main.pWidth/pwidth.getData(); i++){
                subSprites[i+(j * (this.main.pWidth/pwidth.getData()))] = this.main.getSubDisplay(i * pwidth.getData(), j * pheight.getData(), pwidth.getData(), pheight.getData());
            }
        }
    }
    
    public void setSprite(Sprite s){
        this.mainSprite.setData("Custom Sprite");
        this.main = s;
        
        subSprites = new Sprite[(this.main.pHeight/pheight.getData()) * (this.main.pWidth/pwidth.getData())];
        steps = subSprites.length;
        
        for(int j = 0; j < this.main.pHeight/pheight.getData(); j++){
            for(int i = 0; i < this.main.pWidth/pwidth.getData(); i++){
                subSprites[i+(j * (this.main.pWidth/pwidth.getData()))] = this.main.getSubDisplay(i * pwidth.getData(), j * pheight.getData(), pwidth.getData(), pheight.getData());
            }
        }
    }
    
    public void dissipateSprite(int x, int y, Sprite s){
        setSprite(s);
        this.setDissipate = true;
        this.canAdd.setData(false);
        this.setNumParticles(s.pWidth*s.pHeight);
        for(int j = 0; j < s.pHeight; j++){
            for(int i = 0; i < s.pWidth; i++){
                particles[i+(j*s.pWidth)] = new Particle(new Vector3D((float)((rangeXb.getData() * Math.random())+(rangeXt.getData() * Math.random())),(float)((rangeYb.getData() * Math.random())+(rangeYt.getData() * Math.random())),0), (lifespan.getData()/2) +(int)((lifespan.getData()/2) * Math.random()));
                particles[i+(j*s.pWidth)].setPosition(new Vector3D(x+(i*4), y+(j*4), 0));
                particles[i+(j*s.pWidth)].setIndex(i+(j*s.pWidth));
            }
        }
    }
    
    public EntityParticleEmitter setNumParticles(int j){
        this.maxParticles.setData(j);
        particles = new Particle[maxParticles.getData()];
        
        for (int i = 0; i < particles.length; i++) {
            particles[i] = null;
        }
        return this;
    }
    
    public EntityParticleEmitter setLifespan(int j){
        this.lifespan.setData(j);
        return this;
    }
    public EntityParticleEmitter setBoundXBottom(float j){
        this.rangeXb.setData(j);
        return this;
    }
    public EntityParticleEmitter setBoundXTop(float j){
        this.rangeXt.setData(j);
        return this;
    }
    public EntityParticleEmitter setBoundYBottom(float j){
        this.rangeYb.setData(j);
        return this;
    }
    public EntityParticleEmitter setBoundYTop(float j){
        this.rangeYt.setData(j);
        return this;
    }
    
    public EntityParticleEmitter setOffsetXBottom(float j){
        this.offsetXb = j;
        return this;
    }
    public EntityParticleEmitter setOffsetXTop(float j){
        this.offsetXt = j;
        return this;
    }
    public EntityParticleEmitter setOffsetYBottom(float j){
        this.offsetYb = j;
        return this;
    }
    public EntityParticleEmitter setOffsetYTop(float j){
        this.offsetYt = j;
        return this;
    }
    
    public EntityParticleEmitter testForCollision(boolean j){
        this.collisionTest.setData(j);
        return this;
    }
    
    public EntityParticleEmitter useGravity(boolean j){
        this.useGravity.setData(j);
        return this;
    }
    

    @Override
    public void onAttributeChange(){
        this.setSprite(this.mainSprite.getData());
        this.setNumParticles(this.maxParticles.getData());
        this.setLifespan(this.lifespan.getData());
        //prefab exists make it
        if(this.prefabPath.wasEdited()){
            //check extension
            if(!prefabPath.getData().contains(".txt")){
                prefabPath.setData(prefabPath.getData()+".txt");
            }
            if(!SaveManager.fileExists(TowerOfPuzzles.PrefabPath+this.prefabPath.getData())){
                SaveManager.saveFile(TowerOfPuzzles.PrefabPath+this.prefabPath.getData(), genPrefabSave());
            }else{
                int reply = JOptionPane.showConfirmDialog(null, "The prefab file:"+this.prefabPath.getData()+" exists, do you wish to overwrite it?", "That file exists!", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    SaveManager.saveFile(TowerOfPuzzles.PrefabPath+this.prefabPath.getData(), genPrefabSave());
                }else{
                    
                    
                    String[] attributeModifiers = StringUtils.loadData(TowerOfPuzzles.Path+TowerOfPuzzles.PrefabPath+prefabPath.getData());
        
                    for(int i = 0; i < attributeModifiers.length; i++){
                        String line = StringUtils.removeFrontSpacing(attributeModifiers[i]);
                        //Integers
                        if(line.startsWith("width")){
                            this.pwidth.setData(Integer.parseInt(line.split("=")[1]));
                        }    
                        if(line.startsWith("height")){
                            this.pheight.setData(Integer.parseInt(line.split("=")[1]));
                        } 
                        if(line.startsWith("lifespan")){
                            this.lifespan.setData(Integer.parseInt(line.split("=")[1]));
                        } 
                        if(line.startsWith("maxParticles")){
                            this.maxParticles.setData(Integer.parseInt(line.split("=")[1]));
                        } 
                        //Float
                        if(line.startsWith("rangeXb")){
                            this.rangeXb.setData(Float.parseFloat(line.split("=")[1]));
                        } 
                        if(line.startsWith("rangeXt")){
                            this.rangeXt.setData(Float.parseFloat(line.split("=")[1]));
                        } 
                        if(line.startsWith("rangeYb")){
                            this.rangeYb.setData(Float.parseFloat(line.split("=")[1]));
                        } 
                        if(line.startsWith("rangeYt")){
                            this.rangeYt.setData(Float.parseFloat(line.split("=")[1]));
                        } 
                        if(line.startsWith("rotationConstant")){
                            this.rotationConstant.setData(Float.parseFloat(line.split("=")[1]));
                        } 
                        //Boolean
                        if(line.startsWith("useGravity")){
                            this.useGravity.setData(Boolean.parseBoolean(line.split("=")[1]));
                        } 
                        if(line.startsWith("collisionTest")){
                            this.collisionTest.setData(Boolean.parseBoolean(line.split("=")[1]));
                        }
                        if(line.startsWith("pileUp")){
                            this.pileUp.setData(Boolean.parseBoolean(line.split("=")[1]));
                        }
                        if(line.startsWith("canAdd")){
                            this.canAdd.setData(Boolean.parseBoolean(line.split("=")[1]));
                        }
                        //String
                        if(line.startsWith("sprite")){
                            setSprite("particles/"+line.split("=")[1]);
                        } 
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        for(int i = 0; i < particles.length; i++){
            if(particles[i] != null){
                //collision
                if(collisionTest.getData()){
                    if(PhysicsEngine.pointIntersects(particles[i].getX(), particles[i].getY(), "solids")){
                        if(pileUp.getData()){
                            particles[i].velocity.setVelX(0);
                            particles[i].velocity.setVelY(0);
                            particles[i].velocity.setVelZ(0);
                            particles[i].acceleration.setVelX(0);
                            particles[i].acceleration.setVelY(0);
                            particles[i].acceleration.setVelZ(0);
                            if(useGravity.getData()){
                                particles[i].velocity.setVelY(-TowerOfPuzzles.GRAVITY);
                            }
                        }else{
                            particles[i].remove = true;
                        }
                    }
                }
                if(particles[i].remove){
                    if(canAdd.getData()){
                        particles[i] = new Particle(new Vector3D((float)((rangeXb.getData() * Math.random())+(rangeXt.getData() * Math.random())),(float)((rangeYb.getData() * Math.random())+(rangeYt.getData() * Math.random())),0), (lifespan.getData()/2) +(int)((lifespan.getData()/2) * Math.random()));
                        particles[i].setPosition(new Vector3D(super.x, super.y, 0));
                        particles[i].setOffset(calculateOffset());
                        //rotoation
                        particles[i].rotationConstant = (float) (this.rotationConstant.getData()-(this.rotationConstant.getData()*Math.random()*2));
                    }
                }
                //called before tick
                particles[i].calculateBlackHoles();
                if(useGravity.getData()){
                    particles[i].addGravity();
                }
                particles[i].tick();
            }else{
                if(canAdd.getData()){
                    particles[i] = new Particle(new Vector3D((float)((rangeXb.getData() * Math.random())+(rangeXt.getData() * Math.random())),(float)((rangeYb.getData() * Math.random())+(rangeYt.getData() * Math.random())),0), (lifespan.getData()/2) +(int)((lifespan.getData()/2) * Math.random()));
                    particles[i].setPosition(new Vector3D(super.x, super.y, 0));
                    particles[i].setOffset(calculateOffset());
                    //rotoation
                    particles[i].rotationConstant = (float) (this.rotationConstant.getData()-(this.rotationConstant.getData()*Math.random()*2));
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        for(Particle p : particles){
            if(p!=null){
                if(!p.remove){
                    if(steps>0){
                        Sprite s;
                        if(! this.setDissipate){
                            s = subSprites[p.getIndex(steps)-1];
                        }else{
                            s = subSprites[p.getIndex()];
                        }
                        if(this.rotationConstant.getData()>0){
                            s.render( p.getX(),  p.getY(), pwidth.getData() * 4,  pheight.getData() * 4, g, p.getRotation());
                        }else{
                            s.render( p.getX(),  p.getY(), pwidth.getData() * 4,  pheight.getData() * 4, g);
                        }
                    }
                }
            }
        }
    }
    
    private Vector3D calculateOffset(){
        float newX = (float)((Math.random() * (this.offsetXt - this.offsetXb)) + this.offsetXb);
        float newY = (float)((Math.random() * (this.offsetYt - this.offsetYb)) + this.offsetYb);
        
        return new Vector3D(newX, newY, 0);
    }
    
    public void setCanAdd(boolean b){
        this.canAdd.setData(b);
    }
    
    public void regenerateSystem(){
        for(int i = 0; i < this.particles.length; i++){
            this.particles[i] = null;
        }
    }
    
    @Override
    public String save(){
        return this.getType()+"{"+this.x+","+(this.y)+","+this.prefabPath.getData()+"}";
    }
    
    public String[] genPrefabSave(){
        return new String[]{
          "width="+this.pwidth.getData(),
          "height="+this.pheight.getData(),
          "lifespan="+this.lifespan.getData(),
          "maxParticles="+this.maxParticles.getData(),
          "rangeXb="+this.rangeXb.getData(),
          "rangeXt="+this.rangeXt.getData(),
          "rangeYb="+this.rangeYb.getData(),
          "rangeYt="+this.rangeYt.getData(),
          "useGravity="+this.useGravity.getData(),
          "collisionTest="+this.collisionTest.getData(),
          "pileUp="+this.pileUp.getData(),
          "canAdd="+this.canAdd.getData(),
          "sprite="+this.mainSprite.getData().replace("particles/", ""),
          "rotationConstant"+this.rotationConstant.getData(),
        };   
    }
    
    
}
