package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i

/**
 * This unit attacks on sight, and scavenges items when not in combat
 */
class ChargerAi extends BaseAi implements Ai {

    Point2i chargeDest = null

    public ChargerAi(int speed = 10) {
        this.speed = speed
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

    /**
     *
     * @return
     */
    @Override
    public boolean takeTurn() {


        if (chargeDest) {

            if (entity.moveTowards(chargeDest.x, chargeDest.y)) {
                gameTurn += Math.max(1, (int) (speed / 3))
                return true

            } else {
                //TODO: should do bonus damage
                entity.moveTowardsAndAttack(chargeDest.x, chargeDest.y)
                chargeDest = null

                gameTurn += speed
                return true
            }

        }

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()
        if (enemy) {

            if (entity.distanceTo(enemy) < 2) {
                entity.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {

                // charge!  charge through them!
                int dx = enemy.x - entity.x
                int dy = enemy.y - entity.y

                chargeDest = new Point2i(enemy.x + dx, enemy.y + dy)
            }
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
            entity.randomMovement();
        }

        gameTurn += speed
        return true
    }
}
