package com.stewsters.dusk.game

import groovy.transform.CompileStatic

@CompileStatic
class Game {
    public static int gameTurn = 1


    public static passTime() {
        gameTurn++
    }

//    public static final int DAYLENGTH = 100
//    public static boolean isDay() {
//        return (gameTurn % (2 * DAYLENGTH)) < DAYLENGTH
//    }

}
