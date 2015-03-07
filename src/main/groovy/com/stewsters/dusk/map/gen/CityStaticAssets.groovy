package com.stewsters.dusk.map.gen

import com.stewsters.dusk.flyweight.TileType
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.map.Tile
import com.stewsters.util.math.geom.Rect

public class CityStaticAssets {

    public static boolean populate(LevelMap map, Rect rect) {

        int rectWidth = rect.x2 - rect.x1;
        int rectHeight = rect.y2 - rect.y1;

        def lots = StaticLots.manifest.findAll {
            it.x <= rectWidth && it.y <= rectHeight
        }

        Collections.shuffle(lots)
        def lot = lots[0]

        if (lot) {
            for (int x = 0; x < lot.x; x++) {
                for (int y = 0; y < lot.y; y++) {
                    char c = lot.map[y].charAt(x);
                    map.ground[rect.x1 + x][rect.y1 + y] = buildCell(c);
                }
            }
            return true;
        }
        return false;
    }

    private static Tile buildCell(char c) {

        TileType tileType

        switch (c) {
            case '.'://grass
                tileType = TileType.GRASS_SHORT;
                break;
            case '`'://parking lane
                tileType = TileType.FLOOR_STONE;
                break;
            case ','://pavement
                tileType = TileType.FLOOR_STONE;
                break;
            case '_':
                tileType = TileType.FLOOR_DIRT;
                break
            case '=':
                tileType = TileType.WINDOW;
                break
            case '~':
                tileType = TileType.WATER_SHALLOW
                break;
            case '+'://door
                tileType = TileType.DOOR_CLOSED
                break;
            case '₤':
                tileType = TileType.TREE
                break;
            case 'X': //CHEST high wall
                tileType = TileType.WALL
                break;
            case '#': //wall
                tileType = TileType.WALL
                break;
            case '/':
                tileType = TileType.DOOR_OPEN
                break;
            default://opaque items
                tileType = TileType.GRASS_SHORT
        }
        return new Tile(tileType);
    }
}
