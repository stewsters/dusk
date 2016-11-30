package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.magic.Spell
import groovy.transform.CompileStatic

@CompileStatic
class Spellbook {

    Entity entity
    public List<Spell> spells = []

    boolean castSpell(char key) {
        Spell spell = spells.find { it.key.toUpperCase() == key }
        return spell ? spell.cast(entity) : false
    }

}
