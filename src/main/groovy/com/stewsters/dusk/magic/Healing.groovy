package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.system.render.MessageLogSystem
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
public class Healing implements Spell {

    public static final int HEAL_AMOUNT = 20

    public Healing() {
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
    public String getDescription() {
        "Heals $level + d(${HEAL_AMOUNT + level}) damage."
    }
}
