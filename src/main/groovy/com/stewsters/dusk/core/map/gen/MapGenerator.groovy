package com.stewsters.dusk.core.map.gen

import com.stewsters.dusk.core.map.LevelMap


public interface MapGenerator {

    public LevelMap reGenerate(int level)

    public int getPlayerStartX()

    public int getPlayerStartY()
}
