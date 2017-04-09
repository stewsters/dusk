package com.stewsters.dusk.core.magic

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
trait Spell {

    private int level = 1
    private Character key
    private String name

    void setLevel(int level) {
        this.level = level
    }

    int getLevel() {
        level
    }

    String getName() {
        name
    }

    void setName(String name) {
        this.name = name
    }

    Character getKey() {
        key
    }

    void setKey(Character c) {
        key = c
    }

    String getDescription() {
        return "Unknown.  How mysterious!"
    }


    boolean cast(Entity castor) {
        MessageLogSystem.send("The spell does something unknown.", SColor.GREEN)
        return false
    }

}
