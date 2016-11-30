package com.stewsters.dusk.core.map.gen

import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.core.map.Tile
import com.stewsters.dusk.core.map.gen.items.MonsterGen
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import com.stewsters.util.math.geom.Rect

import static com.stewsters.util.math.MatUtils.d

class SimpleMapGenerator implements MapGenerator {
/* 4*4
        ++++
        +..+
        +..+
        ++++
*/
    //This room includes the walls
    private static int ROOM_MAX_SIZE = 12
    private static int ROOM_MIN_SIZE = 6
    private static int MAX_ROOMS = 20
    private static int MAX_ROOM_MONSTERS = 2
    private static int MAX_ROOM_ITEMS = 2

    int playerStartX = 0
    int playerStartY = 0

    @Override
    LevelMap reGenerate(int level) {

        int width = 60
        int height = 60
        LevelMap map = new LevelMap(width, height)

        map.getXSize().times { iX ->
            map.getYSize().times { iY ->
                map.ground[iX][iY] = new Tile(TileType.WALL)
            }
        }

        List<Rect> rooms = []
        int num_rooms = 0


        MAX_ROOMS.times { int roomNo ->

            int w = MatUtils.getIntInRange(ROOM_MIN_SIZE, ROOM_MAX_SIZE)
            int h = MatUtils.getIntInRange(ROOM_MIN_SIZE, ROOM_MAX_SIZE)

            int roomX = MatUtils.getIntInRange(0, (map.xSize - w) - 1)
            int roomY = MatUtils.getIntInRange(0, (map.ySize - h) - 1)

            Rect newRoom = new Rect(roomX, roomY, w + roomX, h + roomY)
            boolean failed = false
            for (Rect otherRoom : rooms) {
                if (newRoom.intersect(otherRoom))
                    failed = true
                break
            }
            if (!failed) {

                switch (d(10)) {

                    case 1:
                        MapGenUtils.pillarRoom(map, newRoom, d(3) + 1,
                                d(6) == 0 ? TileType.WATER_SHALLOW : TileType.WALL,
                                TileType.FLOOR_STONE, MatUtils.boolean ? TileType.WATER_SHALLOW : null)
                        break

                    case 2:
                        MapGenUtils.digPool(map, newRoom, TileType.WATER_SHALLOW, TileType.WATER_DEEP)
                        break

                    default:
                        createRoom(map, newRoom)
                        break
                }


                Point2i center = newRoom.center()

                if (num_rooms == 0) {
                    //set player start
                    playerStartX = center.x
                    playerStartY = center.y

                } else {
                    MapGenUtils.placeObjects(map, newRoom, level, MAX_ROOM_MONSTERS, MAX_ROOM_ITEMS)

                    Point2i lastCenter = rooms[(num_rooms - 1)].center()
                    Point2i prev = new Point2i(lastCenter.x, lastCenter.y)

                    if (MatUtils.getBoolean()) {
                        createHTunnel(map, prev.x, center.x, prev.y)
                        createVTunnel(map, prev.y, center.y, center.x)
                    } else {
                        createVTunnel(map, prev.y, center.y, prev.x)
                        createHTunnel(map, prev.x, center.x, center.y)
                    }
                }
                rooms.add(newRoom)
                num_rooms++
            } else {
                MessageLogSystem.log("failed to place room " + roomNo)
            }
        }

        Point2i upStair = rooms.last().center()
        map.ground[upStair.x][upStair.y].tileType = TileType.STAIRS_UP

        MonsterGen.generateBossForLevel(map, upStair.x + 1, upStair.y, level)

        if (level > 0) {
            map.ground[playerStartX][playerStartY].tileType = TileType.STAIRS_DOWN
        }
        return map
    }

    /**
     * Paint a room onto the map's tiles
     * @return
     */
    private static void createRoom(LevelMap map, Rect room) {

        ((room.x1 + 1)..(room.x2 - 1)).each { int x ->
            ((room.y1 + 1)..(room.y2 - 1)).each { int y ->
                map.ground[x][y].tileType = TileType.FLOOR_STONE
            }
        }
    }

    private static void createHTunnel(LevelMap map, int x1, int x2, int y) {
        (Math.min(x1, x2)..Math.max(x1, x2)).each { int x ->
            map.ground[x][y].tileType = TileType.FLOOR_STONE
        }
    }

    private static void createVTunnel(LevelMap map, int y1, int y2, int x) {
        (Math.min(y1, y2)..Math.max(y1, y2)).each { int y ->
            map.ground[x][y].tileType = TileType.FLOOR_STONE
        }
    }

}
