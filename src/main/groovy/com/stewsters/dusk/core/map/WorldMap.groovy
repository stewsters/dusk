package com.stewsters.dusk.core.map

import groovy.transform.CompileStatic

@CompileStatic
class WorldMap {

    public int currentX
    public int currentY
    public int currentZ

    public final int xSize
    public final int ySize
    public final int zSize

    public final LevelMap[][][] levelMaps


    WorldMap(int xSize, int ySize, int zSize) {
        this.xSize = xSize
        this.ySize = ySize
        this.zSize = zSize

        levelMaps = new LevelMap[xSize][ySize][zSize]

        currentX = 0
        currentY = 0
        currentZ = 0
    }

    public LevelMap getLevelMapAt(int x, int y, int z) {
        return levelMaps[x][y][z]
    }

    public setLevelMap(LevelMap levelMap) {
        levelMaps[levelMap.chunkX][levelMap.chunkY][levelMap.chunkZ] = levelMap
    }

    boolean levelExists(int x, int y, int z) {
        if (x < 0 || x >= xSize || y < 0 || y >= ySize || z < 0 || z >= zSize)
            return false //outside

        levelMaps[x][y][z] != null
    }
}
