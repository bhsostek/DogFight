package Base;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Base.Controller.ControllerManager;
import Base.util.StringUtils;
import Camera.Camera;
import Entity.Animation.Animation;
import Entity.EntityLight;
import Entity.EntityManager;
import Entity.Mob.AiComponets.ControllerComponent;
import Entity.Mob.AttackManager;
import Entity.Mob.Mob;
import Entity.Player;
import Graphics.FadeOutManager;
import Graphics.Font.TextRenderer;
import Graphics.GUI.Chat;
import Graphics.GUI.HUD;
import Graphics.GUI.UI;
import Graphics.SpriteBinder;
import Lighting.LightingEngine;
import Physics.PhysicsEngine;
import Quest.Chat.Message;
import ScriptingEngine.ScriptingEngine;
import Sound.Music;
import Sound.SoundManager;
import World.Room;
import World.WorldManager;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.io.File;
import World.Tiles.TileConstants;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.LinkedList;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author Bayjose
 */
public class TowerOfPuzzles extends Canvas implements Runnable{

    public static String Path = "";
    public static final String RoomPath = "/Rooms/";
    public static final String SoundPath = "/Sound/";
    public static final String ImagePath = "/Images/"; 
    public static final String ScriptPath = "/Scripts/";
    public static final String SavePath = "/Saves/"; 
    public static final String PrefabPath = "/Prefab/"; 
    
    private static Class refrence = TowerOfPuzzles.class;
    public static boolean DEBUG = false;
    
    boolean running = false;
    Thread thread;
    
    public static final String NAME = "The Tower Of Puzzles";
    public static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int HEIGHT =  Toolkit.getDefaultToolkit().getScreenSize().height;
    
    public static float renderRate = 144.0f - 60.0f;
    public static final boolean VSynch = true;
    
    public static final float GRAVITY = 24.0f/60.0f;
    public static final float FRICTION = TileConstants.SCALE/60.0f;
    
    //Managers
    public static SoundManager soundManager;
    private static PhysicsEngine pe;
    public static WorldManager worldManager;
    public static LightingEngine lightingEngine;
    public static EntityManager entityManager;
    public static FadeOutManager fadeManager;
    private static ScriptingEngine scriptingEngine;
    public static Player player;
    public static SpriteBinder spriteBinder;
    public static TextRenderer textRenderer;
    public static AttackManager attackManager;
    public static MouseInput mouseInput;
    public static ControllerManager controllerManager;
    
    //Engines
    private LinkedList<Engine> engines = new LinkedList<Engine>();
    
    private static HUD hud;
    
    public static Keyboard KEYBOARD;
    
    public static boolean IS_EDITOR = false;
    
    public static Camera cam = new Camera(0,0,0);
    
    public static Chat chat;
    
    public Animation test ;
//    public Light light = new Light(0,0,256, PixelUtils.makeColor(new int[]{0, 0, 255, 128}));

    boolean debounce = true;

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
        
        while(this.running){
            long now = System.nanoTime();
            
            while((now-last)+extra>=(1000000000.0/ticksPerSecond)){
                if(ticks<ticksPerSecond){
                    ticks++;
                    tick();
                    if(VSynch){
                        render();
                        frames++;
                    }
                }
                extra += (now-last);
                extra -=(1000000000.0/ticksPerSecond);

                last = now;
            }
            
            if(!VSynch){
                render();
                frames++;
            }
            
            
            if(System.currentTimeMillis() - age > 1000){
                age = System.currentTimeMillis();
//                if(debug){
                    System.err.println("Ticks:"+ticks+" Frames:"+frames+" Entity:"+EntityManager.size());
//                }
                ticks = 0;
                frames = 0;
            }
        }
    }
    
    public void saveAll(){
//        
//        System.err.println("Saving Now");
//        SaveManager.saveInventory();
//        SaveManager.saveDynamicEntities();
//        System.err.println("Saving Complete!");
    }
    
    public void init(){
         System.err.println("---------------- Bayjose 2D Engine ----------------");
        if(this.running){
            this.createBufferStrategy(2);
        }
        
        //set reference
        TowerOfPuzzles.Path = StringUtils.removeEXEandJAR(StringUtils.getRelativePath(refrence));

        //input
        
        //Sprite Binder
        spriteBinder = new SpriteBinder();
        engines.add(spriteBinder);
        
        //engines
        pe = new PhysicsEngine();
        engines.add(pe);
        soundManager = new SoundManager();
        engines.add(soundManager);
        worldManager = new WorldManager();
        engines.add(worldManager);
        lightingEngine = new LightingEngine();
        engines.add(lightingEngine);
        entityManager = new EntityManager();
        engines.add(entityManager);
        fadeManager = new FadeOutManager();
        textRenderer = new TextRenderer("main");
        System.err.println("Initializing:Chat");
        chat = new Chat(WIDTH/2, HEIGHT/2);  
        chat.addMessage(new Message().setName("NPC").setNameColor(Color.CYAN ).setMessage(new String[]{"Line1","Line2","Line3","Line4","Line5","Line6","Line7","Line8"}));
        System.err.println("Initializing:AttackManager");
        attackManager = new AttackManager();
        System.err.println("Initializing:HUD");
        hud = new HUD();
        

        
        System.err.println("---------------- Listeners ----------------");
        System.err.println("Adding:MouseListener");
        mouseInput = new MouseInput();
        this.addMouseListener(mouseInput);
        engines.add(mouseInput);
        KEYBOARD = new Keyboard();
        System.err.println("Adding:KeyListener");
        this.addKeyListener(KEYBOARD);
        System.err.println("Adding:MouseMotionListener");
        this.addMouseMotionListener(new MousePositionLocator());
        controllerManager = new ControllerManager();
        engines.add(controllerManager);
        
        //player
        System.err.println("Initializing:Player");
        player = new Player(0, 0, controllerManager.getContoller(0));
        entityManager.add(player);
        Mob mob = new Mob(0,0,"player.txt");
        mob.addComponent(new ControllerComponent(mob, 1));
        entityManager.add(mob);
//        EntityLight light = new EntityLight(0,0,300, 255, 255, 255);
//        light.link(mob);
//        entityManager.add(light);
        
        
        System.err.println("---------------- Scripting ----------------");
        //LAST after all engines have been initialized
        System.err.println("Initializing:ScriptingEngine");
        scriptingEngine = new ScriptingEngine();
        engines.stream().forEach((e) -> {
            e.registerForScripting(scriptingEngine.getEngine());
        });
        

        System.err.println("---------------- Paths & Folders ----------------");
        System.err.println("Game.path ="+TowerOfPuzzles.Path);

        File imagesFolder = new File(TowerOfPuzzles.Path+TowerOfPuzzles.ImagePath);
        System.err.println("Looking for Image Folder:"+TowerOfPuzzles.Path+TowerOfPuzzles.ImagePath);
        
        if(!imagesFolder.exists()){
            boolean result = false;
                System.err.println("Cannot find Image folder... Creating:"+TowerOfPuzzles.Path+TowerOfPuzzles.ImagePath);
            try{
                imagesFolder.mkdir();
                result = true;
            } 
            catch(SecurityException se){
                //handle it
                System.err.println("Failure...");
                System.err.println("File Permissions do not allow the directory:"+TowerOfPuzzles.Path+TowerOfPuzzles.ImagePath+" to be created."); 
            }        
            if(result) {    
                System.err.println("Success...");  
            }
        }

        File soundsFolder = new File(TowerOfPuzzles.Path+TowerOfPuzzles.SoundPath);
        System.err.println("Looking for Sounds Folder:"+TowerOfPuzzles.Path+TowerOfPuzzles.SoundPath);
        
        if(!soundsFolder.exists()){
            boolean result = false;
                System.err.println("Cannot find sounds folder... Creating:"+TowerOfPuzzles.Path+TowerOfPuzzles.SoundPath);
            try{
                soundsFolder.mkdir();
                result = true;
            } 
            catch(SecurityException se){
                //handle it
                System.err.println("Failure...");
                System.err.println("File Permissions do not allow the directory:"+TowerOfPuzzles.Path+TowerOfPuzzles.SoundPath+" to be created."); 
            }        
            if(result) {    
                System.err.println("Success...");  
            }
        }
        
        File roomsFolder = new File(TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath);
        System.err.println("Looking for Rooms Folder:"+TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath);
        
        if(!roomsFolder.exists()){
            boolean result = false;
                System.err.println("Cannot find rooms folder... Creating:"+TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath);
            try{
                roomsFolder.mkdir();
                result = true;
            } 
            catch(SecurityException se){
                //handle it
                System.err.println("Failure...");
                System.err.println("File Permissions do not allow the directory:"+TowerOfPuzzles.Path+TowerOfPuzzles.RoomPath+" to be created."); 
            }        
            if(result) {    
                System.err.println("Success...");  
            }
        }
        
        File scriptsFolder = new File(TowerOfPuzzles.Path+TowerOfPuzzles.ScriptPath);
        System.err.println("Looking for Scripts Folder:"+TowerOfPuzzles.Path+TowerOfPuzzles.ScriptPath);
        
        if(!scriptsFolder.exists()){
            boolean result = false;
                System.err.println("Cannot find Scripts folder... Creating:"+TowerOfPuzzles.Path+TowerOfPuzzles.ScriptPath);
            try{
                scriptsFolder.mkdir();
                result = true;
            } 
            catch(SecurityException se){
                //handle it
                System.err.println("Failure...");
                System.err.println("File Permissions do not allow the directory:"+TowerOfPuzzles.Path+TowerOfPuzzles.ScriptPath+" to be created."); 
            }        
            if(result) {    
                System.err.println("Success...");  
            }
        }
        File savesFolder = new File(TowerOfPuzzles.Path+TowerOfPuzzles.SavePath);
        System.err.println("Looking for Saves Folder:"+TowerOfPuzzles.Path+TowerOfPuzzles.SavePath);
        
        if(!savesFolder.exists()){
            boolean result = false;
                System.err.println("Cannot find Saves folder... Creating:"+TowerOfPuzzles.Path+TowerOfPuzzles.SavePath);
            try{
                savesFolder.mkdir();
                result = true;
            } 
            catch(SecurityException se){
                //handle it
                System.err.println("Failure...");
                 System.err.println("File Permissions do not allow the directory:"+TowerOfPuzzles.Path+TowerOfPuzzles.SavePath+" to be created."); 
            }        
            if(result) {    
                System.err.println("Success...");  
            }
        }
        File prefabFolder = new File(TowerOfPuzzles.Path+TowerOfPuzzles.PrefabPath);
        System.err.println("Looking for Prefab Folder:"+TowerOfPuzzles.Path+TowerOfPuzzles.PrefabPath);
        
        if(!prefabFolder.exists()){
            boolean result = false;
                System.err.println("Cannot find Prefab folder... Creating:"+TowerOfPuzzles.Path+TowerOfPuzzles.PrefabPath);
            try{
                prefabFolder.mkdir();
                result = true;
            } 
            catch(SecurityException se){
                //handle it
                System.err.println("Failure...");
                 System.err.println("File Permissions do not allow the directory:"+TowerOfPuzzles.Path+TowerOfPuzzles.PrefabPath+" to be created."); 
            }        
            if(result) {    
                System.err.println("Success...");  
            }
        }
        System.err.println("---------------- End ----------------");
        postInit();
    }
    
    private void postInit(){
//        WorldManager.addRoom(new Room(0,0,"rift"));
        test = new Animation("car");
        cam.link(player);
    }
    
    public void tick(){
        controllerManager.tick();
        soundManager.tick();
        worldManager.tick();
        entityManager.tick();
        cam.tick();
        pe.tick();
        scriptingEngine.tick();
        fadeManager.tick();
        chat.tick();
        attackManager.tick();
        hud.tick();
        
        test.tick();
        
        lightingEngine.tick();
        if(mouseInput.IsPressed && debounce){
            hud.onClick(mouseInput.Mouse);
//            chat.addMessage(new Message().setName("Eat a butt").setNameColor(Color.GREEN ).setMessage(new String[]{"Line1","Line2","Line3"}));
            debounce = false;
        }
        if(!mouseInput.IsPressed){
            debounce = true;
        }
    }
    
    private void render(){
        BufferStrategy buffer = this.getBufferStrategy();
        Graphics g = buffer.getDrawGraphics();
        g.setColor(Color.decode("#05435c"));
        g.fillRect(0, 0, TowerOfPuzzles.WIDTH, TowerOfPuzzles.HEIGHT);

        Graphics2D g2D = (Graphics2D)g;
            
        //dependant of camera, always translate by cam, player entity world
        g.translate(-(int)cam.x, -(int)cam.y);
            g2D.rotate(Math.toRadians(cam.rot), player.getX(), player.getY());
                worldManager.render(g);
                lightingEngine.resetMoved();
                scriptingEngine.render(g);
                pe.Render(g);
                entityManager.render(g);
                attackManager.render(g);
                entityManager.renderWater(g);
                test.render(g);
            g2D.rotate(Math.toRadians(-cam.rot), player.getX(), player.getY());
        g.translate((int)cam.x, (int)cam.y);

        
//        test.render(g);
        
        hud.render(g);
        controllerManager.render(g);
        
        chat.render(g);
        
        fadeManager.render(g);
        
        
//                Graphics2D g2d = (Graphics2D) g;
//        Point2D center = new Point2D.Float(TowerOfPuzzles.WIDTH/2,TowerOfPuzzles.HEIGHT/2);
////        Point2D center2 = new Point2D.Float(WIDTH/2,HEIGHT/2);
//        float[] dist = new float[]{0.0f, 1.0f};
//        Color[] colors = new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.BLACK};
//        
//        RadialGradientPaint p = new RadialGradientPaint(center, 500, dist, colors);
////        RadialGradientPaint p2 = new RadialGradientPaint(center2, 800, dist, colors);
//        
//        
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.95f));
//        g2d.setPaint(p);
//        g2d.fillRect(0, 0, TowerOfPuzzles.WIDTH, TowerOfPuzzles.HEIGHT);
        
        g.dispose();
        buffer.show();
    }
    
    public void renderFromEditor(Graphics g){
        g.setColor(Color.decode("#05435c"));
        g.fillRect(0, 0, TowerOfPuzzles.WIDTH, TowerOfPuzzles.HEIGHT);

        Graphics2D g2D = (Graphics2D)g;
            
        //dependant of camera, always translate by cam, player entity world
        
        g.translate(-(int)cam.x, -(int)cam.y);
            g2D.rotate(Math.toRadians(cam.rot), player.getX(), player.getY());
                worldManager.render(g);
                lightingEngine.resetMoved();
                scriptingEngine.render(g);
                pe.Render(g);
                entityManager.render(g);
                player.render(g);
                attackManager.render(g);
                entityManager.renderWater(g);
                test.render(g);
            g2D.rotate(Math.toRadians(-cam.rot), player.getX(), player.getY());
        g.translate((int)cam.x, (int)cam.y);

//        test.render(g);
        
        
        hud.render(g);
        controllerManager.render(g);
        
        chat.render(g);
        
        fadeManager.render(g);

        
    }
    
    public static Rectangle getScreen(){
        return new Rectangle(0, 0, TowerOfPuzzles.WIDTH, TowerOfPuzzles.HEIGHT);
    }
    
    public static Class getRefrence(){
        return refrence;
    }
    
    public static void openUI(UI ui){
        hud.Open(ui);
    }
    
    public static void closeUI(){
        hud.Close();
    }
    
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl","True");
        Base.Window window = new Base.Window(new TowerOfPuzzles());
//        System.out.println(System.getProperty("java.library.path"));

    }
}
