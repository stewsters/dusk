package com.stewsters.dusk.core.component.ai

import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic

@CompileStatic
class LocalPlayer extends BaseAi implements Ai {

    LocalPlayer() {
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

    boolean takeTurn() {
        gameTurn += speed
        true
    }

}
