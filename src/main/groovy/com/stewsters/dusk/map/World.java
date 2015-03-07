package com.stewsters.dusk.map;


import com.stewsters.dusk.flyweight.TileType;
import com.stewsters.dusk.map.chunk.Chunk;
import com.stewsters.dusk.map.chunk.ChunkLoader;
import com.stewsters.dusk.map.chunk.WorldGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;


public class World {

    private static final Logger log = Logger.getLogger(World.class.getName());

    private static int maxLoadedChunks = 10;

    private HashMap<String, Chunk> loadedChunks;
    private WorldGenerator worldGenerator;

    public World() {
        loadedChunks = new HashMap<>();
        worldGenerator = new WorldGenerator();
    }

    public void update() {

    }

    private static int getPrecise(long globalCoord) {
        if (globalCoord >= 0) {
            return (int) (globalCoord % Chunk.chunkSize);
        } else {
            return (int) (globalCoord % Chunk.chunkSize) + Chunk.chunkSize - 1;
        }
    }

    private static long getChunkCoord(long globalCoord) {
        if (globalCoord >= 0) {
            return (globalCoord / Chunk.chunkSize);
        } else {
            return (globalCoord / Chunk.chunkSize) - 1;
        }
    }


    public TileType getTileType(long globalX, long globalY) {
        Chunk chunk = loadChunk(getChunkCoord(globalX), getChunkCoord(globalY));
        return TileType.values()[chunk.data[getPrecise(globalX)][getPrecise(globalY)]];
    }

    public void setTileType(int globalX, int globalY, TileType type) {
        Chunk chunk = loadChunk(getChunkCoord(globalX), getChunkCoord(globalY));
        chunk.data[getPrecise(globalX)][getPrecise(globalY)] = type.id();
    }

    private Chunk loadChunk(long chunkX, long chunkY) {

        if (loadedChunks.size() > maxLoadedChunks)
            flushOld(chunkX, chunkY);

        String key = ChunkLoader.getChunkKey(chunkX, chunkY);

        if (!loadedChunks.containsKey(key)) {
            Chunk chunk = ChunkLoader.loadChunk(chunkX, chunkY, worldGenerator);
            loadedChunks.put(key, chunk);
            return chunk;
        } else {
            return loadedChunks.get(key);
        }

    }

    public void flushOld(long globalX, long globalY) {
        log.info("Flushing Old");

        for (Iterator<Map.Entry<String, Chunk>> it = loadedChunks.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Chunk> entry = it.next();

            Chunk chunk = entry.getValue();
            if (Math.abs(chunk.lowCornerX - globalX) > 2 ||
                    Math.abs(chunk.lowCornerY - globalY) > 2) {

                log.info("Unloading: " + entry.getKey());
                it.remove();
                ChunkLoader.unloadChunk(chunk);


            }


        }
    }

    public void flush() {
        log.info("Unloading All Chunks");

        for (Chunk chunk : loadedChunks.values()) {
            ChunkLoader.unloadChunk(loadedChunks.get(chunk));
        }

        loadedChunks.clear();

    }

}
