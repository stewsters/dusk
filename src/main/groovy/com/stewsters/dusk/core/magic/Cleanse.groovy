package com.stewsters.dusk.core.magic

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
public class Cleanse implements Spell {

    public static final int HEAL_AMOUNT = 20

    public Cleanse() {
        name = "Cleanse"
        key = 'l' as char
    }

    @Override
    boolean cast(Entity caster) {

        //removes toxicity from the system.
        caster.fighter.toxicity = 0
        MessageLogSystem.send("${caster.name} is cleansed.", SColor.AMBER, [caster])
        return true
    }

    @Override
    public String getDescription() {
        "Removes toxicity"
    }
}

