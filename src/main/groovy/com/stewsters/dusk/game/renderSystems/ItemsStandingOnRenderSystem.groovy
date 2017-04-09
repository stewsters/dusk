package com.stewsters.dusk.game.renderSystems

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.game.RenderConfig
import groovy.transform.CompileStatic
import squidpony.squidgrid.gui.swing.SwingPane

@CompileStatic
class ItemsStandingOnRenderSystem {
    static void render(SwingPane display, LevelMap levelMap, Entity player) {

        List<String> names = (levelMap.getEntitiesAtLocation(player.x, player.y) - player).sort { Entity entity -> entity.priority }.name

        String terrain = (levelMap.ground[player.x][player.y].groundCover ? levelMap.ground[player.x][player.y].groundCover.displayName + " covered " : "") + levelMap.ground[player.x][player.y].tileType.displayName
        display.placeHorizontalString(
                RenderConfig.screenWidth - terrain.length(),
                RenderConfig.screenHeight - 1,
                terrain
        )

        names.eachWithIndex { String name, Integer i ->
            if (i < RenderConfig.surroundingHeight) {
                display.placeHorizontalString(
                        RenderConfig.screenWidth - name.length(),
                        RenderConfig.screenHeight - i - 2, name)
            }
        }


    }
}
