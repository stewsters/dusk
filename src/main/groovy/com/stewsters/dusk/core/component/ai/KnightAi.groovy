package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.math.MatUtils

/**
 * This unit attacks on sight, and scavenges items when not in combat
 */
class KnightAi extends BaseAi implements Ai {

    public KnightAi(int speed = 10) {
        this.speed = speed
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

    /**
     *
     * @return
     */
    @Override
    public boolean takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()

        if (enemy) {
            //if we have a gun, and they are getting too close, shoot them
            owner.moveTowardsAndAttack(enemy.x, enemy.y)

        } else if (owner.inventory) {

            //if we are standing on an item and we have room, pick it up
            if (owner.inventory.isFull()) {
                owner.randomMovement()
            } else {
                //find nearest visible item
                Entity item = owner.ai.findClosestVisibleItem()

                //if we are standing on it, pickUp
                if (item) {
                    if (item.x == owner.x && item.y == owner.y) {
                        owner.inventory.pickUp(item)
                    } else {
                        owner.moveTowardsAndAttack(item.x, item.y)
                    }
                } else {
                    owner.randomMovement()
                }

            }
        } else if (MatUtils.boolean) {
            owner.randomMovement();
        }

        gameTurn += speed
        return true
    }
}
