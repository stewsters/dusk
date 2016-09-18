package com.stewsters.dusk.game.renderSystems

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.core.map.Tile
import com.stewsters.dusk.game.RenderConfig
import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.gui.swing.SwingPane

public class MapRenderSystem {

    public static void render(SwingPane display, LevelMap levelMap, Entity player) {

        int left = player.x - (RenderConfig.mapScreenWidth / 2)
        int top = player.y - (RenderConfig.mapScreenHeight / 2)

        // Render tiles
        for (int x = 0; x < RenderConfig.mapScreenWidth; x++) {
            for (int y = 0; y < RenderConfig.mapScreenHeight; y++) {

                int wx = x + left;
                int wy = y + top;

                int sx = x + RenderConfig.leftWindow
                int sy = -y + RenderConfig.mapScreenHeight - 1

                // Outside the map is just back
                if (levelMap.outsideMap(wx, wy)) {
                    display.clearCell(sx, sy)
                    continue
                }

                float lightLevel = levelMap.getLight(wx, wy);
                if (lightLevel > 0) {

                    double radius = Math.sqrt((wx - player.x) * (wx - player.x) + (wy - player.y) * (wy - player.y));
                    SColor cellLight = SColorFactory.fromPallet("dark", lightLevel);
                    SColor backColor = SColorFactory.blend(
                            levelMap.ground[wx][wy].groundCover ? levelMap.ground[wx][wy].groundCover.color : levelMap.ground[wx][wy].background,
                            cellLight, getTint(radius));

                    Entity entity = levelMap.getEntitiesAtLocation(wx, wy).max { it.priority }
                    if (entity) {

                        SColor objectLight = SColorFactory.blend(entity.color, cellLight, getTint(0f));
                        display.placeCharacter(sx, sy, entity.ch, objectLight, backColor);

                    } else {

                        SColor groundLight = SColorFactory.blend(levelMap.ground[wx][wy].color, cellLight, getTint(radius));

                        display.placeCharacter(sx, sy, levelMap.ground[wx][wy].representation, groundLight, backColor);
                    }

                    levelMap.ground[wx][wy].isExplored = true

                } else if (levelMap.ground[wx][wy].isExplored) {
                    Tile tile = levelMap.ground[wx][wy]
                    display.placeCharacter(sx, sy, tile.representation, tile.color.darker().darker(), tile.background.darker().darker())

                } else {
                    display.clearCell(sx, sy);
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
}
