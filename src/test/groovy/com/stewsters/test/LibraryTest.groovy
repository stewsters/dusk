package com.stewsters.test

import com.stewsters.dusk.game.Game
import spock.lang.Specification

class LibraryTest extends Specification {
    def "daylight looks ok"() {
        given:
        Game.gameTurn = gameTurn

        when:
        double light = Game.dayLight

        then:
        light <= expectedLight + 0.0001
        light >= expectedLight - 0.0001

        where:
        gameTurn                  | expectedLight
        0                         | 0f
        Game.DAY_LENGTH * (1 / 4) | 1f
        Game.DAY_LENGTH * (2 / 4) | 0f
        Game.DAY_LENGTH * (3 / 4) | -1f
        Game.DAY_LENGTH * (4 / 4) | 0f
    }
}
