/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.Keyboard;
import Base.TowerOfPuzzles;
import Physics.Point;
import Physics.Point2D;
import Physics.PrebuiltBodies;
import Physics.RigidUtils;
import World.Tiles.TileConstants;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityWire extends Entity{
    Node node1;
    Node node2;
    String id1;
    String id2;
    int numNode1;
    int numNode2;
    
    Point[] pts;
    
    public EntityWire(String id1, int node1, String id2, int node2) {
        super(0, 0, 0, 0, EnumEntityType.WIRE);
        this.id1 = id1;
        this.id2 = id2;
        this.numNode1 = node1;
        this.numNode2 = node2;
        //LEE algorithem, or devise own
    }

    @Override
    public void update() {
        if(node1!=null){
            if(node2!=null){
                node2.setPowered(node1.isPowered());
            }
        }
        if(TowerOfPuzzles.entityManager.getFromID(id1)==null||
           TowerOfPuzzles.entityManager.getFromID(id2)==null){
            TowerOfPuzzles.entityManager.remove(this);
        }
    }

    @Override
    public void onAdd(){
        if(TowerOfPuzzles.entityManager.getFromID(id1) == null |
           TowerOfPuzzles.entityManager.getFromID(id2) == null){
            TowerOfPuzzles.entityManager.remove(this);
            return;
        }
        this.node1 = TowerOfPuzzles.entityManager.getFromID(id1).getNodes()[numNode1];
        this.node2 = TowerOfPuzzles.entityManager.getFromID(id2).getNodes()[numNode2];
        
        addToList(new Point2D(node1.x, node1.y));
//        for(int i = 0; i < (int)(Math.random() * 16); i++){
//            addToList(new Point2D(node1.x+(int)(Math.random()*(node2.x - node1.x)), node1.y+(int)(Math.random()*(node2.y - node1.y))));
//        }
        addToList(new Point2D(node2.x, node2.y));
    }
    
    @Override
    public void render(Graphics g) {
        if(TowerOfPuzzles.IS_EDITOR){
            if(node1.isPowered()){
                g.setColor(Color.green);
            }else{
                g.setColor(Color.red);
            }

            g.drawLine(node1.x+node1.offsetX,node1.y+node1.offsetY,node2.x+node2.offsetX,node2.y+node2.offsetY);
        }
    }
    
    public void addToList(Point p){
        if(pts == null){
            pts = new Point[1];
            pts[0] = p;
        }
        
        Point[] tmpPts = new Point[pts.length+1];
        for(int i = 0; i < pts.length; i++){
            tmpPts[i] = pts[i];
        }
        tmpPts[tmpPts.length-1] = p;
        
        pts = tmpPts;
    }
    
    @Override
    public String save(){
        return this.getType()+"{"+this.id1+","+this.numNode1+","+this.id2+","+this.numNode2+"}";
    }
    
}
