package com.stewsters.dusk.game.renderSystems

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.RenderConfig
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.core.map.Tile
import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.gui.swing.SwingPane;

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
                int sy = y

                if (levelMap.outsideMap(wx, wy)) {
                    display.clearCell(sx, sy)

                } else if (levelMap.getLight(wx, wy) > 0) {

                    double radius = Math.sqrt((wx - player.x) * (wx - player.x) + (wy - player.y) * (wy - player.y));
                    SColor cellLight = SColorFactory.fromPallet("dark", levelMap.getLight(wx, wy) as float)

                    SColor objectLight = SColorFactory.blend(
                            levelMap.ground[wx][wy].gore ? SColor.RED : levelMap.ground[wx][wy].color, cellLight, getTint(radius));
                    SColor backColor = SColorFactory.blend(
                            levelMap.ground[wx][wy].background, cellLight, getTint(radius));

                    display.placeCharacter(sx, sy, levelMap.ground[wx][wy].representation, objectLight, backColor);

                    levelMap.ground[wx][wy].isExplored = true

                } else if (levelMap.ground[wx][wy].isExplored) {
                    Tile tile = levelMap.ground[wx][wy]
                    display.placeCharacter(sx, sy, tile.representation, tile.color.darker().darker(), tile.background.darker().darker())

                } else {
                    display.clearCell(sx, sy);
                }

            }
        }

        // Render Objects
        levelMap.getEntitiesBetween(left, top, left + RenderConfig.mapScreenWidth, top + RenderConfig.mapScreenHeight).sort {
            it.priority
        }.each { Entity entity ->

            int screenPositionX = entity.x - left + RenderConfig.leftWindow
            int screenPositionY = entity.y - top

            if (screenPositionX >= 0 && screenPositionX < RenderConfig.mapScreenWidth && screenPositionY >= 0 && screenPositionY < RenderConfig.mapScreenHeight) {

                float light = levelMap.getLight(entity.x, entity.y)
                if (light > 0f) {

                    //put the player at the origin of the FOV

                    SColor cellLight = SColorFactory.fromPallet("dark", light);
                    SColor objectLight = SColorFactory.blend(entity.color, cellLight, getTint(0f));

                    for (int x = 0; x < entity.xSize; x++) {
                        for (int y = 0; y < entity.ySize; y++) {
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
}
