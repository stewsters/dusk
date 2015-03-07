package com.stewsters.dusk.map.gen

import com.stewsters.dusk.map.LevelMap


public interface MapGenerator {

    public LevelMap reGenerate();

    public int getPlayerStartX();

    public int getPlayerStartY();
}
