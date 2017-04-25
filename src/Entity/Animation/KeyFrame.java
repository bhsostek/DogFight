/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Animation;

/**
 *
 * @author Bailey
 */
public class KeyFrame {
    float x = 0;
    float y = 0;
    float angle = 0;
    float time = 0;
    int spin = 1;
    
    public KeyFrame(float x, float y, float rotation, int time, int spin){
        this.x = x;
        this.y = y;
        this.angle = rotation;
        this.time = time;
        this.spin = spin;
    }
}
