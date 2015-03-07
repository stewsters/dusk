package com.stewsters.dusk.map.chunk;

import com.stewsters.dusk.flyweight.TileType;
import com.stewsters.util.noise.OpenSimplexNoise;

import java.util.logging.Logger;

public class WorldGenerator {

    private final static Logger log = Logger.getLogger(WorldGenerator.class.getName());
    OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();

    public byte[][] generate(long chunkX, long chunkY) {

        log.info("generating " + chunkX + " : " + chunkY);

        byte[][] result = new byte[Chunk.chunkSize][Chunk.chunkSize];
        short[][] height = new short[Chunk.chunkSize][Chunk.chunkSize];

        for (int x = 0; x < Chunk.chunkSize; x++) {
            for (int y = 0; y < Chunk.chunkSize; y++) {

                result[x][y] = openSimplexNoise.eval(
                        (chunkX * Chunk.chunkSize + x) / 10f,
                        (chunkY * Chunk.chunkSize + y) / 10f) > 0.5 ? TileType.EVERGREEN.id() : TileType.FLOOR_DIRT.id();

            }
        }
        return result;

    }

}
