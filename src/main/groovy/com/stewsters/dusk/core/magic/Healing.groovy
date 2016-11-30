package com.stewsters.dusk.core.magic

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
class Healing implements Spell {

    public static final int HEAL_AMOUNT = 20

    Healing() {
        name = "Healing"
        key = 'h' as char
    }

    @Override
    boolean cast(Entity caster) {

        if (caster.fighter.hp == caster.fighter.maxHP) {
            MessageLogSystem.send("You are already at full health.", SColor.WHITE, [caster])
            return false
        } else {

            MessageLogSystem.send("Your wounds seal up.", SColor.GREEN, [caster])
            caster.fighter.addHealth(level + MatUtils.d(HEAL_AMOUNT + level))

            return true
        }

    }

    @Override
    String getDescription() {
        "Heals $level + d(${HEAL_AMOUNT + level}) damage."
    }
}
