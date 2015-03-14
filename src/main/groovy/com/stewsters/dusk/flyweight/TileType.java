package com.stewsters.dusk.flyweight;

import squidpony.squidcolor.SColor;

public enum TileType {


    FLOOR_STONE('.', SColor.WHITE, SColor.BLACK, false, 0f),
    FLOOR_WOOD('.', SColor.ALOEWOOD, SColor.BROWN, false, 0f),
    FLOOR_DIRT('.', SColor.BROWN, SColor.BROWNER, false, 0f),

    GRASS_SHORT('.', SColor.GREEN_YELLOW, SColor.DARK_GREEN, false, 0f),
    GRASS_MEDIUM(';', SColor.BLUE_GREEN_DYE, SColor.DARK_GREEN, false, 0.1f),
    GRASS_TALL('\\', SColor.BLUE_GREEN, SColor.DARK_GREEN, false, 0.2f),
    GRASS_FOLIAGE('♈', SColor.AMBER, SColor.DARK_GREEN, false, 0.5f),

    WALL('#', SColor.DARK_GRAY, SColor.LIGHT_GRAY, true, 1f),
    WINDOW('#', SColor.ALICE_BLUE, SColor.LIGHT_BLUE, true, 0.25f),

    WATER_SHALLOW('.', SColor.AZUL, SColor.DARK_BLUE, false, 0f),
    WATER_DEEP('≈', SColor.AZUL, SColor.NAVY_BLUE, false, 0f),

    LAVA_SHALLOW('~', SColor.WHITE, SColor.CORAL_RED, false, 0f),
    LAVA_DEEP('≈', SColor.WHITE, SColor.DARK_RED, false, 0f),

    TREE('t', SColor.GREENFINCH, SColor.BROWN, true, 0.3f),
    EVERGREEN('₤', SColor.FOREST_GREEN, SColor.GREEN_BAMBOO, true, 0.7f),

    DOOR_CLOSED('+', SColor.WHITE, SColor.BROWN, true, 1f),
    DOOR_OPEN('/', SColor.WHITE, SColor.BROWN, false, 0f),

    STAIRS_UP('<', SColor.WHITE, SColor.BLACK, false, 0.8f),
    STAIRS_DOWN('>', SColor.WHITE, SColor.BLACK, false, 0.8f),

    GAME_WIN('_', SColor.GREEN, SColor.DARK_GREEN, false, 0f);

    public final boolean blocks;
    public final SColor color;
    public final SColor background;
    public final char character;
    public float opacity;

    public static TileType[] lookup = TileType.values();

    TileType(char character, SColor color, SColor background, boolean blocked, float opacity) {

        this.character = character;
        this.color = color;
        this.background = background;
        this.blocks = blocked;
        this.opacity = opacity;
    }


    public byte id() {
        return (byte) ordinal();
    }

    public static TileType[] grassTypes = {GRASS_SHORT, GRASS_MEDIUM, GRASS_TALL, GRASS_FOLIAGE};

}
