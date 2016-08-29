package com.stewsters.dusk.system.render

import com.stewsters.dusk.main.RenderConfig
import squidpony.squidgrid.gui.swing.SwingPane

class BottomBarRenderSystem {
    static void render(SwingPane display, Integer currentZ) {
        String text = "Level ${currentZ + 1}    (I)nventory (A)pply (E)quip (R)emove (D)rop (G)rab (M)agic (P)layer"
        display.placeHorizontalString(1, RenderConfig.mapScreenHeight - 2, text)
    }
}
