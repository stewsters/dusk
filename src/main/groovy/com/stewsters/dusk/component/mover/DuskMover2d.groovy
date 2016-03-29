package com.stewsters.dusk.component.mover

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.map.LevelMap
import com.stewsters.util.pathing.twoDimention.shared.Mover2d

public class DuskMover2d implements Mover2d {

    private Entity owner
    private LevelMap map
    private int xSize
    int ySize

    public DuskMover2d(LevelMap map) {
        this.map = map
        xSize = 1
        ySize = 1
    }

    public DuskMover2d(LevelMap map, int xSize, int ySize) {
        this.map = map
        this.xSize = xSize
        this.ySize = ySize
    }

    public void setOwner(Entity owner){
        this.owner=owner
    }

    public void setMap(LevelMap map) {
        this.map = map
    }

    @Override
    public boolean canTraverse(int sx, int sy, int tx, int ty) {
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                if (map.isBlocked(tx + x, ty + y,owner)) {
                    return false
                }
            }
        }
        return true
    }

    @Override
    public boolean canOccupy(int tx, int ty) {
        return !map.isBlocked(tx, ty)
    }

    @Override
    public float getCost(int sx, int sy, int tx, int ty) {
        return 1
    }
}