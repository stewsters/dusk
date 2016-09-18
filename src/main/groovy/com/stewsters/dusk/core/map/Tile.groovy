package com.stewsters.dusk.core.map

import com.stewsters.dusk.core.flyweight.GroundCover
import com.stewsters.dusk.core.flyweight.TileType
import squidpony.squidcolor.SColor

public class Tile {

    public TileType tileType
    public Boolean isExplored = false
    public GroundCover groundCover = null

    public Tile(TileType tileType) {
        this.tileType = tileType;
    }


    public boolean getIsBlocked() {
        tileType.blocks
    }

    public char getRepresentation() {
        tileType.character
    }

    public SColor getColor() {
        tileType.color
    }

    public SColor getBackground() {
        tileType.background
    }

    public float getOpacity() {
        tileType.opacity
    }

}
