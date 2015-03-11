package com.stewsters.dusk.component.ai

import com.stewsters.dusk.entity.Entity
import com.stewsters.util.math.MatUtils

class BasicOpponent extends BaseAi implements Ai {

    public BasicOpponent(int speed = 10) {
        this.speed = speed

        gameTurn = MatUtils.getIntInRange(1, speed)
    }

    private boolean active = true;

    public boolean takeTurn() {
        if (!owner)
            return //you died this turn, sorry bro
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
                    owner.move(MatUtils.getIntInRange(-1, 1), MatUtils.getIntInRange(-1, 1))
                } else {
                    if (lastNoise) {
                        owner.moveTowardsAndAttack(lastNoise.x, lastNoise.y)
                    }
                }
            }
        }
        gameTurn += speed
        return true
    }


}
