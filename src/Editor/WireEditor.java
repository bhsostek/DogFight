/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import Base.Keyboard;
import Base.MousePositionLocator;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Graphics.GUI.UI;
import Entity.Entity;
import Entity.EntityManager;
import Entity.EntityWire;
import Entity.Node;
import Graphics.Sprite;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidBody;
import Physics.RigidUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class WireEditor extends UI{
    
    private int firstIndex = -1;
    private String firstID = "";
    private Node node1 = null;
    private Node node2 = null;
    
    private final int nodeWidth;
    private final int nodeHeight;
    
    private Debouncer escape = new Debouncer(Keyboard.ESC);
    
    private Sprite node = TowerOfPuzzles.spriteBinder.loadSprite("entity/nodes/powered.png");
    
    public WireEditor(int x, int y) {
        super(x, y);
        nodeWidth = node.pWidth*4;
        nodeHeight = node.pHeight*4;
    }

    @Override
    public void tick() {
        if(escape.risingAction(Keyboard.ESC)){
            this.resetNodes();
        }
    }

    @Override
    public void render(Graphics g) {
        
        g.setColor(Color.CYAN);
        if(node1 != null){
            g.drawLine((int)-TowerOfPuzzles.cam.x+node1.getX(), (int)-TowerOfPuzzles.cam.y+node1.getY(), MousePositionLocator.MouseLocation.x, MousePositionLocator.MouseLocation.y);
        }
        

            for(Entity e: EntityManager.getEntities()){
                for(Node n: e.getNodes()){
                    node.render(n.getX()-TowerOfPuzzles.cam.x, n.getY()-TowerOfPuzzles.cam.y, nodeWidth, nodeHeight, g);
                }
            }

       
    }

    @Override
    public void onClick(Rectangle rect) {
        for(Entity e: EntityManager.getEntities()){
            int index = -1;
            for(Node n: e.getNodes()){
                index++;
                RigidBody bod = PrebuiltBodies.quad(new Point2D(n.getX(), n.getY()), nodeWidth, nodeHeight);
                if(RigidUtils.Collides(new Rectangle((int)TowerOfPuzzles.cam.x+MousePositionLocator.MouseLocation.x,(int)TowerOfPuzzles.cam.y+MousePositionLocator.MouseLocation.y,1,1), bod)){
                    if(node1 == null){
                        node1 = n;
                        firstIndex = index;
                        firstID = e.getID();
                        return;
                    }else{
                        node2 = n;
                        EntityWire wire = new EntityWire(firstID, firstIndex, e.getID(), index);
                        TowerOfPuzzles.entityManager.add(wire);
                        resetNodes();
                        return;
                    }
                }
            }
        }
    }
    
    public void resetNodes(){
        this.node1 = null;
        this.node2 = null;
        this.firstIndex = -1;
        this.firstID = "";
    }
    
}
