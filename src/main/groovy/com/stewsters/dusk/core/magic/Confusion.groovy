package com.stewsters.dusk.core.magic

import com.stewsters.dusk.core.component.ai.Ai
import com.stewsters.dusk.core.component.ai.ConfusedOpponent
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
class Confusion implements Spell {

    public static final int CONFUSE_RANGE = 10
    public static final int CONFUSE_NUM_TURNS = 20

    Confusion() {
        name = "Confusion"
        key = 'c' as char
    }

    @Override
    boolean cast(Entity caster) {

        Set<Entity> enemies = caster.ai.findAllVisibleEnemies(CONFUSE_RANGE)

//        Entity enemy = user.ai.findClosestVisibleEnemy()
        if (!enemies) {
            MessageLogSystem.send('No enemy is close enough to confused.', SColor.RED, [caster])
            return false
        } else {

            int turns = (int) Math.ceil((CONFUSE_NUM_TURNS as double) / enemies.size())

            enemies.each { Entity enemy ->
                Ai oldID = enemy.ai
                enemy.levelMap.actors.remove(oldID)
                enemy.ai = new ConfusedOpponent(oldAI: oldID, castor: caster, numTurns: turns)
                enemy.ai.entity = enemy
                enemy.levelMap.actors.add(enemy.ai)

                MessageLogSystem.send("${enemy.name} becomes confused.", SColor.LIGHT_BLUE)
            }

            return true
        }

    }

    @Override
    String getDescription() {
        "Confuses nearby enemies."
    }

}
