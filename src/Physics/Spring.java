/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

/**
 *
 * @author Bailey
 */
public class Spring {
    Vector3D position;
    private float targetWidth;
    private float targetHeight;
    private final float k = 0.015f; // adjust this value to your liking
    private final float DAMPING = 0.98f;
    Vector3D velocity = new Vector3D(0,0,0);
    Vector3D acceleration = new Vector3D(0,0,0);
    
    private Vector3D speed = new Vector3D(0,0,0);
    
    public Spring(int x, int y){
        this.position = new Vector3D(x, y, 0);
        this.targetWidth = x;
        this.targetHeight = y;
    }
    
    public void tick(){
        float forceX = speed.getX();
        if(velocity.getX()>=-1 && velocity.getX()<=1){
            velocity.setVelX(0);
        }
        float dx = (position.getX()) - targetWidth;
        forceX += dx * -k;
        acceleration.setVelX(forceX / 1.0f);
        velocity.setVelX(DAMPING * (velocity.getX() + acceleration.getX()));
        position.increaseVelX(velocity.xDir);
        speed.setVelX(0);
        
        float forceY = speed.getY();
        if(velocity.getY()>=-1 && velocity.getY()<=1){
            velocity.setVelY(0);
        }
        float dy = (position.getY()) - targetHeight;
        forceY += dy * -k;
        acceleration.setVelY(forceY / 1.0f);
        velocity.setVelY(DAMPING * (velocity.getY() + acceleration.getY()));
        position.increaseVelY(velocity.yDir);
        speed.setVelY(0);
    }

    public void increaseTargetHeight(float f){
        this.targetHeight+=f;
    }
    
    public float getHeight(){
        return position.yDir;
    }
    
    public float getTargetHight(){
        return this.targetHeight;
    }
    
    public void increaseHeight(float f){
        this.position.yDir+=f;
    }
    
    public Vector3D getSpeed(){
        return this.speed;
    }
    
    public void setSpeed(float f){
        this.speed.setVelY(f);
    }
    
    public void increaseSpeed(float f){
        this.speed.increaseVelY(f);
    }
    
    public float getX(){
        return this.position.getX();
    }
    
    public float getY(){
        return this.position.getY();
    }
    
    public void setX(float x){
        this.position.xDir = x;
        this.targetWidth = x;
    }
    
    public void setY(float y){
        this.position.xDir = y;
        this.targetHeight = y;
    }
    
    public Vector3D getVelocity(){
        return this.velocity;
    }
    
    public Vector3D getAcceleration(){
        return this.acceleration;
    }
    
    public void killInertia(){
        this.position.setVelX(targetWidth);
        this.position.setVelY(targetHeight);
        this.speed.setVelX(0);
        this.speed.setVelY(0);
        this.velocity.setVelX(0);
        this.velocity.setVelY(0);
        this.acceleration.setVelX(0);
        this.acceleration.setVelY(0);
    }
}
