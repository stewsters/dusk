package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog

public class Cleanse implements Spell {

    public static final int HEAL_AMOUNT = 20

    public Cleanse() {
        name = "Cleanse"
        key = 'l'
    }

    @Override
    boolean cast(Entity caster) {

        //removes toxicity from the system.
        caster.fighter.toxicity = 0
        MessageLog.send("${caster.name} is cleansed.", [caster])
        return true
    }

    @Override
    public String getDescription() {
        "Removes toxicity"
    }
}

