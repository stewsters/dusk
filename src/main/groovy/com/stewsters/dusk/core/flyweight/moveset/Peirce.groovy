package com.stewsters.dusk.core.flyweight.moveset

import com.stewsters.util.math.Point2i

/**
 * Attacks the target and the one behind them
 *
 * Spears
 * */
class Peirce implements Moveset {

    @Override
    List<Point2i> getTriggerArea(int x, int y, int dx, int dy) {
        return [new Point2i(x + dx, y + dy), new Point2i(x + dx + dx, y + dy + dy)]
    }

    @Override
    List<Point2i> getAttackArea(int x, int y, int dx, int dy) {
        return [new Point2i(x + dx, y + dy), new Point2i(x + dx + dx, y + dy + dy)]
    }

}
