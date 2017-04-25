/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Graphics.GUI.UI;
import Base.Keyboard;
import Base.MousePositionLocator;
import Base.TowerOfPuzzles;
import Physics.PhysicsEngine;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.RigidUtils;
import Physics.Vector3D;
import World.Tiles.TileConstants;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JOptionPane;

/**
 *
 * @author Bailey
 */
public class CollisionEditor extends UI{

    RigidBody selected = null;
    boolean moving = false;
    
    public CollisionEditor(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick() {
        if(Keyboard.ESC){
            this.selected = null;
        }
        if(this.selected!=null){
            if(Keyboard.DELETE||Keyboard.BACKSPACE){
                PhysicsEngine.getChannel("toAdd").remove(selected);
                this.selected = null;
                return;
            }
        }
        if(TowerOfPuzzles.mouseInput.IsPressed && !TowerOfPuzzles.mouseInput.IsRightClick){
            if(moving == true){
                if(selected != null){
                    int pos_x = MousePositionLocator.MouseLocation.x+(int)TowerOfPuzzles.cam.x;
                    int pos_y = MousePositionLocator.MouseLocation.y+(int)TowerOfPuzzles.cam.y;
                    if(Editor.SNAP_TO_GRID){
                        pos_x/=TileConstants.SCALE;
                        pos_y/=TileConstants.SCALE;
                        pos_x*=TileConstants.SCALE;
                        pos_y*=TileConstants.SCALE;
                        if((this.selected.getCollision().getBounds().getWidth()/TileConstants.SCALE)%2==1){
                            pos_x+=TileConstants.SCALE/2;
                        }
                        if((this.selected.getCollision().getBounds().getHeight()/TileConstants.SCALE)%2==1){
                            pos_y+=TileConstants.SCALE/2;
                        }
                    }
                    RigidUtils.MoveTo(new Vector3D(pos_x, pos_y, 0), selected);
                }
            }
        }else{
            moving = false;
        }
    }

    @Override
    public void render(Graphics g) {
        g.translate(-(int)TowerOfPuzzles.cam.x, -(int)TowerOfPuzzles.cam.y);
        for(RigidBody bod: PhysicsEngine.getChannel("toAdd").collisons){
            RigidUtils.RenderWireframe(bod, g);
        }
        if(selected!=null){
            RigidUtils.Render(selected, g);
        }
        g.translate((int)TowerOfPuzzles.cam.x, (int)TowerOfPuzzles.cam.y);
        g.drawRect(MousePositionLocator.MouseLocation.x, MousePositionLocator.MouseLocation.y,8,8);
    }

    @Override
    public void onClick(Rectangle rect) {
        if(!rect.intersects(Editor.MENU_BAR)){
            for(RigidBody bod: PhysicsEngine.getChannel("toAdd").collisons){
                if(RigidUtils.Collides(new Rectangle(rect.x+(int)TowerOfPuzzles.cam.x, rect.y+(int)TowerOfPuzzles.cam.y, rect.width, rect.height), bod)){
                    if(bod.equals(selected)){
                        if(TowerOfPuzzles.mouseInput.IsRightClick){
                            try{
                                int newX = Integer.parseInt(JOptionPane.showInputDialog("New Width in Tiles", "1"));
                                int newY = Integer.parseInt(JOptionPane.showInputDialog("New Height in Tiles", "1"));
                                PhysicsEngine.getChannel("toAdd").remove(selected);
                                RigidBody tmp = PrebuiltBodies.quad(new Point2D(TowerOfPuzzles.mouseInput.Mouse.x+TowerOfPuzzles.cam.x, TowerOfPuzzles.mouseInput.Mouse.y+TowerOfPuzzles.cam.y), TileConstants.SCALE*newX, TileConstants.SCALE*newY);
                                PhysicsEngine.addToChannel("toAdd", tmp);
                                selected = tmp;
                                return;
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    selected = bod;
                    moving = true;
                    return;
                }
            }
            selected = null;
            if(TowerOfPuzzles.mouseInput.IsRightClick){
                PhysicsEngine.addToChannel("toAdd", PrebuiltBodies.quad(new Point2D(TowerOfPuzzles.mouseInput.Mouse.x+TowerOfPuzzles.cam.x, TowerOfPuzzles.mouseInput.Mouse.y+TowerOfPuzzles.cam.y), TileConstants.SCALE*3, TileConstants.SCALE));
            }
        }
        
    }
    
}
