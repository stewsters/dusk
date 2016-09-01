package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.math.MatUtils

class SkirmisherAi extends BaseAi implements Ai {

    public SkirmisherAi() {
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

    @Override
    public boolean takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()


        if (enemy && owner.fighter) {
            //if we have a gun, and they are getting too close, shoot them

            if (owner.fighter.hp == owner.fighter.maxHP) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {
                //if injured, move away. This works best for regenerators
                if (!owner.moveAway(enemy.x, enemy.y)) {
                    //but if you cant, fight it out.
                    owner.moveTowardsAndAttack(enemy.x, enemy.y)
                }

            }
        } else if (MatUtils.boolean) {
            owner.randomMovement();
        } else {
            //TODO: regeneration
            owner.standStill()
            if (owner.fighter) {
                owner.fighter.addHealth(1)
            }

        }

        gameTurn += speed
        return true
    }
}
