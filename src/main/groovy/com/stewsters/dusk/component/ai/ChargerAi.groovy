package com.stewsters.dusk.component.ai

import com.stewsters.dusk.entity.Entity
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

            if (owner.moveTowards(chargeDest.x, chargeDest.y)) {
                gameTurn += Math.max(1, (int) (speed / 3))
                return true

            } else {
                //TODO: should do bonus damage
                owner.moveTowardsAndAttack(chargeDest.x, chargeDest.y)
                chargeDest = null

                gameTurn += speed
                return true
            }

        }

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()
        if (enemy) {

            if (owner.distanceTo(enemy) < 2) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {

                // charge!  charge through them!
                int dx = enemy.x - owner.x
                int dy = enemy.y - owner.y

                chargeDest = new Point2i(enemy.x + dx, enemy.y + dy)
            }
        }
        else if (owner.inventory) {

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
