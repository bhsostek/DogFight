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
public class PrebuiltBodies {
    
    //vertical walls
    public static RigidBody quad(Point origin, int width){
        RigidBody temp = new RigidBody(
           new Point[]{
               new Point3D(- (width/2), - (width/2), origin.getZ()),
               new Point3D(+ (width/2), - (width/2), origin.getZ()),
               new Point3D(+ (width/2), + (width/2), origin.getZ()),
               new Point3D(- (width/2), + (width/2), origin.getZ()),
           }
        );
        temp.Translate((int)origin.getX(), (int)origin.getY(), (int)origin.getZ());
        return temp;
    }
    
    public static RigidBody quad(Point origin, int width, int height){
        RigidBody temp = new RigidBody(
           new Point[]{
               new Point3D(- (width/2), - (height/2), origin.getZ()),
               new Point3D(+ (width/2), - (height/2), origin.getZ()),
               new Point3D(+ (width/2), + (height/2), origin.getZ()),
               new Point3D(- (width/2), + (height/2), origin.getZ()),
           }
        );
        temp.Translate((int)origin.getX(), (int)origin.getY(), (int)origin.getZ());
        return temp;
    }
    
    
}
