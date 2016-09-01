package com.stewsters.dusk.core.map

import com.stewsters.dusk.core.component.ai.Ai
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.util.math.Point2i
import com.stewsters.util.spatial.IntervalKDTree2d

public class LevelMap extends BaseLitMap2d {

    public PriorityQueue<Ai> actors // need to cut down this visibility
    private IntervalKDTree2d<Entity> spatialHash
    private HashSet<Entity> entityTemp;

    public LevelMap(int x, int y) {
        super(x, y)

        actors = new PriorityQueue<Ai>(new Comparator<Ai>() {
            @Override
            int compare(Ai o1, Ai o2) {
                return o1.getGameTurn().compareTo(o2.getGameTurn())
            }
        })
        spatialHash = new IntervalKDTree2d<>(Math.max(x, y), 4)
        entityTemp = new HashSet<>();
    }

    public void add(Entity e) {
        if (e.ai)
            actors.add(e.ai)
        spatialHash.put(e.x - 0.25, e.y - 0.25, e.x + e.xSize - 0.75, e.y + e.xSize - 0.75, e)
    }

    public void remove(Entity e) {
        if (e.ai)
            actors.remove(e.ai)
        spatialHash.remove(e)
    }


    void update(Entity e) {
        spatialHash.put(e.x - 0.25, e.y - 0.25, e.x + e.xSize - 0.75, e.y + e.ySize - 0.75, e)
    }

    public HashSet<Entity> getEntitiesAtLocation(int x, int y) {
        entityTemp.clear()
        return spatialHash.getValues(x - 0.5, y - 0.5, x + 0.5, y + 0.5, entityTemp)
    }

    public HashSet<Entity> getEntitiesBetween(int lowX, int lowY, int highX, int highY) {
        entityTemp.clear()
        return spatialHash.getValues(lowX - 0.5, lowY - 0.5, highX + 0.5, highY + 0.5, entityTemp)
    }


    public boolean isBlocked(int x, int y, Entity ignore = null) {

        if (super.isBlocked(x, y))
            return true;
        for (Entity entity : getEntitiesAtLocation(x, y)) {
            if (entity != ignore && entity.blocks)
                return true
        }

        return false
    }

    // This is used to find up and down stairs
    public Point2i findATile(TileType tileType) {
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                if (ground[x][y].tileType == tileType) {
                    return new Point2i(x, y)
                }
            }
        }
        return null
    }

    public void setExplored(int x, int y, boolean isExplored) {
        ground[x][y].isExplored = isExplored
    }

    public TileType getTileType(int x, int y) {
        return ground[x][y].tileType
    }
}
