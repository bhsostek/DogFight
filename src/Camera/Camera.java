/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Camera;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Entity.Entity;
import Physics.Vector3D;

/**
 *
 * @author Bailey
 */
public class Camera {
    public float x = 0;
    public float y = 0;
    public int rot = 0;
    public float scale = 1.0f;
    
    public int countdown = 0;
    Vector3D translation = new Vector3D(0,0,0);
    
    Entity linked = null;
    
    public Camera(int x, int y, int rot){
        this.x = x;
        this.y = y;
        this.rot = rot;
    }
    
    public void tick(){
        if(Keyboard.LEFT){
            rot+=1;
        }
        if(Keyboard.RIGHT){
            rot-=1;
        }
        if(Keyboard.UP){
            scale+=0.1f;
        }
        if(Keyboard.DOWN){
            scale-=0.1f;
        }
        if(linked != null){
            this.x = linked.getX()-TowerOfPuzzles.WIDTH/2;
            this.y = linked.getY()-TowerOfPuzzles.HEIGHT/2;
        }
    }
    
    public void link(Entity e){
        this.linked = e;
    }
    
    public void gotoPos(float x, float y, int ticks){
    	this.linked = null;
    	this.x = (int)x-(TowerOfPuzzles.WIDTH/2);
    	this.y = (int)y-(TowerOfPuzzles.HEIGHT/2);
    	countdown = ticks;
    }
    
}
