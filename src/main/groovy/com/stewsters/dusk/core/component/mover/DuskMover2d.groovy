package com.stewsters.dusk.core.component.mover

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.pathing.twoDimention.shared.Mover2d

public class DuskMover2d implements Mover2d {

    private Entity owner

    public DuskMover2d(Entity owner) {
        this.owner = owner
    }

    @Override
    public boolean canTraverse(int sx, int sy, int tx, int ty) {
        for (int x = 0; x < owner.xSize; x++) {
            for (int y = 0; y < owner.ySize; y++) {
                if (owner.levelMap.isBlocked(tx + x, ty + y, owner)) {
                    return false
                }
            }
        }
        return true
    }

    @Override
    public boolean canOccupy(int tx, int ty) {
        return !owner.levelMap.isBlocked(tx, ty)
    }

    @Override
    public float getCost(int sx, int sy, int tx, int ty) {
        return 1
    }
}