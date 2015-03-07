package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

/**
 * Created by stewsters on 1/28/15.
 */
public class Healing implements Spell {

    public static final int HEAL_AMOUNT = 10

    @Override
    boolean cast(Entity caster) {

        if (caster.fighter.hp == caster.fighter.maxHP) {
            MessageLog.send("You are already at full health.", SColor.WHITE, [caster])
            return false
        } else {

            MessageLog.send("Your wounds seal up.", SColor.GREEN)
            caster.fighter.addHealth(level + MatUtils.d(HEAL_AMOUNT + level))
            caster.fighter.addToxicity(2);
            return true
        }

    }
}
