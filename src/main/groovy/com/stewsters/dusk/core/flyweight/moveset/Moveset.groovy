package com.stewsters.dusk.core.flyweight.moveset

import com.stewsters.util.math.Point2i

interface Moveset {

    List<Point2i> getTriggerArea(int x, int y, int dx, int dy);
    List<Point2i> getAttackArea(int x, int y, int dx, int dy);

}
