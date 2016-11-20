package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class ConfusedOpponent extends BaseAi implements Ai {
    Ai oldAI = null
    int numTurns = 0
    Entity castor

    public ConfusedOpponent(params) {
        oldAI = params.oldAI
        numTurns = params.numTurns
        castor = params.castor
        gameTurn = oldAI.gameTurn
    }

    @Override
    public boolean takeTurn() {

        if (numTurns > 0) {
            entity.move(MatUtils.getIntInRange(-1, 1), MatUtils.getIntInRange(-1, 1))
            numTurns--
            gameTurn += speed
            return true
        } else {
            restore()
            MessageLogSystem.send("The ${entity.name} is no longer confused!", SColor.RED, [entity, castor])
            gameTurn += speed
            return false
        }

    }


    private void restore() {
        entity.levelMap.actors.remove(this)

        if (oldAI) {
            oldAI.setGameTurn(gameTurn)
            entity.ai = oldAI
            entity.levelMap.actors.add oldAI
        }
    }

}
