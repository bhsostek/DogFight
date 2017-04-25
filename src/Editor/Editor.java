package Editor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Base.Keyboard;
import Base.MouseInput;
import Base.MousePositionLocator;
import Base.MouseWheel;
import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Entity.EntityManager;
import Physics.PhysicsEngine;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

/**
 *
 * @author Bayjose
 */
public class Editor extends Canvas implements Runnable{

    boolean running = false;
    Thread thread;
    
    public static final String NAME = "Editor";
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;
    
    public static boolean SNAP_TO_GRID=false;
    public static boolean FOCUSED_ON_PLAYER = false;
    
    //Managers
    private static TowerOfPuzzles top;
    //Editor
    private Tab[] tabs;
    EntityEditor selector = new EntityEditor(0,48);
    TileChooser tileChooser;
    CollisionEditor collisionTab;
    WireEditor wireEditor;
    SaveButton saveButton;
    LoadButton loadButton;
    SnapToGridButton stgb;
    IncreaseXSize its;
    IncreaseYSize iys;
    
    public static final Rectangle MENU_BAR = new Rectangle(0,0,WIDTH,48);
    
    private MouseWheel mouseWheel;
    
    boolean debounce = true;
    
    private Debouncer focusPlayer = new Debouncer(FOCUSED_ON_PLAYER);
    private boolean movingPlayer = false;
    
    
    public synchronized void start(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    public void run() {
        init();
        long last = System.nanoTime();
        final float ticksPerSecond = 60.0f;
        int frames = 0;
        int ticks = 0;
        long age = System.currentTimeMillis();
        long extra = 0;
        
        while(this.running == true){
            long now = System.nanoTime();
            while((now-last)+extra>=(1000000000.0/ticksPerSecond)){
                ticks++;
                tick();
                if(TowerOfPuzzles.VSynch){
                    render();
                    frames++;
                }
                extra += (now-last);
                extra -=(1000000000.0/ticksPerSecond);
                last = now;
            }

            if(!TowerOfPuzzles.VSynch){
                render();
                frames++;
            }
            

            if(System.currentTimeMillis() - age > 1000){
                age = System.currentTimeMillis();
                System.out.println("Ticks:"+ticks+" Frames:"+frames+" Entity:"+EntityManager.size());
                ticks = 0;
                frames = 0;
            }
        }
    }
    
    public void init(){
        this.createBufferStrategy(2);
        top = new TowerOfPuzzles();
        top.init();
        TowerOfPuzzles.IS_EDITOR = true;
        mouseWheel = new MouseWheel();
        tileChooser = new TileChooser(TowerOfPuzzles.WIDTH - 128,0);
        collisionTab = new CollisionEditor(0,0);
        wireEditor = new WireEditor(0,0);
        
        tabs = new Tab[]{new Tab(0,0, selector, "Entity Editor"),
                         new Tab(128,0, tileChooser, "TileEditor"),
                         new Tab(256,0, collisionTab, "Collisions"),
                         new Tab(256+128,0, wireEditor, "Wire")};
        saveButton = new SaveButton(512,8,tileChooser);
        loadButton = new LoadButton(512+48,8,tileChooser);
        its = new IncreaseXSize(512+48+48,8,tileChooser);
        iys = new IncreaseYSize(512+96+48,8,tileChooser);
        stgb = new SnapToGridButton(512+96+48+48,8);
        
        this.addKeyListener(top.getKeyListeners()[0]);
        this.addMouseListener(top.getMouseListeners()[0]);
        this.addMouseMotionListener(top.getMouseMotionListeners()[0]);
        this.addMouseWheelListener(mouseWheel);
        
        TowerOfPuzzles.player.setCanMove(false);
        TowerOfPuzzles.cam.link(null);
       
    }
    
    
    public void tick(){
        top.tick();
        mouseWheel.tick();
        saveButton.tick();
        loadButton.tick();
        its.tick();
        iys.tick();
        stgb.tick();
        
        FOCUSED_ON_PLAYER = Keyboard.bool_9;
        
        if(focusPlayer.risingAction(FOCUSED_ON_PLAYER)){
            TowerOfPuzzles.player.setCanMove(true);
            TowerOfPuzzles.cam.link(TowerOfPuzzles.player);
            movingPlayer = false;
        }
        
        if(focusPlayer.fallingAction(FOCUSED_ON_PLAYER)){
            TowerOfPuzzles.player.setCanMove(false);
            TowerOfPuzzles.cam.link(null);
        }
        
        if(!FOCUSED_ON_PLAYER){
            if(Keyboard.W){
                TowerOfPuzzles.cam.y-=10;
            }
            if(Keyboard.S){
                TowerOfPuzzles.cam.y+=10;
            }
            if(Keyboard.A){
                TowerOfPuzzles.cam.x-=10;
            }
            if(Keyboard.D){
                TowerOfPuzzles.cam.x+=10;
            }
        }
        
        for(Tab tab : tabs){
            tab.tick();
        }
        
        if(movingPlayer){
            TowerOfPuzzles.player.movePlayerTo(MousePositionLocator.MouseLocation.x+(int)TowerOfPuzzles.cam.x, MousePositionLocator.MouseLocation.y+(int)TowerOfPuzzles.cam.y);
        }
        
        if(TowerOfPuzzles.mouseInput.IsPressed && debounce){
            for(int i = 0; i < tabs.length; i++){
                if(tabs[i].collides(TowerOfPuzzles.mouseInput.Mouse)){
                    tabs[i].setSelected(true);
                    for(int j = 0; j < tabs.length; j++){
                        if(j!=i){
                            tabs[j].setSelected(false);
                        }
                    }
                }
            }
            
            for(int i = 0; i < tabs.length; i++){
                tabs[i].onClick(TowerOfPuzzles.mouseInput.Mouse);
            }
            
            saveButton.onClick(TowerOfPuzzles.mouseInput.Mouse);
            loadButton.onClick(TowerOfPuzzles.mouseInput.Mouse);
            its.onClick(TowerOfPuzzles.mouseInput.Mouse);
            iys.onClick(TowerOfPuzzles.mouseInput.Mouse);
            stgb.onClick(TowerOfPuzzles.mouseInput.Mouse);
            debounce = false;
            
            
            if(PhysicsEngine.pointIntersects(MousePositionLocator.MouseLocation.x+(int)TowerOfPuzzles.cam.x, MousePositionLocator.MouseLocation.y+(int)TowerOfPuzzles.cam.y, "player")){
                movingPlayer = true;
            }else{
                movingPlayer = false;
            }
            
        }
        if(!TowerOfPuzzles.mouseInput.IsPressed){
            debounce = true;
            movingPlayer = false;
        }
        
    }
    
    public void render(){
        BufferStrategy buffer = this.getBufferStrategy();
        Graphics g = buffer.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, TowerOfPuzzles.WIDTH, TowerOfPuzzles.HEIGHT);

        top.renderFromEditor(g);
        for(Tab tab : tabs){
            tab.render(g);
        }
        saveButton.render(g);
        loadButton.render(g);
        its.render(g);
        iys.render(g);
        stgb.render(g);
        
        g.dispose();
        buffer.show();
    }
    
    public static Rectangle getScreen(){
        return new Rectangle(0, 0, TowerOfPuzzles.WIDTH, TowerOfPuzzles.HEIGHT);
    }
    
    
    public static void main(String[] args) {
        EditorWindow window = new EditorWindow(new Editor());
    }
}
