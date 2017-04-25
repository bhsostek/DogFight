/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Editor.Attribute;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.Vector3D;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public abstract class Entity {
    
    protected float x = 0;
    protected float y = 0;
    public int width = 0;
    public int height = 0;
    
    public RigidBody collision;
    
    private Node[] nodes = new Node[]{};
    private Attribute[] attributes;
    
    private Attribute<String> id = new Attribute<String>("ID:","");
    
    private Entity link = null;
    private float offsetX = 0;
    private float offsetY = 0;
    
    private final EnumEntityType type;
    
    private String savePath = "";
    
    private boolean alwaysRender = false;
    private boolean shouldSave = true;
    
    public Entity(int x, int y, int width, int height, EnumEntityType eet){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        type = eet;
        attributes  = new Attribute[]{
            id,
            new Attribute<>("Entity Type:", type.toString()),
        };
        this.collision = PrebuiltBodies.quad(new Point2D(x,y), width, height);
    }
    
    public Entity setID(String id){
        System.out.println("-----In ID:"+id);
        if(id.isEmpty()){
            id = "id:"+Math.random();
        }
        this.id.setData(id);
        return this;
    }
    
    public void setNodes(Node[] nodes){
        for(int i = 0; i < nodes.length; i++){
            nodes[i].offsetX=(int)x;
            nodes[i].offsetY=(int)y;
        }
        this.nodes = nodes;
    }
    
    public void setExtraSavePath(String path){
        this.savePath = path;
    }
    
    public void addAttribute(Attribute a){
        Attribute[] tempAttributes = new Attribute[attributes.length+1];
        for(int i = 0; i < attributes.length; i++){
            tempAttributes[i] = attributes[i];
        }
        tempAttributes[attributes.length] = a;
        attributes = tempAttributes;
    }
    
    public void removeAttribute(Attribute a){
        Attribute[] tempAttributes = new Attribute[attributes.length-1];
        boolean hasAttribute = false;
        int index = 0;
        for(int i = 0; i < attributes.length; i++){
            loop:{
                if(attributes[i].equals(a)){
                    hasAttribute = true;
                    break loop;
                }
                tempAttributes[index] = attributes[i];
                index++;
            }
        }
        if(hasAttribute){
            attributes = tempAttributes;
        }
    }
    
    public Attribute[] getAttributes(){
        return this.attributes;
    }
    
    public Node[] getNodes(){
        return this.nodes;
    }
    
    public void tick(){
        if(this.link!=null){
            this.offsetToLinked();
        }
        collision.setPosition(x, y, 0);
        for(int i = 0; i < nodes.length; i++){
            nodes[i].offsetX=(int)x;
            nodes[i].offsetY=(int)y;
        }
        update();
    }
    
    public abstract void update();
    public abstract void render(Graphics g);
    
    public void drawNodes(Graphics g){
        for(int i = 0; i < nodes.length; i++){
            nodes[i].render(g);
        }
    }
    
    
    public void onAdd(){
        return;
    }
    
    public void onRemove(){
        return;
    }
    
    protected String extraSaveData(){
        return "";
    }
    
    public String save(){
        return this.getType()+"{"+this.x+","+(this.y)+","+this.id.getData()+extraSaveData()+"}";
    }
    
    //used in the editor
    public void onMove(){
        return;
    }
    
    public void onAttributeChange(){
        return;
    }
    
    public final void offsetToLinked() {
        if(link != null){
            this.x = link.x-offsetX;
            this.y = link.y-offsetY;
        }
        this.onMove();
    }

    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    protected void offset(Vector3D offset){
        this.x += offset.getX();
        this.y += offset.getY();
    }
    
    public final void link(Entity e){
        this.link = e;
        offsetX = e.x - this.x;
        offsetY = e.y - this.y;
    }
    
    public String getID(){
        return this.id.getData();
    }
    
    public EnumEntityType getType(){
        return this.type;
    }
    
    public String[] saveAdditionalData(){
        return new String[]{};
    }
    
    public String getAdditionalSavePath(){
        return this.savePath;
    }
    
    public final boolean isDynamic(){
        return !this.savePath.isEmpty();
    }
   
    public float getX(){
        return this.x;
    }
    
    public float getY(){
        return this.y;
    }
    
    public void setX(float x){
        this.x = x;
    }
    
    public void setY(float y){
        this.y = y;
    }
    
    public void setAlwaysRender(boolean b){
        this.alwaysRender = b;
    }
    
    public void setShouldSave(boolean b){
        this.shouldSave = b;
    }
    
    public boolean getAlwaysRender(){
        return this.alwaysRender;
    }
    
    public boolean getShouldSave(){
        return this.shouldSave;
    }
}
