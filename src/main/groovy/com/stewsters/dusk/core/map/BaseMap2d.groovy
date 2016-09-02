package com.stewsters.dusk.core.map

import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.util.pathing.twoDimention.shared.TileBasedMap2d
import groovy.transform.CompileStatic

@CompileStatic
public class BaseMap2d implements TileBasedMap2d {

    public final int xSize;
    public final int ySize;
    public Tile[][] ground;

    public BaseMap2d(int width, int height) {
        this.xSize = width;
        this.ySize = height;

        ground = new Tile[width][height];
    }

    public boolean outsideMap(int x, int y) {
        return (x < 0 || x >= ground.length || y < 0 || y >= ground[0].length)
    }

    @Override
    public int getXSize() {
        return xSize;
    }

    @Override
    public int getYSize() {
        return ySize;
    }

    @Override
    public void pathFinderVisited(int x, int y) {

    }

    public boolean isBlocked(int x, int y) {

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return true;
        }

        return ground[x][y].isBlocked;
    }

    public boolean isType(int x, int y, TileType tileType) {

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return false;
        }
        return ground[x][y].tileType == tileType

    }

    public boolean contains(int x, int y) {
        x >= 0 && x < xSize && y >= 0 && y < ySize
    }
}