package com.stewsters.dusk.map.gen

import com.stewsters.dusk.component.Fighter
import com.stewsters.dusk.component.ai.BasicOpponent
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.flyweight.TileType
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.map.Tile
import com.stewsters.dusk.map.gen.items.FantasyItemGen
import com.stewsters.dusk.sfx.DeathFunctions
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import com.stewsters.util.math.geom.Rect
import squidpony.squidcolor.SColor

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
    public LevelMap reGenerate(int level) {

        int width = 60
        int height = 60
        LevelMap map = new LevelMap(width, height);

        map.xSize.times { iX ->
            map.ySize.times { iY ->
                map.ground[iX][iY] = new Tile(TileType.WALL)
            }
        }

        List<Rect> rooms = []
        int num_rooms = 0


        MAX_ROOMS.times { roomNo ->

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

                createRoom(map, newRoom)

                Point2i center = newRoom.center()

                if (num_rooms == 0) {
                    //set player start
                    playerStartX = center.x
                    playerStartY = center.y

                } else {
                    placeObjects(map, newRoom, level)

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
                MessageLog.log("failed to place room " + roomNo)
            }
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

    private static void placeObjects(LevelMap map, Rect room, int level) {

        int numMonsters = MatUtils.getIntInRange(0, MAX_ROOM_MONSTERS)

        numMonsters.times {
            int x = MatUtils.getIntInRange(room.x1, room.x2)
            int y = MatUtils.getIntInRange(room.y1, room.y2)

            if (!map.isBlocked(x, y)) {

                int d100 = MatUtils.getIntInRange(0, 100)
                if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'g', name: 'Goblin', color: SColor.SEA_GREEN, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                            fighter: new Fighter(hp: 4, stamina: 4, melee: 0, evasion: 3,
                                    unarmedDamage: (1..4),
                                    deathFunction: DeathFunctions.opponentDeath)


                    )
                } else if (d100 < 90) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'o', name: 'Orc', color: SColor.LAWN_GREEN, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                            fighter: new Fighter(hp: 10, stamina: 4, melee: 3, evasion: 2,
                                    unarmedDamage: (1..6),
                                    deathFunction: DeathFunctions.opponentDeath)
                    )
                } else {

                    new Entity(map: map, x: x, y: y,
                            ch: 'T', name: 'Troll', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                            fighter: new Fighter(hp: 20, stamina: 8, melee: 4, evasion: -2,
                                    unarmedDamage: (4..8),
                                    deathFunction: DeathFunctions.opponentDeath)
                    )
                }
            }
        }

        //now items
        int numItems = MatUtils.getIntInRange(0, MAX_ROOM_ITEMS)
        numItems.times {
            int x = MatUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
            int y = MatUtils.getIntInRange(room.y1 + 1, room.y2 - 1)
            if (!map.isBlocked(x, y)) {

                FantasyItemGen.getRandomItemByLevel(map, x, y, level)

            }
        }
    }
}
