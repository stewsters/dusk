package com.stewsters.dusk.map.gen

import com.stewsters.dusk.component.Fighter
import com.stewsters.dusk.component.Inventory
import com.stewsters.dusk.component.ai.AdvancedStats
import com.stewsters.dusk.component.ai.BasicOpponent
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.flyweight.TileType
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.map.Tile
import com.stewsters.dusk.map.gen.items.RandomItemGen
import com.stewsters.dusk.sfx.DeathFunctions
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.geom.Rect
import com.stewsters.util.name.NameGen
import com.stewsters.util.noise.OpenSimplexNoise
import squidpony.squidcolor.SColor

class CityMapGenerator implements MapGenerator {

    int playerStartX = 0
    int playerStartY = 0

    public static final int BLOCKSIZE = 25

    @Override
    public LevelMap reGenerate() {

        int width = 200
        int height = 200


        int[][] material = new int[width][height]
        width.times { int iX ->
            height.times { int iY ->
                material[iX][iY] = 5
            }
        }


        playerStartX = width / 2
        playerStartY = height / 2

        List<Intersection> intersections = []
        intersections.add new Intersection(material, playerStartX, playerStartY)

        int maxAttempts = 100
        maxAttempts.times {
            int intersectionX = MatUtils.getIntInRange(0 + (int) (BLOCKSIZE / 2), width - (int) (BLOCKSIZE / 2))
            int intersectionY = MatUtils.getIntInRange(0 + (int) (BLOCKSIZE / 2), height - (int) (BLOCKSIZE / 2))

            //city blocks
            intersectionX -= (intersectionX % BLOCKSIZE)
            intersectionY -= (intersectionY % BLOCKSIZE)

            def collisions = intersections.find { Intersection e ->
                Math.abs(e.centerX - intersectionX) < BLOCKSIZE &&
                        Math.abs(e.centerY - intersectionY) < BLOCKSIZE
            }

            if (!collisions) {
                Intersection intersection = new Intersection(material, intersectionX, intersectionY)

                intersection.linkWithRoads(material, MatUtils.rand(intersections))
                intersections.add(intersection)
            }
        }

//        println intersections

        LevelMap map = convert(material)

        def lots = constructBuildings(map, intersections)
        growTrees(map)
        populate(map, 100)
        infest(map, 50)

        for (Rect lot : lots) {
            addItems(map, lot)
        }
        return map
    }

    Map residential = [
            pattern: [[3, 3, 3, 3, 3, 3, 3],
                      [3, 5, 5, 5, 5, 5, 3],
                      [3, 5, 1, 1, 1, 5, 3],
                      [3, 5, 1, 1, 1, 5, 3],
                      [3, 5, 1, 1, 1, 5, 3],
                      [3, 5, 5, 5, 5, 5, 3],
                      [3, 3, 3, 3, 3, 3, 3]],
            offsetX: 4,
            offsetY: 4
    ]

    def static carveRoad(int[][] material, Map bit, centerX, centerY) {

        //find bit center

        for (int x = 0; x < bit.pattern.size(); x++) {
            for (int y = 0; y < bit.pattern[0].size(); y++) {

                int realX = centerX + x - bit.offsetX
                int realY = centerY + y - bit.offsetY

                int newDepth = bit.pattern[x][y]
                int oldDepth = material[realX][realY]
                material[realX][realY] = Math.min(newDepth, oldDepth)
            }
        }

    }

    private static LevelMap convert(int[][] material) {
        LevelMap map = new LevelMap(material.length, material[0].length);
        map.xSize.times { iX ->
            map.ySize.times { iY ->


                Tile tile
                if (iX == 0 || iY == 0 || iX == map.xSize - 1 || iY == map.ySize - 1) {
                    tile = new Tile(TileType.TREE)
                } else {

                    switch (material[iX][iY]) {
                        case 1: //asphalt road
                            tile = new Tile(TileType.FLOOR_DIRT)
                            break;
                        case 2: //parking lane
                        case 3: //sidewalk
                            tile = new Tile(TileType.FLOOR_STONE)
                            break;
                        case 4: //wall
                            tile = new Tile(TileType.WALL)
                            break;
                        default:// grass
                            tile = new Tile(TileType.GRASS_SHORT)
                            break;

                    }
                }

                map.ground[iX][iY] = tile
            }
        }
        return map
    }

    private static void growTrees(LevelMap map) {
        //Randomly place trees on grass squares
        OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();

        (0..map.xSize - 1).each { int x ->
            (0..map.ySize - 1).each { int y ->

                //if grass
                if (map.ground[x][y].color == SColor.GREEN) {

                    if (openSimplexNoise.eval((double) x / 10.0, (double) y / 10.0) > 0.5) {
                        map.ground[x][y].color = SColor.FOREST_GREEN
                        map.ground[x][y].isBlocked = true
                        map.ground[x][y].opacity = 0.7f
                        map.ground[x][y].representation = '₤' as char
                    }

                }


            }

        }

    }

    /**
     * Fill the map with humans
     * @param map
     * @param maximum
     */
    private static void populate(LevelMap map, int maximum) {
        maximum.times {
            int x = MatUtils.getIntInRange(1, map.xSize - 2)
            int y = MatUtils.getIntInRange(1, map.ySize - 2)

            if (!map.isBlocked(x, y)) {
                int d100 = MatUtils.getIntInRange(0, 100)
                if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'h', name: NameGen.gener(), color: SColor.WHITE_TEA_DYE, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.GOOD,
                            ai: new AdvancedStats(),
                            inventory: new Inventory(),
                            fighter: new Fighter(hp: 4,
                                    defense: 1,
                                    marksman: 1, power: 1,
                                    maxToxicity: 2,
                                    stamina: 4,
                                    deathFunction: DeathFunctions.opponentDeath)


                    )
                } else if (d100 < 95) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'H', name: NameGen.gener(), color: SColor.WHITE_MOUSE, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.GOOD,
                            ai: new AdvancedStats(),
                            inventory: new Inventory(),
                            fighter: new Fighter(hp: 6, defense: 1,
                                    marksman: 1, power: 2,
                                    maxToxicity: 2,
                                    stamina: 6,
                                    deathFunction: DeathFunctions.opponentDeath)

                    )
                } else {

                    new Entity(map: map, x: x, y: y,
                            ch: 'H', name: NameGen.gener(), color: SColor.WHITE, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.GOOD,
                            ai: new AdvancedStats(),
                            inventory: new Inventory(),
                            fighter: new Fighter(hp: 6, defense: 1,
                                    marksman: 3, power: 2,
                                    maxToxicity: 3,
                                    stamina: 6,
                                    deathFunction: DeathFunctions.opponentDeath)


                    )
                }
            }
        }

    }

/**
 * Fill the map with zombies
 * @param map
 * @param maximum
 */
    private static void infest(LevelMap map, int maximum) {
        //fill in zombies
        maximum.times {
            int x = MatUtils.getIntInRange(1, map.xSize - 2)
            int y = MatUtils.getIntInRange(1, map.ySize - 2)

            if (!map.isBlocked(x, y)) {
                int d100 = MatUtils.getIntInRange(0, 100)
                if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'z', name: 'Zombie', color: SColor.SEA_GREEN, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.EVIL,
                            ai: new BasicOpponent(),
                            fighter: new Fighter(hp: 6, defense: 1,
                                    marksman: 0, power: 2,
                                    maxtoxicity: 3,
                                    toxicity: 3,
                                    deathFunction: DeathFunctions.opponentDeath)
                    )
                } else if (d100 < 90) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'Z', name: 'Large Zombie', color: SColor.LAWN_GREEN, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.EVIL,
                            ai: new BasicOpponent(),
                            fighter: new Fighter(hp: 10, defense: 1,
                                    marksman: 0, power: 3,
                                    maxtoxicity: 3,
                                    toxicity: 3,
                                    deathFunction: DeathFunctions.opponentDeath)

                    )
                } else {

                    new Entity(map: map, x: x, y: y,
                            ch: 'f', name: 'Fast Zombie', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                            priority: Priority.OPPONENT, faction: Faction.EVIL,
                            ai: new BasicOpponent(),
                            fighter: new Fighter(hp: 8, defense: 2,
                                    marksman: 0, power: 3,
                                    maxtoxicity: 3,
                                    toxicity: 3,
                                    deathFunction: DeathFunctions.opponentDeath),

                    )
                }
            }
        }
    }

    public static final int MIN_BUILDING_SIZE = 5

    /**
     * We construct a building lot
     * @param map
     * @param intersections
     * @return
     */
    private List<Rect> constructBuildings(LevelMap map, List<Intersection> intersections) {

        List<Rect> lots = []
        for (Intersection inter : intersections) {

            for (int xMod : [-1, 1]) {
                for (int yMod : [-1, 1]) {
                    int offsetX = (inter.centerX + xMod * (residential.offsetX - (xMod > 0 ? 1 : 0)))
                    int offsetY = (inter.centerX + yMod * (residential.offsetY - (yMod > 0 ? 1 : 0)))
                    int size = MIN_BUILDING_SIZE - 1;
                    boolean clear = map.ground[offsetX][offsetY].color == SColor.GREEN

                    while (clear) {

                        if (clear)
                            size++

                        int lowX = Math.min(offsetX, offsetX + (size * xMod))
                        int lowY = Math.min(offsetY, offsetY + (size * yMod))

                        int highX = Math.max(offsetX, offsetX + (size * xMod)) //- (xMod > 0 ? 1 : 0)
                        int highY = Math.max(offsetY, offsetY + (size * yMod)) //- (yMod > 0 ? 1 : 0)

                        for (int x = lowX; x < highX; x++) {
                            for (int y = lowY; y < highY; y++) {
                                if (x < 0 || x >= map.ground.length || y < 0 || y >= map.ground[0].length) {
                                    clear = false
                                } else if (map.ground[x][y].color != SColor.GREEN) {
                                    clear = false
//                                    map.ground[x][y].representation = '^'
                                }
                                if (!clear)
                                    break
                            }
                            if (!clear)
                                break
                        }

                    }
                    size--

                    if (size >= MIN_BUILDING_SIZE) {


                        Rect rect = new Rect(
                                Math.min(offsetX, offsetX + (size * xMod)),
                                Math.min(offsetY, offsetY + (size * yMod)),
                                Math.max(offsetX, offsetX + (size * xMod)),
                                Math.max(offsetY, offsetY + (size * yMod)))
                        lots.add rect
                    }
                }
            }
        }

        //on each lot, construct a house
        lots = lots.unique { Rect a, Rect b ->
            a.intersect(b) ? 0 : 1
        }
        lots.each { Rect lot ->
            CityLotGenerator.generate(map, lot)
        }
        return lots
    }


    private static int MIN_ROOM_ITEMS = 10
    private static int MAX_ROOM_ITEMS = 20 //this can depend on room type
    private static void addItems(LevelMap map, Rect room) {

        try {
            int numItems = MatUtils.getIntInRange(MIN_ROOM_ITEMS, MAX_ROOM_ITEMS)
            numItems.times {

                int x = MatUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
                int y = MatUtils.getIntInRange(room.y1 + 1, room.y2 - 1)
                if (!map.isBlocked(x, y)) {
                    RandomItemGen.placeRandomItem(map, x, y)
                }
            }
        } catch (Exception e) {
            println "d"
        }


    }

    private class Intersection {

        int centerX
        int centerY

        Intersection(int[][] material, x, y) {
            centerX = x
            centerY = y
            carveRoad(material, residential, centerX, centerY)
        }


        void linkWithRoads(int[][] material, Intersection other) {

            if (MatUtils.getBoolean()) {
                createHRoad(material, other.centerX, centerX, other.centerY)
                createVRoad(material, other.centerY, centerY, centerX)
            } else {
                createVRoad(material, other.centerY, centerY, other.centerX)
                createHRoad(material, other.centerX, centerX, centerY)
            }

        }

        private void createHRoad(int[][] material, int x1, int x2, int y) {
            (Math.min(x1, x2)..Math.max(x1, x2)).each { int x ->
                carveRoad(material, residential, x, y)
            }
        }

        private void createVRoad(int[][] material, int y1, int y2, int x) {
            (Math.min(y1, y2)..Math.max(y1, y2)).each { int y ->
                carveRoad(material, residential, x, y)
            }
        }


    }

}
