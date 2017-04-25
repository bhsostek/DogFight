/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World.Tiles;

import Base.TowerOfPuzzles;
import Graphics.Sprite;
import Graphics.SpriteBinder;

/**
 *
 * @author Bailey
 */
public enum EnumTile {
    NULL("null.png"),
    
    WATER_TEMPLE_STONE("water_temple_stone.png"),
    WATER_TEMPLE_STONE_DARK("water_temple_stone_dark.png"),
    WATER_TEMPLE_STONE_EROADED("water_temple_stone_eroaded_1.png"),
    WATER_TEMPLE_STONE_HOLE("water_temple_stone_hole.png"),
    WATER_TEMPLE_STONE_LATTUCE("water_temple_stone_lattuce.png"),
    WATER_TEMPLE_STONE_RUNE("water_temple_stone_rune.png"),
    
    STONE_1("stone_1.png"),
    STONE_2("stone_2.png"),
    STONE_3("stone_3.png"),
    STONE_4("stone_4.png"),
    
    LADDER_LEFT("ladder_left_top.png"),
    LADDER_RIGHT("ladder_right_top.png"),
    LADDER_CENTER("ladder_center_top.png"),
    
    WOOD_WALL_VERTICAL("wood_wall_vertical.png"),
    WOOD_WALL_HORISONTAL("wood_wall_horisontal.png"),
    WOOD_WALL_TEST("wood_test.png"),
    
    MOLDING("molding.png"),
    MOSS_STONE("stone.png"),
    STONE_BLOCK("stone_block.png"),
    STONE_BLUR("stone_blur.png"),
    WOOD_FLOOR("floor.png"),
    
    STAIR_LEFT("stair_left.png"),
    STAIR_RIGHT("stair_right.png"),
    STONE_STAIR_LEFT("stone_stair_left.png"),
    STONE_STAIR_RIGHT("stone_stair_right.png"),
    STAIR_BASE("stair_base.png"),
    STAIR_BASE_CARPET("stair_base_carpet.png"),
    
    //Semi Colon to end the tile enum
    ;
    protected Sprite sprite;
    
   
    EnumTile(String image){
        this.sprite = TowerOfPuzzles.spriteBinder.loadSprite("tiles/"+image);
    }
    
    public Sprite getImage(){
        return this.sprite;
    }
    
    public Tile generate(int x, int y){
        switch(this){
            case LADDER_LEFT:
                return new TileLadderLeft(x,y);
            case LADDER_RIGHT:
                return new TileLadderRight(x,y);
            case LADDER_CENTER:
                return new TileLadder(x,y);
            case STAIR_RIGHT:
                return new TileStairRight(x, y, this);
            case STAIR_LEFT:
                return new TileStairLeft(x,y, this);
            case STONE_STAIR_RIGHT:
                return new TileStairRight(x, y, this);
            case STONE_STAIR_LEFT:
                return new TileStairLeft(x,y, this);
            case WOOD_FLOOR:
                return new TileFloor(x,y,this);
            default:
                return new BasicTile(this,x,y);
        }
    }
    
}
