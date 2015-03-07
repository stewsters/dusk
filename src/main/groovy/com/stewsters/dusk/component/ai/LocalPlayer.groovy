package com.stewsters.dusk.component.ai

import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic

@CompileStatic
public class LocalPlayer extends BaseAi implements Ai {

    public LocalPlayer() {
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

    public boolean takeTurn() {
        gameTurn += speed
        true
    }

}
