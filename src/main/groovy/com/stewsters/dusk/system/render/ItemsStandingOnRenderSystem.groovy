package com.stewsters.dusk.system.render

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.main.RenderConfig
import com.stewsters.dusk.map.LevelMap
import squidpony.squidgrid.gui.swing.SwingPane

public class ItemsStandingOnRenderSystem {
    public static void render(SwingPane display, LevelMap levelMap, Entity player) {

        def names = (levelMap.getEntitiesAtLocation(player.x, player.y) - player).sort { Entity entity -> entity.priority }.name

        names.eachWithIndex { String name, Integer i ->
            if (i < RenderConfig.surroundingHeight) {
                display.placeHorizontalString(RenderConfig.mapScreenWidth + RenderConfig.leftWindow - RenderConfig.inventoryWidth, RenderConfig.surroundingY + i, name)
            }
        }

    }
}
