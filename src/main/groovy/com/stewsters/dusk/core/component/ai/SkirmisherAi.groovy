package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic

@CompileStatic
class SkirmisherAi extends BaseAi implements Ai {

    SkirmisherAi() {
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

    @Override
    boolean takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()


        if (enemy && entity.fighter) {
            //if we have a gun, and they are getting too close, shoot them

            if (entity.fighter.hp == entity.fighter.maxHP) {
                entity.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {
                //if injured, move away. This works best for regenerators
                if (!entity.moveAway(enemy.x, enemy.y)) {
                    //but if you cant, fight it out.
                    entity.moveTowardsAndAttack(enemy.x, enemy.y)
                }

            }
        } else if (MatUtils.boolean) {
            entity.randomMovement()
        } else {
            //TODO: regeneration
            entity.standStill()
            if (entity.fighter) {
                entity.fighter.addHealth(1)
            }

        }

        gameTurn += speed
        return true
    }
}
