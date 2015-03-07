package com.stewsters.dusk.map

import com.stewsters.dusk.flyweight.TileType
import com.stewsters.util.pathing.twoDimention.shared.Mover2d
import com.stewsters.util.pathing.twoDimention.shared.PathNode2d
import com.stewsters.util.pathing.twoDimention.shared.TileBasedMap2d

public class BaseMap2d implements TileBasedMap2d {

    protected final int xSize;
    protected final int ySize;
    protected Tile[][] ground;

    public BaseMap2d(int width, int height) {
        this.xSize = width;
        this.ySize = height;

        ground = new Tile[width][height];

//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//
//                ground[x][y] = baseType;
//            }
//        }

    }

    public boolean outsideMap(int x, int y) {
        return (x < 0 || x >= ground.length || y < 0 || y >= ground[0].length)
    }

    @Override
    public int getWidthInTiles() {
        return xSize;
    }

    @Override
    public int getHeightInTiles() {
        return ySize;
    }

    @Override
    public void pathFinderVisited(int x, int y) {

    }

    @Override
    public boolean isBlocked(Mover2d mover, PathNode2d pathNode) {
        return ground[pathNode.x][pathNode.y].isBlocked;
    }

    @Override
    public boolean isBlocked(Mover2d mover, int x, int y) {

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return true;
        }

        if (ground[x][y].isBlocked) {
            return true;
        }

        return ground[x][y].isBlocked;
    }

    public boolean isBlocked(int x, int y) {

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return true;
        }

        if (ground[x][y].isBlocked) {
            return true;
        }

        return ground[x][y].isBlocked;
    }

    @Override
    public float getCost(Mover2d mover, int sx, int sy, int tx, int ty) {
        return 1;
    }

    public boolean isType(int x, int y, TileType tileType){

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return false;
        }
        return ground[x][y].tileType == tileType

    }
}