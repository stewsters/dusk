package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
public class Domination implements Spell {

    public static final int DOMINATION_RANGE = 3

    @Override
    boolean cast(Entity caster) {

        Entity enemy = caster.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLog.send('No enemy is close enough to dominate.', SColor.RED)
            return false
        } else if (caster.distanceTo(enemy) > DOMINATION_RANGE) {
            MessageLog.send("${enemy.name} is too far to dominate.", SColor.RED, [caster])
            return false
        } else {
            MessageLog.send("Dark magic takes over ${enemy.name}.", SColor.LIGHT_BLUE, [caster, enemy])
            enemy.faction = caster.faction
            return true
        }

        return true

    }

    @Override
    public String getDescription() {
        "Changes the alignment of the nearest enemy to match yours."
    }

}
