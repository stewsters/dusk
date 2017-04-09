package com.stewsters.dusk.game

import groovy.transform.CompileStatic

@CompileStatic
class Game {
    public static int gameTurn = 1


    static passTime() {
        gameTurn++
    }

    public static final int DAY_LENGTH = 100

    //
    // Day begins at sunrise rather than midnight
    // Negative is moonlight (blue tint) and positive in sunlight (yellow tint)
    static double getDayLight() {
        double dayPeriod = (gameTurn % DAY_LENGTH) / (double) DAY_LENGTH

        return Math.sin(Math.PI * 2 * dayPeriod)
    }

}
