package com.stewsters.dusk.core.component.mover

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.pathing.twoDimention.shared.Mover2d
import groovy.transform.CompileStatic

@CompileStatic
class DuskMover2d implements Mover2d {

    private Entity entity

    DuskMover2d(Entity entity) {
        this.entity = entity
    }

    @Override
    boolean canTraverse(int sx, int sy, int tx, int ty) {
        for (int x = 0; x < entity.xSize; x++) {
            for (int y = 0; y < entity.ySize; y++) {
                if (entity.levelMap.isBlocked(tx + x, ty + y, entity)) {
                    return false
                }
            }
        }
        return true
    }

    @Override
    boolean canOccupy(int tx, int ty) {
        return !entity.levelMap.isBlocked(tx, ty)
    }

    @Override
    float getCost(int sx, int sy, int tx, int ty) {
        return 1
    }
}