package com.stewsters.dusk.component.ai

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.util.math.Point2i
import squidpony.squidcolor.SColor

class Projectile extends BaseAi implements Ai {
    Ai oldAI = null
    Entity castor
    Point2i target

    public Projectile(params) {
        oldAI = params.oldAI
        castor = params.castor
        target = params.target
        gameTurn = oldAI.gameTurn
    }

    public boolean takeTurn() {

        int dx = target.x - owner.x
        int dy = target.y - owner.y
        float distance = Math.sqrt(dx**2 + dy**2)
        dx = (int) Math.round(dx / distance)
        dy = (int) Math.round(dy / distance)

        if (owner.move(dx, dy)) {
            // kept flying
            if (target.x == owner.x && target.y == owner.y) {
                // natural land
                gameTurn += speed // recovery
                restore()
                return false
            }
            return true

        } else {
            //collision
            gameTurn += speed // recovery
            restore()
            MessageLog.send("The ${owner.name} colides!", SColor.RED, [owner, castor])
            return false
        }
    }


    private restore() {
        owner.levelMap.actors.remove(this)

        if (oldAI) {
            oldAI.setGameTurn(gameTurn)
            owner.ai = oldAI
            owner.levelMap.actors.add oldAI
        } else {
            owner.ai = null
        }
    }

}
