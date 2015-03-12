package com.stewsters.dusk.component.ai

import com.stewsters.dusk.entity.Entity
import com.stewsters.util.math.Point2i

class Projectile extends BaseAi implements Ai {
    Ai oldAI = null
    Entity caster
    Point2i target
    Closure onImpact

    public Projectile(params) {
        oldAI = params.oldAI
        caster = params.caster
        target = params.target
        onImpact = params.onImpact

        if (oldAI)
            gameTurn = oldAI.gameTurn
        else if (caster && caster.ai) {
            gameTurn = caster.ai.gameTurn
        }
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

                if (onImpact(0, 0)) {
                    return false
                }

                // natural land
                gameTurn += speed // recovery
                restore()
                return false
            }
            return true

        } else {
            //collision
            if (onImpact(dx, dy)) {
                return false
            }

            gameTurn += speed // recovery
            restore()

            return false
        }
    }

    private onImpact(int dx, int dy) {
        if (onImpact) {
            if (onImpact(caster, owner.x + dx, owner.y + dy)) {
                owner.levelMap.remove(owner)
                return true
            }
        }
        return false
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
