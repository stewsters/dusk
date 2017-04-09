package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic

/**
 * This unit attacks on sight, and scavenges items when not in combat
 */
@CompileStatic
class KnightAi extends BaseAi implements Ai {

    KnightAi(int speed = 10) {
        this.speed = speed
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

    /**
     *
     * @return
     */
    @Override
    boolean takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()

        if (enemy) {
            //if we have a gun, and they are getting too close, shoot them
            entity.moveTowardsAndAttack(enemy.x, enemy.y)

        } else if (entity.inventory) {

            //if we are standing on an item and we have room, pick it up
            if (entity.inventory.isFull()) {
                entity.randomMovement()
            } else {
                //find nearest visible item
                Entity item = entity.ai.findClosestVisibleItem()

                //if we are standing on it, pickUp
                if (item) {
                    if (item.x == entity.x && item.y == entity.y) {
                        entity.inventory.pickUp(item)
                    } else {
                        entity.moveTowardsAndAttack(item.x, item.y)
                    }
                } else {
                    entity.randomMovement()
                }

            }
        } else if (MatUtils.boolean) {
            entity.randomMovement()
        }

        gameTurn += speed
        return true
    }
}
