package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
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

        if (params.speed)
            speed = params.speed

        if (oldAI)
            gameTurn = oldAI.gameTurn
        else if (caster && caster.ai) {
            gameTurn = caster.ai.gameTurn
        }
    }

    public boolean takeTurn() {

        int dx = target.x - entity.x
        int dy = target.y - entity.y
        float distance = Math.sqrt(dx**2 + dy**2)
        dx = (int) Math.round(dx / distance)
        dy = (int) Math.round(dy / distance)

        if (entity.move(dx, dy)) {
            // kept flying
            if (target.x == entity.x && target.y == entity.y) {

                if (onImpact(0, 0)) {
                    return false
                }

                // natural land
                gameTurn += speed // recovery
                restore()
                return false
            }
            gameTurn += speed
            return true

        } else {
            //collision
            if (onImpact(dx, dy)) {
                gameTurn += speed
                return false
            }

            gameTurn += speed // recovery
            restore()

            return false
        }

    }

    private boolean onImpact(int dx, int dy) {
        if (onImpact) {
            if (onImpact(caster, entity.x + dx, entity.y + dy)) {
                entity.levelMap.remove(entity)
                return true
            }
        }
        return false
    }


    private void restore() {
        if (!entity) // entity destroyed?
            return

        entity.levelMap.actors.remove(this)

        if (oldAI) {
            oldAI.setGameTurn(gameTurn)
            entity.ai = oldAI
            entity.levelMap.actors.add oldAI
        } else {
            entity.ai = null
        }
    }

}
