package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

public class Healing implements Spell {

    public static final int HEAL_AMOUNT = 20

    @Override
    boolean cast(Entity caster) {

        if (caster.fighter.hp == caster.fighter.maxHP) {
            MessageLog.send("You are already at full health.", SColor.WHITE, [caster])
            return false
        } else {

            MessageLog.send("Your wounds seal up.", SColor.GREEN, [caster])
            caster.fighter.addHealth(level + MatUtils.d(HEAL_AMOUNT + level))

            return true
        }

    }

    @Override
    public String getDescription() {
        "Heals $level + d(${HEAL_AMOUNT + level}) damage."
    }
}
