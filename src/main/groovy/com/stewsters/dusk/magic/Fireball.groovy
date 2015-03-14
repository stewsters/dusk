package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
class Fireball implements Spell {

    public static final int FIREBALL_DAMAGE = 6
    public static final int FIREBALL_RANGE = 3


    @Override
    public boolean cast(Entity caster) {

        //TODO: create a fireball aimed at the target
//        List<Entity> targets = caster.ai.findVisibleEnemies(FIREBALL_RANGE + level)

        Entity enemy = caster.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLog.send('No enemy is close enough to strike.', SColor.RED, [caster])
            return false
        } else if (caster.distanceTo(enemy) > FIREBALL_RANGE + level) {
            MessageLog.send(enemy.name + ' is too far to strike.', SColor.RED, [caster])
            return false
        } else {
            int damage = MatUtils.d(FIREBALL_DAMAGE + level) + level

            enemy.fighter.takeDamage(damage, caster, [DamageType.FIRE])

            MessageLog.send("Flame envelopes ${enemy.name}! The damage is ${damage} hit points.", SColor.LIGHT_BLUE, [caster, enemy])
            return true
        }
    }

    @Override
    public String getDescription() {
        "Fires a ball of fire to envelope a target no more than ${FIREBALL_DAMAGE + level} spaces away for ${level + 1} to ${FIREBALL_DAMAGE + level + level} damage."
    }


}
