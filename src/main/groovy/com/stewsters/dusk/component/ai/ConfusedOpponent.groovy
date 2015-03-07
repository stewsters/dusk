package com.stewsters.dusk.component.ai

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
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
            owner.move(MatUtils.getIntInRange(-1, 1), MatUtils.getIntInRange(-1, 1))
            numTurns--
            gameTurn += speed
            return true
        } else {
            restore()
            MessageLog.send("The ${owner.name} is no longer confused!", SColor.RED, [owner, castor])
            gameTurn += speed
            return false
        }

    }


    private void restore() {
        owner.levelMap.actors.remove(this)

        if (oldAI) {
            oldAI.setGameTurn(gameTurn)
            owner.ai = oldAI
            owner.levelMap.actors.add oldAI
        }
    }

}
