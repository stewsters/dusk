package com.stewsters.dusk.core.map;

import com.stewsters.util.shadow.twoDimention.LitMap2d;

public class BaseLitMap2d extends BaseMap2d implements LitMap2d {

    public float[][] lightLevel;

    private int turnCounter;
    private int[][] turnLastUpdated;

    public BaseLitMap2d(int width, int height) {
        super(width, height);
        turnCounter = Integer.MIN_VALUE;


        turnLastUpdated = new int[width][height];
        lightLevel = new float[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                turnLastUpdated[x][y] = Integer.MIN_VALUE;
                lightLevel[x][y] = 0f;
            }
        }

    }

    @Override
    public void setLight(int x, int y, float force) {
        lightLevel[x][y] = force;
        turnLastUpdated[x][y] = turnCounter;
    }

    @Override
    public float getLight(int x, int y) {
        if (turnLastUpdated[x][y] == turnCounter)
            return lightLevel[x][y];
        else
            return 0;
    }

    @Override
    public float getResistance(int x, int y) {
        return ground[x][y].isBlocked ? 1 : 0;
    }

    @Override
    public void addLight(int x, int y, float bright) {
        if (turnCounter == turnLastUpdated[x][y]) {
            lightLevel[x][y] += bright;
        } else {
            turnLastUpdated[x][y] = turnCounter;
            lightLevel[x][y] = bright;
        }
    }

    public void incrementTurn() {

        if (turnCounter == Integer.MAX_VALUE) {
            turnCounter = Integer.MIN_VALUE;
        } else {
            turnCounter++;
        }
    }

    public float getOpacity(int x, int y) {
        ground[x][y].getOpacity()
    }
}