package com.stewsters.dusk.map.gen

import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.map.gen.items.FantasyItemGen
import com.stewsters.dusk.map.gen.items.MonsterGen
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.geom.Rect


class BaseMapGenerator {


    protected static void placeObjects(LevelMap map, Rect room, int level, int MAX_ROOM_MONSTERS, int MAX_ROOM_ITEMS) {

        int numMonsters = MatUtils.getIntInRange(0, MAX_ROOM_MONSTERS)

        numMonsters.times {
            int x = MatUtils.getIntInRange(room.x1, room.x2)
            int y = MatUtils.getIntInRange(room.y1, room.y2)

            if (!map.isBlocked(x, y)) {
                MonsterGen.getRandomMonsterByLevel(map, x, y, level)
            }
        }

        //now items
        int numItems = MatUtils.getIntInRange(0, MAX_ROOM_ITEMS)
        numItems.times {
            int x = MatUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
            int y = MatUtils.getIntInRange(room.y1 + 1, room.y2 - 1)
            if (!map.isBlocked(x, y)) {

                FantasyItemGen.getRandomItemByLevel(map, x, y, level)
//                FantasyItemGen.getRandomItem(map, x, y)
            }
        }
    }
}
