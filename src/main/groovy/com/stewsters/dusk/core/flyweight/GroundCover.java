package com.stewsters.dusk.core.flyweight;

import squidpony.squidcolor.SColor;

public enum GroundCover {
    BLOOD("blood", SColor.DARK_RED),
    GRASS("grass", SColor.DARK_GREEN),
    ASH("ash", SColor.GRAY);


    public final String displayName;
    public final SColor color;

    GroundCover(String name, SColor color) {
        this.displayName = name;
        this.color = color;
    }

}
