package com.stewsters.dusk.core.map

public class MapStack {

    public int currentX
    public int currentY
    public int currentZ

    public final int xSize
    public final int ySize
    public final int zSize

    public LevelMap[][][] levelMaps


    public MapStack(int xSize, int ySize, int zSize) {
        this.xSize = xSize
        this.ySize = ySize
        this.zSize = zSize

        levelMaps = new LevelMap[xSize][ySize][zSize]

        currentX = 0
        currentY = 0
        currentZ = 0
    }


}
