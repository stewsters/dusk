package com.stewsters.dusk.map

public class MapStack {

    public int currentLevel

    public LevelMap[] levelMaps


    public MapStack(int floors) {
        currentLevel = 0
        levelMaps = new LevelMap[floors]
    }


}
