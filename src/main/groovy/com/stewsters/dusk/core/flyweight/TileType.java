package com.stewsters.dusk.core.flyweight;

import squidpony.squidcolor.SColor;

public enum TileType {


    FLOOR_STONE('.', SColor.WHITE, SColor.BLACK, "stone floor", false, 0f),
    FLOOR_WOOD('.', SColor.ALOEWOOD, SColor.BROWN, "wood floor", false, 0f),
    FLOOR_DIRT('.', SColor.BROWN, SColor.BROWNER, "dirt floor", false, 0f),

    GRASS_SHORT('.', SColor.GREEN_YELLOW, SColor.DARK_GREEN, "short grass", false, 0f),
    GRASS_MEDIUM(';', SColor.BLUE_GREEN_DYE, SColor.DARK_GREEN, "medium grass", false, 0.1f),
    GRASS_TALL('\\', SColor.BLUE_GREEN, SColor.DARK_GREEN, "tall grass", false, 0.2f),
    GRASS_FOLIAGE('♈', SColor.AMBER, SColor.DARK_GREEN, "foliage", false, 0.5f),

    WALL('#', SColor.DARK_GRAY, SColor.LIGHT_GRAY, "wall", true, 1f),
    WINDOW('#', SColor.ALICE_BLUE, SColor.LIGHT_BLUE, "window", true, 0.25f),

    WATER_SHALLOW('.', SColor.AZUL, SColor.DARK_BLUE, "shallow water", false, 0f),
    WATER_DEEP('≈', SColor.AZUL, SColor.NAVY_BLUE, "deep water", false, 0f),

    LAVA_SHALLOW('~', SColor.WHITE, SColor.CORAL_RED, "shallow lava", false, 0f),
    LAVA_DEEP('≈', SColor.WHITE, SColor.DARK_RED, "deep lava", false, 0f),

    TREE('t', SColor.GREENFINCH, SColor.BROWN, "tree", true, 0.3f),
    EVERGREEN('₤', SColor.FOREST_GREEN, SColor.GREEN_BAMBOO, "evergreen", true, 0.7f),

    DOOR_CLOSED('+', SColor.WHITE, SColor.BROWN, "closed door", true, 1f),
    DOOR_OPEN('/', SColor.WHITE, SColor.BROWN, "open door", false, 0f),

    STAIRS_UP('<', SColor.WHITE, SColor.BLACK, "upward stairs", false, 0.8f),
    STAIRS_DOWN('>', SColor.WHITE, SColor.BLACK, "downward stairs", false, 0.8f),

    GAME_WIN('_', SColor.GREEN, SColor.DARK_GREEN, "Escape", false, 0f);

    public final boolean blocks;
    public final SColor color;
    public final SColor background;
    public final char character;
    public final float opacity;
    public final String displayName;

    public static TileType[] lookup = TileType.values();

    TileType(char character, SColor color, SColor background, String name, boolean blocked, float opacity) {

        this.character = character;
        this.color = color;
        this.background = background;
        this.displayName = name;
        this.blocks = blocked;
        this.opacity = opacity;
    }


    public byte id() {
        return (byte) ordinal();
    }

    public static TileType[] grassTypes = {GRASS_SHORT, GRASS_MEDIUM, GRASS_TALL, GRASS_FOLIAGE};

}
