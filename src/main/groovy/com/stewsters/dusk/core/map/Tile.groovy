package com.stewsters.dusk.core.map

import com.stewsters.dusk.core.flyweight.GroundCover
import com.stewsters.dusk.core.flyweight.TileType
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
class Tile {

    public TileType tileType
    public Boolean isExplored = false
    public GroundCover groundCover = null

    Tile(TileType tileType) {
        this.tileType = tileType
    }


    boolean getIsBlocked() {
        tileType.blocks
    }

    char getRepresentation() {
        tileType.character
    }

    SColor getColor() {
        tileType.color
    }

    SColor getBackground() {
        tileType.background
    }

    float getOpacity() {
        tileType.opacity
    }

}
