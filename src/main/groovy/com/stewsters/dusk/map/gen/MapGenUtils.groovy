package com.stewsters.dusk.map.gen

import com.stewsters.dusk.flyweight.TileType
import com.stewsters.dusk.map.LevelMap
import com.stewsters.util.math.Point2i
import com.stewsters.util.math.geom.Rect

/**
 * Created by stewsters on 2/18/16.
 */
class MapGenUtils {

    public static void digPool(LevelMap map, Rect room, TileType shallow, TileType deep) {

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

    public static void pillarRoom(LevelMap map, Rect room, int spacing, TileType column, TileType floor, TileType edge = null) {
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
