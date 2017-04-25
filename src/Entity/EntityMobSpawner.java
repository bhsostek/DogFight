/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Base.TowerOfPuzzles;
import Base.util.Debouncer;
import Base.util.DynamicCollection;
import Editor.Attribute;
import Entity.Mob.Mob;
import World.Tiles.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class EntityMobSpawner extends Entity{

    private Attribute<String> prefab = new Attribute<String>("Monster:","crab");
    private Attribute<Integer> spawnCount = new Attribute<Integer>("Number to Spawn:", 1);
    private Debouncer powered = new Debouncer(false);
    
    private DynamicCollection<Mob> spawnedMobs = new DynamicCollection<Mob>();
    
    private EntityParticleEmitter epe;
    
    public EntityMobSpawner(int x, int y, String prefab, int spawnCount) {
        super(x, y, TileConstants.SCALE, TileConstants.SCALE, EnumEntityType.MOB_SPAWNER);
        
        epe  = new EntityParticleEmitter(x, y, "mob_spawner.txt");
        epe.link(this);
        epe.setCanAdd(false);
        
        super.setAlwaysRender(true);
        
        this.prefab.setData(prefab);
        this.spawnCount.setData(spawnCount);
        
        Node[] nodes = new Node[]{
            new Node(0,0),
            new Node(TileConstants.SCALE,0),
        };
        
        super.addAttribute(this.prefab);
        super.addAttribute(this.spawnCount);
        
        super.setNodes(nodes);
        
    }

    @Override
    public void update() {
        for(Mob e : spawnedMobs.getCollection(Mob.class)){
            loop:{
                if(e==null){
                    spawnedMobs.remove(e);
                    break loop;
                }
                if(TowerOfPuzzles.entityManager.getFromID(e.getID())==null){
                    spawnedMobs.remove(e);
                    break loop;
                }
            }
        }
        spawnedMobs.tick();
        
        if(this.spawnedMobs.getLength()>0){
            this.getNodes()[1].setPowered(true);
        }else{
            this.getNodes()[1].setPowered(false);
        }
        
        if(powered.risingAction(super.getNodes()[0].isPowered())){
            epe.setCanAdd(true);
            epe.regenerateSystem();
            epe.update();
            epe.setCanAdd(false);
            for(int i = 0; i < this.spawnCount.getData(); i++){
                Mob toSpawn = new Mob((int)super.x, (int)super.y, this.prefab.getData());
                toSpawn.setID("mob:"+Math.random());
                toSpawn.setShouldSave(false);
                TowerOfPuzzles.entityManager.add(toSpawn);
                this.spawnedMobs.add(toSpawn);
            }
        }
        
        epe.tick();
    }
    
    @Override
    public void render(Graphics g) {
        epe.render(g);
    }
    
    @Override
    public String save() {
        return this.getType()+"{"+this.x+","+this.y+","+this.getID()+","+this.prefab.getData()+","+this.spawnCount.getData()+"}";
    }

    
}
