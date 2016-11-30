package com.stewsters.dusk.core.map.gen

import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.core.map.Tile
import com.stewsters.dusk.core.map.gen.items.FantasyItemGen
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.geom.Rect
import com.stewsters.util.noise.OpenSimplexNoise

class TestMapGenerator implements MapGenerator {

    int playerStartX = 0
    int playerStartY = 0

    @Override
    public LevelMap reGenerate(int level) {
        int width = 100
        int height = 100
        LevelMap map = new LevelMap(width, height)

        map.xSize.times { iX ->
            map.ySize.times { iY ->
                map.ground[iX][iY] = new Tile(TileType.WALL)
            }
        }

        createRoom(map, new Rect(40, 1, 60, 99))

        //todo Add transitions
        map.ground[48][30].tileType = TileType.STAIRS_UP
        map.ground[52][30].tileType = TileType.STAIRS_DOWN

        MapGenUtils.digPool(map, new Rect(35, 41, 40, 46), TileType.WATER_SHALLOW, TileType.WATER_DEEP)

        MapGenUtils.pillarRoom(map, new Rect(30, 48, 40, 64), 3, TileType.WALL, TileType.FLOOR_STONE, TileType.WATER_SHALLOW)

        MapGenUtils.digPool(map, new Rect(35, 66, 40, 70), TileType.LAVA_SHALLOW, TileType.LAVA_DEEP)



        playerStartX = map.xSize / 2 - 1
        playerStartY = map.ySize / 2 - 1

        /**
         * Items
         */
        FantasyItemGen.spawnPerLevel*.name.eachWithIndex { String name, Integer i ->
            FantasyItemGen.createFromName(map, playerStartX - 1, playerStartY + i, name)
        }

        return map
    }

    /**
     * Paint a room onto the map's tiles
     * @return
     */
    public static void createRoom(LevelMap map, Rect room) {

        OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise()

        ((room.x1 + 1)..(room.x2 - 1)).each { int x ->
            ((room.y1 + 1)..(room.y2 - 1)).each { int y ->

                float noise = (openSimplexNoise.eval(x / 10f, y / 10f) + openSimplexNoise.eval(x, y)) / 2f
                TileType tileType
                if (noise < 0.3) {
                    tileType = TileType.GRASS_SHORT
                } else if (noise < 0.6) {
                    tileType = TileType.GRASS_MEDIUM
                } else if (noise < 0.9)
                    tileType = TileType.GRASS_TALL
                else
                    tileType = TileType.GRASS_FOLIAGE

                map.ground[x][y].tileType = tileType
            }
        }

        int x = MatUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
        int y = MatUtils.getIntInRange(room.y1 + 1, room.y2 - 1)

        map.ground[x][y].tileType = TileType.WINDOW
    }


}


