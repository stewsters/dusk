package com.stewsters.dusk.core.map.gen

import com.stewsters.dusk.core.map.LevelMap
import groovy.transform.CompileStatic

@CompileStatic
interface MapGenerator {

    LevelMap reGenerate(int level)

    int getPlayerStartX()

    int getPlayerStartY()
}
