package com.stewsters.dusk.core.magic

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
class Domination implements Spell {

    public static final int DOMINATION_RANGE = 3

    Domination() {
        name = "Domination"
        key = 'd' as char
    }

    @Override
    boolean cast(Entity caster) {

        Entity enemy = caster.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLogSystem.send('No enemy is close enough to dominate.', SColor.RED)
            return false
        } else if (caster.distanceTo(enemy) > DOMINATION_RANGE) {
            MessageLogSystem.send("${enemy.name} is too far to dominate.", SColor.RED, [caster])
            return false
        }
        MessageLogSystem.send("Dark magic takes over ${enemy.name}.", SColor.LIGHT_BLUE, [caster, enemy])
        enemy.faction = caster.faction
        return true

    }

    @Override
    String getDescription() {
        "Changes the alignment of the nearest enemy to match yours."
    }

}
