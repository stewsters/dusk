package com.stewsters.dusk.screen.subscreen

import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

public class ListSelector<T> {

    private title
    private int selectedIndex = 0
    private List<T> options

    public ListSelector(String title, List<T> options) {
        this.title = title
        this.options = options
        selectedIndex = MatUtils.getIntInRange(0, options.size() - 1)
    }


    public void render(SwingPane display, int xMin, int yMin, boolean colSelected) {
        display.placeHorizontalString(xMin, yMin, title)
        options.eachWithIndex { T option, int i ->

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

    public void up() {
        selectedIndex = Math.max(0, selectedIndex - 1)
    }

    public void down() {
        selectedIndex = Math.min(options.size() - 1, selectedIndex + 1)
    }


    public T getSelected() {
        return options[selectedIndex]
    }

}
