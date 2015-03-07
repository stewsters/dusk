package com.stewsters.dusk.graphic

import com.stewsters.dusk.main.RenderConfig
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

/**
 * This is a quick horizontal status bar
 */
class StatusBar {

/**
 *
 * @param display
 * @param x
 * @param y
 * @param totalWidth
 * @param name
 * @param value
 * @param maximum
 * @param barColor
 */
    public
    static void render(SwingPane display, int x, int y, int totalWidth, String name, int value, int maximum, SColor barColor) {

        double ratio = (double) value / (double) maximum
        int barWidth = (int) Math.ceil(ratio * (double) totalWidth)

        totalWidth.times { xPos ->
            2.times { yPos ->
                display.clearCell(xPos + x, yPos + y)
            }
        }


        (0..(totalWidth - 1)).each { int xOffset ->
            if (xOffset < barWidth || value == maximum)
                display.placeCharacter(x + xOffset, y + 1, '#' as char, barColor)
            else
                display.placeCharacter(x + xOffset, y + 1, '-' as char, barColor)
        }
        display.placeHorizontalString(x, y, "${name}: ${value}/${maximum}")
    }

    //width 20
    public static void renderTextOnly(SwingPane display, int x, int y, String name, int value, int maximum) {

        20.times { xPos ->
            display.clearCell(xPos + x, y)
        }

        display.placeHorizontalString(x, y, "${name}: ${value}/${maximum}")
    }

    public static void renderTextBar(SwingPane display, int x, int y, int totalWidth, String name, int value, int maximum, SColor barColor) {

        double ratio = (double) value / (double) maximum
        int barWidth = (int) Math.ceil(ratio * (double) totalWidth)

        if (name.size() > 20)
            name = name.substring(0, 20)

        int leftJust = (20 - name.size()) / 2

        RenderConfig.leftWindow.times { xPos ->

            int offset = xPos - leftJust

            char character

            if (offset < 0 || offset >= name.size()) {
                character = ' '
            } else {
                character = name.charAt(offset)
            }

            if (xPos < barWidth) {
                display.placeCharacter(x + xPos, y, character, SColor.WHITE, barColor)
            } else {
                display.placeCharacter(x + xPos, y, character, SColor.WHITE, SColor.BLACK)
            }
        }

//        display.placeHorizontalString(x, y, "${name}: ${value}/${maximum}")
    }
}
