package com.stewsters.dusk.map.chunk;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class ChunkLoader {
    private final static Logger log = Logger.getLogger(ChunkLoader.class.getName());
    private final static String saveFile = "saves";
    private final static List<Chunk> chunkPool = new LinkedList<>();

    //TODO: runlength encoding
    public static Chunk loadChunk(long chunkX, long chunkY, WorldGenerator worldGenerator) {

        //load chunk from disk
        byte[][] data = new byte[Chunk.chunkSize][Chunk.chunkSize];

        String fileName = saveFile + File.separator + chunkX + ":" + chunkY + ".wut";

        try (FileInputStream fileInputStream = new FileInputStream(new File(fileName))) {

            byte[] bytes = IOUtils.toByteArray(fileInputStream);

            if (bytes.length != Chunk.chunkSize * Chunk.chunkSize) {
                log.severe("Wrong block size");
                assert false;
            }

            for (int x = 0; x < Chunk.chunkSize; x++) {
                for (int y = 0; y < Chunk.chunkSize; y++) {
                    data[x][y] = bytes[x * Chunk.chunkSize + y];
                }
            }

        } catch (FileNotFoundException e) {
            // it looks like we don't have the file.  lets make one!
            data = worldGenerator.generate(chunkX, chunkY);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Chunk(chunkX, chunkY, data);

    }

    /**
     * @param chunk The chunk to unload
     */
    public static void unloadChunk(Chunk chunk) {

        //TODO: use the worldgenerator to compress and save
        String fileName = saveFile + File.separator + chunk.lowCornerX + ":" + chunk.lowCornerY + ".wut";

        try (FileOutputStream fileInputStream = new FileOutputStream(new File(fileName))) {

            byte[] bytes = new byte[Chunk.chunkSize * Chunk.chunkSize];
            for (int x = 0; x < Chunk.chunkSize; x++) {
                for (int y = 0; y < Chunk.chunkSize; y++) {
                    bytes[x * Chunk.chunkSize + y] = chunk.data[x][y];
                }
            }
            IOUtils.write(bytes, fileInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getChunkKey(long chunkX, long chunkY) {
        return chunkX + " " + chunkY;
    }

}
