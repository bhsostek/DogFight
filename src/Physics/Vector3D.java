/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Physics;

/**
 *
 * @author Bayjose
 */
public class Vector3D {
    float xDir=0.0F;
    float yDir=0.0F;
    float zDir=0.0F;
    
    public Vector3D(float x, float y, float z){
        this.xDir=x;
        this.yDir=y;
        this.zDir=z;
    }
    
    public float getX(){
        return this.xDir;
    }
    
    public float getY(){
        return this.yDir;
    }
    
    public float getZ(){
        return this.zDir;
    }
    
    public void setVelZ(float velForwards){
        this.zDir=velForwards;
    }
    
    public void setVelX(float velForwards){
        this.xDir=velForwards;
    }
    public void setVelY(float velForwards){
        this.yDir=velForwards;
    }
    
    public void increaseVelX(float x){
        this.xDir+=x;
    }
    public void increaseVelY(float y){
        this.yDir+=y;
    }
    public void increaseVelZ(float z){
        this.zDir+=z;
    }
    
    public Vector3D addVector(Vector3D vector){
        this.xDir+=vector.getX();
        this.yDir+=vector.getY();
        this.zDir+=vector.getZ();
        return this;
    }
    
    public Vector3D inverse(){
        return new Vector3D(-this.getX(), -this.getY(), -this.getZ());
    }
    
    public boolean isEqualTo(Vector3D test){
        if(test.getX()==this.getX()){
            if(test.getY()==this.getY()){
                if(test.getZ()==this.getZ()){
                    return true;
                }
            } 
        }
        return false;
    }
    
    public Vector3D newInstance(){
        return new Vector3D(this.getX(), this.getY(), this.getZ());
    }
    
    public float dotProduct(Vector3D compare){
        float uv = (this.getX()*compare.getX()) + (this.getY() * compare.getY()) + (this.getZ() + compare.getZ());
        float u = (float)(Math.sqrt((this.getX()*this.getX())+(this.getY()*this.getY())+(this.getZ() * this.getZ())));
        float v = (float)(Math.sqrt((compare.getX()*compare.getX())+(compare.getY()*compare.getY())+(compare.getZ() * compare.getZ())));
        return (float)Math.acos(uv/u*v);
    }
    
    public Vector3D multiplyVector(Vector3D magnitude){
        return new Vector3D(this.xDir*magnitude.getX(), this.yDir*magnitude.getY(), this.zDir*magnitude.getZ());
    }
    
    public Vector3D rotateX(float angle){

        this.xDir = (float) ((this.xDir * Math.cos(Math.toRadians(angle))) + (this.yDir * Math.sin(Math.toRadians(angle))));
        this.yDir = (float) ((this.xDir * Math.sin(Math.toRadians(angle))) - (this.yDir * Math.cos(Math.toRadians(angle)))) * -1;
        return this;
    }
    
    public Vector3D normalize(){
        float largest = 0;
        
        if(Math.abs(xDir)>largest){
            largest = Math.abs(xDir);
        }
        if(Math.abs(yDir)>largest){
            largest = Math.abs(yDir);
        }
        if(Math.abs(zDir)>largest){
            largest = Math.abs(zDir);
        }
        
        if(largest == 0){
            return this;
        }
        return new Vector3D(this.getX()/largest,this.getY()/largest,this.getZ()/largest);
    }
    
    public float getAngle(){
        float out;
        
        float o = this.yDir;
        float a = this.xDir;

        
        if(a == 0){
            return 0;
        }
        
        out = (float) Math.toDegrees(Math.atan(o/a));
        out -= 90; //Offset the vector by 90 degrees
        if(a>0){
            out +=180;
        }
        
        return out;
    }
}
