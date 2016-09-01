package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.math.MatUtils

class BasicOpponent extends BaseAi implements Ai {

    public BasicOpponent(int speed = 10) {
        this.speed = speed

        gameTurn = MatUtils.getIntInRange(1, speed)
    }

    private boolean active = false;

    public boolean takeTurn() {
        if (!owner)
            return true//you died this turn, sorry bro
        if (!active) {

            if (owner.levelMap.ground[owner.x][owner.y].isExplored)
                active = true
        } else {
            //nearest opponent
            Entity enemy = findClosestVisibleEnemy()

            if (enemy) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {

                if (MatUtils.boolean) {
                    owner.randomMovement()
                }
            }
        }
        gameTurn += speed
        return true
    }


}