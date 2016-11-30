package com.stewsters.dusk.core.map.gen

import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.core.map.Tile
import com.stewsters.dusk.core.map.gen.items.MonsterGen
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import com.stewsters.util.math.geom.Rect

class JailMapGenerator implements MapGenerator {

    private static int MAX_ROOM_MONSTERS = 2
    private static int MAX_ROOM_ITEMS = 2

    int playerStartX = 0
    int playerStartY = 0

    @Override
    public LevelMap reGenerate(int level) {

        int width = 40
        int height = 40

        LevelMap map = new LevelMap(width, height)
        map.getXSize().times { iX ->
            map.getYSize().times { iY ->
                map.ground[iX][iY] = new Tile(TileType.WALL)
            }
        }

        List<Rect> rooms = []
        int roomSize = 6

        for (int roomX = 1; roomX < width - roomSize; roomX += roomSize) {
            for (int roomY = 1; roomY < height - roomSize; roomY += roomSize) {

                if (MatUtils.boolean)
                    continue

                Rect newRoom = new Rect(roomX, roomY, roomSize + roomX, roomSize + roomY)
                boolean failed = false
                for (Rect otherRoom : rooms) {
                    if (newRoom.intersect(otherRoom))
                        failed = true
                    break
                }
                if (!failed) {
                    rooms.add(newRoom)
                }
            }
        }

        Collections.shuffle rooms

        rooms.eachWithIndex { newRoom, roomNum ->
            createRoom(map, newRoom)

            Point2i center = newRoom.center()

            if (roomNum == 0) {
                //set player start
                playerStartX = center.x
                playerStartY = center.y

            } else {
                MapGenUtils.placeObjects(map, newRoom, level, MAX_ROOM_MONSTERS, MAX_ROOM_ITEMS)

                Point2i lastCenter = rooms[(roomNum - 1)].center()
                Point2i prev = new Point2i(lastCenter.x, lastCenter.y)

                if (MatUtils.getBoolean()) {
                    createHTunnel(map, prev.x, center.x, prev.y)
                    createVTunnel(map, prev.y, center.y, center.x)
                } else {
                    createVTunnel(map, prev.y, center.y, prev.x)
                    createHTunnel(map, prev.x, center.x, center.y)
                }
            }
        }

        Point2i upStair = rooms.last().center()
        map.ground[upStair.x][upStair.y].tileType = TileType.STAIRS_UP

        MonsterGen.generateBossForLevel(map, upStair.x + 1, upStair.y, level)

        if (level > 0) {
            map.ground[playerStartX][playerStartY].tileType = TileType.STAIRS_DOWN
        }//TODO: starting

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
