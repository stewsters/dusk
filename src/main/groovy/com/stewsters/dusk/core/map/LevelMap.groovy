package com.stewsters.dusk.core.map

import com.stewsters.dusk.core.component.ai.Ai
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import com.stewsters.util.spatial.IntervalKDTree2d
import groovy.transform.CompileStatic

@CompileStatic
class LevelMap extends BaseLitMap2d {

    public PriorityQueue<Ai> actors // need to cut down this visibility
    private IntervalKDTree2d<Entity> spatialHash
    private HashSet<Entity> entityTemp

    public static final int chunkSize = 60

    // These are chunk offsets in the worldmap
    final int chunkX
    final int chunkY
    final int chunkZ

    LevelMap(int x, int y, int z) {
        super(chunkSize, chunkSize)

        chunkX = x
        chunkY = y
        chunkZ = z

        actors = new PriorityQueue<Ai>(new Comparator<Ai>() {
            @Override
            int compare(Ai o1, Ai o2) {
                return o1.getGameTurn().compareTo(o2.getGameTurn())
            }
        })
        spatialHash = new IntervalKDTree2d<>(Math.max(chunkSize, chunkSize), 4)
        entityTemp = new HashSet<>()
    }

    void add(Entity e) {
        if (e.ai) {
            e.ai.gameTurn = (actors.peek()?.gameTurn ?: 0) + MatUtils.getIntInRange(0, e.ai.speed)
            actors.add(e.ai)
        }

        // TODO: this can cause issues with 2x2 creatures.  I think the xSize and ySize are wrong
        // They look like
        spatialHash.put(e.x - 0.25d, e.y - 0.25d, e.x + e.xSize - 0.75d, e.y + e.ySize - 0.75d, e)
    }

    void remove(Entity e) {
        if (e.ai)
            actors.remove(e.ai)
        spatialHash.remove(e)
    }


    void update(Entity e) {
        spatialHash.put(e.x - 0.25d, e.y - 0.25d, e.x + e.xSize - 0.75d, e.y + e.ySize - 0.75d, e)
    }

    HashSet<Entity> getEntitiesAtLocation(int x, int y) {
        entityTemp.clear()
        return spatialHash.getValues(x - 0.5d, y - 0.5d, x + 0.5d, y + 0.5d, entityTemp)
    }

    HashSet<Entity> getEntitiesBetween(int lowX, int lowY, int highX, int highY) {
        entityTemp.clear()
        return spatialHash.getValues(lowX - 0.5d, lowY - 0.5d, highX + 0.5d, highY + 0.5d, entityTemp)
    }


    boolean isBlocked(int x, int y, Entity ignore = null) {

        if (super.isBlocked(x, y))
            return true
        for (Entity entity : getEntitiesAtLocation(x, y)) {
            if (entity != ignore && entity.blocks)
                return true
        }

        return false
    }

    boolean isBlocked(final int x, final int y, final int xSize, final int ySize, Entity ignore = null) {
        for (int xMod = 0; xMod < xSize; xMod++) {
            for (int yMod = 0; yMod < ySize; yMod++) {
                if (super.isBlocked(x + xMod, y + yMod))
                    return true

                for (Entity entity : getEntitiesAtLocation(x + xMod, y + yMod)) {
                    if (entity != ignore && entity.blocks)
                        return true
                }
            }
        }

        return false
    }

    // This is used to find up and down stairs
    Point2i findATile(TileType tileType) {
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                if (ground[x][y].tileType == tileType) {
                    return new Point2i(x, y)
                }
            }
        }
        return null
    }

    void setExplored(int x, int y, boolean isExplored) {
        ground[x][y].isExplored = isExplored
    }

    TileType getTileType(int x, int y) {
        return ground[x][y].tileType
    }
}
