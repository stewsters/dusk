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

}
