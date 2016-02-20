package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

public class Cleanse implements Spell {

    public static final int HEAL_AMOUNT = 20

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

