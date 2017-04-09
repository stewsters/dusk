package com.stewsters.dusk.core.map.gen

import com.stewsters.dusk.core.map.LevelMap
import groovy.transform.CompileStatic

@CompileStatic
interface MapGenerator {

    LevelMap reGenerate(int x, int y, int z)

    int getPlayerStartX()

    int getPlayerStartY()
}
