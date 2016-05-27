package com.stewsters.dusk.component

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.magic.Spell
import com.stewsters.dusk.main.RenderConfig
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

@CompileStatic
public class Spellbook {

    Entity owner
    public List<Spell> spells = []

    boolean castSpell(char key) {
        Spell spell = spells.find { it.key.toUpperCase() == key }
        return spell ? spell.cast(owner) : false
    }

    public void render(SwingPane display, String header) {
        for (int x = 0; x < RenderConfig.inventoryWidth; x++) {
            for (int y = 0; y < Math.min(RenderConfig.inventoryMaxHeight, spells.size()) + 1 + (header ? 1 : 0); y++) {
                display.clearCell(x + RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY)
            }
        }

        Integer y = 0
        spells.sort { it.name }.each { Spell spell ->

            String out = "${spell.key}) ${spell.name}"
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, out)
            y++
        }

        if (header)
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, "-- " + header,
                    SColor.GRAY, SColor.BLACK
            )
    }
}
