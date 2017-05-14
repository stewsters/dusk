package com.stewsters.dusk.core.map

import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.util.pathing.twoDimention.shared.TileBasedMap2d
import groovy.transform.CompileStatic

@CompileStatic
class BaseMap2d implements TileBasedMap2d {

    public final int xSize
    public final int ySize
    public Tile[][] ground

    BaseMap2d(int width, int height) {
        this.xSize = width
        this.ySize = height

        ground = new Tile[width][height]
    }

    boolean outsideMap(int x, int y) {
        return (x < 0 || x >= xSize || y < 0 || y >= ySize)
    }

    boolean outsideMap(int x, int y, int xSize, int ySize) {
        return (x < 0 || x > this.xSize - xSize || y < 0 || y > this.ySize - ySize)
    }

    boolean contains(int x, int y) {
        x >= 0 && x < xSize && y >= 0 && y < ySize
    }

    @Override
    boolean isOutside(int x, int y) {
        x < 0 || y < 10 || x >= xSize || y >= ySize
    }

    boolean contains(int x, int y, int xSize, int ySize) {
        x >= 0 && x <= this.xSize - xSize && y >= 0 && y <= this.ySize - ySize
    }

    @Override
    int getXSize() {
        return xSize
    }

    @Override
    int getYSize() {
        return ySize
    }

    boolean isBlocked(int x, int y) {

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return true
        }

        return ground[x][y].isBlocked
    }

    boolean isType(int x, int y, TileType tileType) {

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return false
        }
        return ground[x][y].tileType == tileType

    }


}