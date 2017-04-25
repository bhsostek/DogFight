/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Mob.Mob;
import World.Tiles.TileConstants;

/**
 *
 * @author Bailey
 */
public enum EnumEntityType {
    TORCH(),
    DOOR(),
    FLOOR_LAMP(),
    FLOOR_LANTERN(),
    GRATE(),
    WIRE(),
    WATER(),
    WATER_CELLS(),
    LOG(),
    PEDESTAL(),
    TIMER(),
    RS_LATCH(),
    NOT_GATE(),
    OR_GATE(),
    AND_GATE(),
    BUFFER(),
    SPAWN_POINT(),
    GATE(),
    LIGHT(),
    TELEPORTER(),
    SPAWN_ROOM(),
    PARTICLE_EMITTER(),
    PLAYER(),
    BOOK_SHELF(),
    BOOK(),
    WATER_DROP(),
    BLACK_HOLE(),
    ELEVATOR(),
    BARREL(),
    //TO ADD
    WARP(),
    BED(),
//    ROPE(),
//    CHAIN(),
//    DYNAMITE(),
//    SECRET_FROG(),
    WINDOW(),
//    T_FLIP_FLOP(),
    CHEST(),
    POSITIONAL_AUDIO(),
    TREE(),
    SCRIPT(),
    PEDESTAL_ITEM(),
    
    MONSTER(),
    DETECTOR(),
    MOB_SPAWNER(),
    
    TEXT(),
    BULLET(),
    ;
    
    Entity generate(String in, String extraData){
        String[] data = in.split(",");
        switch(this){
            case TORCH:
                return new EntityTorch((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case PEDESTAL:
                return new EntityPedestalButton((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case TIMER:
                return new EntityTimer((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2])).setID(data[3]);
            case DOOR:
                return new EntityDoor((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), Boolean.parseBoolean(data[2])).setID(data[3]);
            case BOOK_SHELF:
                return new EntityBookShelf((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),Boolean.parseBoolean(data[2]));
            case FLOOR_LAMP:
                return new EntityFloorLamp((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2]),(int)Float.parseFloat(data[3]),(int)Float.parseFloat(data[4])).setID(data[5]);
            case FLOOR_LANTERN:
                return new EntityFloorLantern((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2]),(int)Float.parseFloat(data[3]),(int)Float.parseFloat(data[4])).setID(data[5]);
            case GRATE:
                return new EntityGrate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case WIRE:
                return new EntityWire(data[0],(int)Float.parseFloat(data[1]),data[2],(int)Float.parseFloat(data[3]));
            case WATER:
                return new EntityWater((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2]),(int)Float.parseFloat(data[3]));
            case LOG:
                return new EntityLog((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2]));
            case SPAWN_POINT:
                return new EntitySpawnPlayer((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case RS_LATCH:
                return new EntityRSLatch((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case NOT_GATE:
                return new EntityNotGate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case OR_GATE:
                return new EntityORGate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case AND_GATE:
                return new EntityANDGate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case GATE:
                return new EntityGate((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case LIGHT:
                return new EntityLight((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2]),(int)Float.parseFloat(data[3]),(int)Float.parseFloat(data[4]),(int)Float.parseFloat(data[5])).setID(data[6]);
            case ELEVATOR:
                return new EntityElevator((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case TELEPORTER:
                return new EntityTeleportPlayer((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1])).setID(data[2]);
            case BUFFER:
                return new EntityBuffer((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2])).setID(data[3]);
            case SPAWN_ROOM:
                return new EntitySpawnRoom((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),data[2]).setID(data[3]);
            case WINDOW:
                return new EntityWindow((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]));
            case CHEST:
                return new EntityChest((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),Boolean.parseBoolean(data[2]), extraData);
            case BARREL:
                return new EntityBarrel((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), extraData);
            case WARP:
                return new EntityWarp((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2]),(int)Float.parseFloat(data[3]),data[4], Boolean.parseBoolean(data[5]),(int)Float.parseFloat(data[6]),(int)Float.parseFloat(data[7]),Boolean.parseBoolean(data[8]), data[9]);
            case PARTICLE_EMITTER:
                EntityParticleEmitter pe;
                pe = new EntityParticleEmitter((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),data[2]);
                return pe;
            case BED:
                return new EntityBed((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]));
            case BLACK_HOLE:
                return new EntityBlackHole((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2])).setID(data[3]);
            case TREE:
                return new EntityTree((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]),(int)Float.parseFloat(data[2]));
            case SCRIPT:
                return new EntityScript(data[0]);
            case PEDESTAL_ITEM:
                return new EntityItemPedestal((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), extraData).setID(data[2]);
            case MONSTER:
                return new Mob((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), data[2]);
            case POSITIONAL_AUDIO:
                return new EntityPositionalAudio((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), data[2], Float.parseFloat(data[3]), Boolean.parseBoolean(data[4]));
            case MOB_SPAWNER:
                return new EntityMobSpawner((int)Float.parseFloat(data[0]),(int)Float.parseFloat(data[1]), data[3], (int)Float.parseFloat(data[4])).setID(data[2]);
        }
        
        System.err.println("Entity:"+this.name()+" not recognised.");
        
        return null;
    }
}
