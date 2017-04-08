package com.stewsters.dusk.core.flyweight.moveset

import com.stewsters.util.math.Point2i

/**
 * Attacks all adjacent targets
 * */
class Sweep implements Moveset {

    @Override
    List<Point2i> getTriggerArea(int x, int y, int dx, int dy) {
        return [new Point2i(x + dx, y + dy)]
    }

    @Override
    List<Point2i> getAttackArea(int x, int y, int dx, int dy) {
        return new Point2i(x, y).mooreNeighborhood().toList()
    }

}
