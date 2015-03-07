package com.stewsters.dusk.map.gen

import com.stewsters.dusk.flyweight.TileType
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.map.Tile
import com.stewsters.dusk.map.gen.items.FantasyItemGen
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import com.stewsters.util.math.geom.Rect
import com.stewsters.util.noise.OpenSimplexNoise

class TestMapGenerator implements MapGenerator {

    int playerStartX = 0
    int playerStartY = 0

    @Override
    public LevelMap reGenerate() {
        int width = 100
        int height = 100
        LevelMap map = new LevelMap(width, height);

        map.xSize.times { iX ->
            map.ySize.times { iY ->
                map.ground[iX][iY] = new Tile(TileType.WALL)
            }
        }

        createRoom(map, new Rect(40, 1, 60, 99))

        //todo Add transitions
        map.ground[48][30].tileType = TileType.STAIRS_UP
        map.ground[52][30].tileType = TileType.STAIRS_DOWN

        digPool(map, new Rect(35, 41, 40, 46), TileType.WATER_SHALLOW, TileType.WATER_DEEP)

        pillarRoom(map, new Rect(30, 48, 40, 64), 3, TileType.WALL, TileType.FLOOR_STONE, TileType.WATER_SHALLOW)

        digPool(map, new Rect(35, 66, 40, 70), TileType.LAVA_SHALLOW, TileType.LAVA_DEEP)



        playerStartX = map.xSize / 2 - 1
        playerStartY = map.ySize / 2 - 1

        /**
         * Items
         */
        FantasyItemGen.spawnChance.keySet().eachWithIndex { String name, int i ->
            FantasyItemGen.createFromName(map, playerStartX - 1, playerStartY + i, name)
        }

        return map
    }

    /**
     * Paint a room onto the map's tiles
     * @return
     */
    private void createRoom(LevelMap map, Rect room) {

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


    private void digPool(LevelMap map, Rect room, TileType shallow, TileType deep) {

        ((room.x1)..(room.x2)).each { int x ->
            ((room.y1)..(room.y2)).each { int y ->
                TileType tileType
                if (x == room.x1 || x == room.x2 || y == room.y1 || y == room.y2) {
                    tileType = shallow
                } else {
                    tileType = deep
                }

                map.ground[x][y].tileType = tileType
            }
        }
    }

    private void pillarRoom(LevelMap map, Rect room, int spacing, TileType column, TileType floor, TileType edge = null) {
        ((room.x1)..(room.x2)).each { int x ->
            ((room.y1)..(room.y2)).each { int y ->

                TileType tileType
                if (edge && (x == room.x1 || x == room.x2 || y == room.y1 || y == room.y2)) {
                    tileType = edge
                } else {
                    Point2i center = room.center()

                    boolean vert
                    boolean hori

                    if (x == center.x || (x % 2 == 1 && x == center.x + 1)) {
                        hori = false
                    } else {
                        if (x < center.x) {
                            hori = (x - room.x1) % spacing == 0
                        } else {
                            hori = (room.x2 - x) % spacing == 0
                        }
                    }

                    if (y == center.y || (y % 2 == 1 && y == center.y + 1)) {
                        vert = false
                    } else {
                        if (y < center.y) {
                            vert = (y - room.y1) % spacing == 0
                        } else {
                            vert = (room.y2 - y) % spacing == 0
                        }
                    }

                    tileType = vert && hori ? column : floor

                }

                map.ground[x][y].tileType = tileType
            }
        }
    }
}


