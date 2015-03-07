package com.stewsters.dusk.graphic

import com.stewsters.util.math.geom.Rect
import squidpony.squidgrid.gui.swing.SwingPane


class MessageBox {


    public static void render(SwingPane display, String message, Rect location) {

        for (int x = location.x1; x < location.x2; x++) {
            for (int y = location.y1; y < location.y2; y++) {
                display.clearCell(x, y)
            }
        }
        display.placeHorizontalString(location.x1 + 1, location.y1 + 1, message)
        //todo: add space to close?
    }

}
