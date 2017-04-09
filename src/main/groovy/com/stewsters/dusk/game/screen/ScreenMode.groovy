package com.stewsters.dusk.game.screen

import groovy.transform.CompileStatic

@CompileStatic
enum ScreenMode {
    PLAYING,
    INVENTORY,
    INVENTORY_INSPECT,
    APPLY,
    EQUIP,
    REMOVE,
    THROW,
    DROP,
    CAST
}