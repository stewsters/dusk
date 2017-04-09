package com.stewsters.dusk.game.screen.subscreen

import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

@CompileStatic
class ListSelector<T> {

    private String title
    private int selectedIndex = 0
    private List<T> options

    ListSelector(String title, List<T> options) {
        this.title = title
        this.options = options
        selectedIndex = MatUtils.getIntInRange(0, options.size() - 1)
    }


    void render(SwingPane display, int xMin, int yMin, boolean colSelected) {
        display.placeHorizontalString(xMin, yMin, title)
        options.eachWithIndex { T option, Integer i ->

            SColor foreColor
            SColor rearColor
            if (options[selectedIndex] == option) {
                foreColor = colSelected ? SColor.DARK_BLUE : SColor.WHITE
                rearColor = colSelected ? SColor.WHITE : SColor.BLACK
            } else {
                foreColor = SColor.GRAY
                rearColor = SColor.BLACK
            }
            display.placeHorizontalString(xMin, yMin + 2 + 2 * i, option.toString().toLowerCase().capitalize(), foreColor, rearColor)
        }

    }

    void up() {
        selectedIndex = Math.max(0, selectedIndex - 1)
    }

    void down() {
        selectedIndex = Math.min(options.size() - 1, selectedIndex + 1)
    }


    T getSelected() {
        return options[selectedIndex]
    }

}
