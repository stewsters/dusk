package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.graphic.MessageLog
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
class LightningStrike implements Spell {

    public static final int LIGHTNING_DAMAGE = 20
    public static final int LIGHTNING_RANGE = 5


    @Override
    public boolean cast(Entity caster) {

        Entity enemy = caster.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLog.send('No enemy is visible to strike.', SColor.RED, [caster])
            return false
        } else if (caster.distanceTo(enemy) > LIGHTNING_RANGE) {
            MessageLog.send("${enemy.name} is too farther than $LIGHTNING_RANGE.", SColor.RED, [caster])
            return false
        } else {
            int actualDamage = enemy.fighter.takeDamage(LIGHTNING_DAMAGE, caster, [DamageType.LIGHTNING])
            MessageLog.send("A lightning bolt strikes the ${enemy.name} with a loud thunder for ${actualDamage} damage!", SColor.LIGHT_BLUE, [caster, enemy])
            return true
        }
    }

    @Override
    public String getDescription() {
        "Lightning strikes a target no more than ${LIGHTNING_RANGE + level} spaces away for ${LIGHTNING_DAMAGE} damage."
    }


}
