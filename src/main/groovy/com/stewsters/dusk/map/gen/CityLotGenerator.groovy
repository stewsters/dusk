package com.stewsters.dusk.map.gen

import com.stewsters.dusk.map.LevelMap
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import com.stewsters.util.math.geom.Rect
import com.stewsters.util.math.geom.RectSubdivider
import squidpony.squidcolor.SColor

class CityLotGenerator {

    private static final int MAX_ROOM_WINDOWS = 3
    private static final int MIN_ROOM_WINDOWS = 1

    private static final int MAX_ROOM_DOORS = 3
    private static final int MIN_ROOM_DOORS = 1


    public static void generate(LevelMap map, Rect lot) {

        if (MatUtils.boolean) {
            CityStaticAssets.populate(map, lot)
        } else {
            List<Rect> lots = RectSubdivider.divide(lot, 4)

            lots.each { Rect thisLot ->
                proceduralGen(map, thisLot)
            }

            Point2i center = lot.center()
            int x = MatUtils.limit(center.x + (MatUtils.boolean ? 20 : -20), 1, map.xSize - 1)
            int y = MatUtils.limit(center.y + (MatUtils.boolean ? 20 : -20), 1, map.ySize - 1)

            Rect last = new Rect(x, y, 1, 1)

            lots.each { Rect rect ->

                if (last) {
                    Point2i lastCenter = last.center()
                    int prevX = lastCenter.x
                    int prevY = lastCenter.y

                    Point2i thisCenter = rect.center()
                    int newX = thisCenter.x
                    int newY = thisCenter.y

                    if (MatUtils.getBoolean()) {
                        createHDoorTunnel(map, lot, prevX, newX, prevY)
                        createVDoorTunnel(map, lot, prevY, newY, newX)
                    } else {
                        createVDoorTunnel(map, lot, prevY, newY, prevX)
                        createHDoorTunnel(map, lot, prevX, newX, newY)
                    }
                }
                last = rect
            }

        }

    }


    private static void createHDoorTunnel(LevelMap map, Rect bounding, int x1, int x2, int y) {
        (Math.min(x1, x2)..Math.max(x1, x2)).each { int x ->
            if (bounding.contains(x, y) && map.ground[x][y].isBlocked) {
                map.ground[x][y].isBlocked = false
                map.ground[x][y].representation = '+' as char
                map.ground[x][y].color = SColor.WHITE
                map.ground[x][y].opacity = 0f;
            }
        }
    }

    private static void createVDoorTunnel(LevelMap map, Rect bounding, int y1, int y2, int x) {
        (Math.min(y1, y2)..Math.max(y1, y2)).each { int y ->
            if (bounding.contains(x, y) && map.ground[x][y].isBlocked) {
                map.ground[x][y].isBlocked = false
                map.ground[x][y].representation = '+' as char
                map.ground[x][y].color = SColor.WHITE
                map.ground[x][y].opacity = 0f;
            }
        }
    }


    private static void proceduralGen(LevelMap map, Rect lot) {
        for (int x = lot.x1; x <= lot.x2; x++) {
            for (int y = lot.y1; y <= lot.y2; y++) {
                if (x == lot.x1 || x == lot.x2 || y == lot.y1 || y == lot.y2) {
                    map.ground[x][y].color = SColor.WHITE
                    map.ground[x][y].representation = '#' as char
                    map.ground[x][y].opacity = 1f
                    map.ground[x][y].isBlocked = true
                } else {
                    map.ground[x][y].color = SColor.BROWN
                    map.ground[x][y].representation = '.' as char
                    map.ground[x][y].opacity = 0f
                    map.ground[x][y].isBlocked = false
                }
            }
        }

        MatUtils.getIntInRange(MIN_ROOM_WINDOWS, MAX_ROOM_WINDOWS).times {
            switch (MatUtils.getIntInRange(0, 3)) {
                case (0): //top
                    cutWindowInHorizontalWall(map, lot.x1, lot.x2, lot.y1)
                    break;
                case (1)://right
                    cutWindowInVerticalWall(map, lot.x2, lot.y1, lot.y2)
                    break;
                case (2)://bottom
                    cutWindowInVerticalWall(map, lot.x1, lot.y1, lot.y2)
                    break;
                default://left
                    cutWindowInHorizontalWall(map, lot.x1, lot.x2, lot.y2)
                    break;
            }
        }

//
//        MatUtils.getIntInRange(MIN_ROOM_DOORS, MAX_ROOM_DOORS).times {
//            switch (MatUtils.getIntInRange(0, 3)) {
//                case (0): //top
//                    cutDoorInHorizontalWall(map, lot.x1, lot.x2, lot.y1)
//                    break;
//                case (1)://right
//                    cutDoorInVerticalWall(map, lot.x2, lot.y1, lot.y2)
//                    break;
//                case (2)://bottom
//                    cutDoorInVerticalWall(map, lot.x1, lot.y1, lot.y2)
//                    break;
//                default://left
//                    cutDoorInHorizontalWall(map, lot.x1, lot.x2, lot.y2)
//                    break;
//            }
//        }
    }


    private static void cutWindowInHorizontalWall(LevelMap map, int x1, int x2, int y) {

        if (x2 - 1 >= x1 + 1) {
            int x = MatUtils.getIntInRange(x1 + 1, x2 - 1)
            map.ground[x][y].opacity = 0.25f
            map.ground[x][y].color = SColor.BLUE
            map.ground[x][y].isBlocked = true
            map.ground[x][y].representation = '#'
        }
    }

    private static void cutWindowInVerticalWall(LevelMap map, int x, int y1, int y2) {
        if (y2 - 1 >= y1 + 1) {
            int y = MatUtils.getIntInRange(y1 + 1, y2 - 1)
            map.ground[x][y].opacity = 0.25f
            map.ground[x][y].color = SColor.BLUE
            map.ground[x][y].isBlocked = true
            map.ground[x][y].representation = '#'
        }
    }


    private static void cutDoorInHorizontalWall(LevelMap map, int x1, int x2, int y) {
        if (x2 - 1 >= x1 + 1) {
            int x = MatUtils.getIntInRange(x1 + 1, x2 - 1)
            map.ground[x][y].opacity = 0f
            map.ground[x][y].color = SColor.NEW_BRIDGE
            map.ground[x][y].isBlocked = false
            map.ground[x][y].representation = '+'
        }
    }

    private static void cutDoorInVerticalWall(LevelMap map, int x, int y1, int y2) {
        if (y2 - 1 >= y1 + 1) {

            int y = MatUtils.getIntInRange(y1 + 1, y2 - 1)
            map.ground[x][y].opacity = 0f
            map.ground[x][y].color = SColor.NEW_BRIDGE
            map.ground[x][y].isBlocked = false
            map.ground[x][y].representation = '+'
        }
    }

}
