package com.stewsters.dusk.map

import com.stewsters.dusk.component.ai.Ai
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.TileType
import com.stewsters.dusk.main.RenderConfig
import com.stewsters.util.math.Point2i
import com.stewsters.util.spatial.IntervalKDTree2d
import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.gui.swing.SwingPane

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
        spatialHash.put(e.x - 0.25, e.y - 0.25, e.x + 0.25, e.y + 0.25, e)
    }

    public void remove(Entity e) {
        if (e.ai)
            actors.remove(e.ai)
        spatialHash.remove(e)
    }


    void update(Entity e) {
        spatialHash.remove(e)
        spatialHash.put(e.x - 0.25, e.y - 0.25, e.x + 0.25, e.y + 0.25, e)
    }

    public HashSet<Entity> getEntitiesAtLocation(int x, int y) {
        entityTemp.clear()
        return spatialHash.getValues(x - 0.5, y - 0.5, x + 0.5, y + 0.5, entityTemp)
    }

    public HashSet<Entity> getEntitiesBetween(int lowX, int lowY, int highX, int highY) {
        entityTemp.clear()
        return spatialHash.getValues(lowX - 0.5, lowY - 0.5, highX + 0.5, highY + 0.5, entityTemp)
    }

    public void makeNoise(int x, int y, int noise) {

        int noiseSquare = noise * noise

        getEntitiesBetween(x - noise, y - noise, x + noise, y + noise).each {
            if (it.ai && ((it.x - x) * (it.x - x)) + ((it.y - y) * (it.y - y)) < noiseSquare) {
                it.ai.hearNoise(x, y)
            }
        }

    }

    @Override
    public boolean isBlocked(int x, int y, Entity ignore = null) {

        if (super.isBlocked(x, y))
            return true;
        for (Entity entity : getEntitiesAtLocation(x, y) - ignore) {
            if (entity.blocks)
                return true
        }

        return false
    }

    /**
     * Performs the Field of View process
     *
     * @param startx
     * @param starty
     */
    public void render(SwingPane display, Entity player) {

        int left = player.x - (RenderConfig.mapScreenWidth / 2)
        int top = player.y - (RenderConfig.mapScreenHeight / 2)

        // Render tiles
        for (int x = 0; x < RenderConfig.mapScreenWidth; x++) {
            for (int y = 0; y < RenderConfig.mapScreenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                int sx = x + RenderConfig.leftWindow
                int sy = y

                if (outsideMap(wx, wy)) {
                    display.clearCell(sx, sy)

                } else if (getLight(wx, wy) > 0) {

                    double radius = Math.sqrt((wx - player.x) * (wx - player.x) + (wy - player.y) * (wy - player.y));
                    SColor cellLight = SColorFactory.fromPallet("dark", getLight(wx, wy) as float)

                    SColor objectLight = SColorFactory.blend(
                            ground[wx][wy].gore ? SColor.RED : ground[wx][wy].color, cellLight, getTint(radius));
                    SColor backColor = SColorFactory.blend(
                            ground[wx][wy].background, cellLight, getTint(radius));

                    display.placeCharacter(sx, sy, ground[wx][wy].representation, objectLight, backColor);

                    ground[wx][wy].isExplored = true

                } else if (ground[wx][wy].isExplored) {
                    Tile tile = ground[wx][wy]
                    display.placeCharacter(sx, sy, tile.representation, tile.color.darker().darker(), tile.background.darker().darker())

                } else {
                    display.clearCell(sx, sy);
                }

            }
        }

        // Render Objects
        getEntitiesBetween(left, top, left + RenderConfig.mapScreenWidth, top + RenderConfig.mapScreenHeight).sort {
            it.priority
        }.each { Entity entity ->

            int screenPositionX = entity.x - left + RenderConfig.leftWindow
            int screenPositionY = entity.y - top

            if (screenPositionX >= 0 && screenPositionX < RenderConfig.mapScreenWidth && screenPositionY >= 0 && screenPositionY < RenderConfig.mapScreenHeight) {

                float light = getLight(entity.x, entity.y)
                if (light > 0f) {

                    //put the player at the origin of the FOV

                    SColor cellLight = SColorFactory.fromPallet("dark", light);
                    SColor objectLight = SColorFactory.blend(entity.color, cellLight, getTint(0f));

                    for(int x = 0 ; x <entity.xSize; x++){
                        for(int y = 0 ; y <entity.ySize; y++){
                            display.placeCharacter(screenPositionX + x, screenPositionY + y, entity.ch, objectLight);
                        }
                    }
                }
            }
        }
    }

/**
 * Custom method to determine tint based on radius as well as general tint
 * factor.
 *
 * @param radius
 * @return
 */
    private static float getTint(double radius) {
        return (float) (RenderConfig.lightTintPercentage * radius);//adjust tint based on distance
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
