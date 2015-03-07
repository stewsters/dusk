package com.stewsters.dusk.map.chunk;

public class Chunk {

    public static final int chunkSize = 64;

    public final long lowCornerX;
    public final long lowCornerY;
    public final byte[][] data;

    public Chunk(long lowCornerX, long lowCornerY, byte[][] data) {
        this.lowCornerX = lowCornerX;
        this.lowCornerY = lowCornerY;
        this.data = data;
    }


}
