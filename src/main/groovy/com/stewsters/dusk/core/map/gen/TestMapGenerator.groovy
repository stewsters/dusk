package com.stewsters.dusk.core.map.gen

import com.stewsters.dusk.core.component.CoreStats
import com.stewsters.dusk.core.component.Fighter
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.core.map.Tile
import com.stewsters.dusk.core.map.gen.items.FantasyItemGen
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.geom.Rect
import com.stewsters.util.noise.OpenSimplexNoise
import squidpony.squidcolor.SColor

class TestMapGenerator implements MapGenerator {

    int playerStartX = 0
    int playerStartY = 0

    @Override
    LevelMap reGenerate(int x, int y, int level) {

        LevelMap map = new LevelMap(x, y, level)

        map.xSize.times { iX ->
            map.ySize.times { iY ->
                map.ground[iX][iY] = new Tile(TileType.WALL)
            }
        }

        createRoom(map, new Rect(20, 1, 40, 50))

        //todo Add transitions
        map.ground[31][33].tileType = TileType.STAIRS_UP
        map.ground[32][33].tileType = TileType.STAIRS_DOWN

        MapGenUtils.digPool(map, new Rect(1, 20, 20, 30), TileType.WATER_SHALLOW, TileType.WATER_DEEP)

        MapGenUtils.pillarRoom(map, new Rect(1, 32, 20, 42), 3, TileType.WALL, TileType.FLOOR_STONE, TileType.WATER_SHALLOW)

        MapGenUtils.digPool(map, new Rect(1, 44, 20, 54), TileType.LAVA_SHALLOW, TileType.LAVA_DEEP)

        playerStartX = map.xSize / 2 - 1
        playerStartY = map.ySize / 2 - 1

        /**
         * Items
         */
        FantasyItemGen.spawnPerLevel*.value.flatten()*.name.eachWithIndex { String name, Integer i ->
            FantasyItemGen.createFromName(map, playerStartX - 1, 2 + i, name)
        }

        new Entity(
                map: map,
                x: playerStartX + 1,
                y: playerStartY + 1,
                name: "Practice Dummy",

                ch: 'd',
                color: SColor.WHITE,

                blocks: true,
                fighter: new Fighter(hp: 100),
                coreStats: new CoreStats(strength: 10, life: 10, stamina: 10)
        )

        return map
    }

    /**
     * Paint a room onto the map's tiles
     * @return
     */
    static void createRoom(LevelMap map, Rect room) {

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
