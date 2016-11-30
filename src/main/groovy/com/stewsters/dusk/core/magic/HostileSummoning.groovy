package com.stewsters.dusk.core.magic

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.Faction
import com.stewsters.dusk.core.map.gen.items.MonsterGen
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic
import squidpony.squidgrid.util.Direction

@CompileStatic
class HostileSummoning implements Spell {


    HostileSummoning() {
        name = "Hostile Summoning"
        key = 'o' as char
    }

    @Override
    boolean cast(Entity caster) {

        List<Direction> directions = Direction.OUTWARDS as List
        Collections.shuffle(directions)

        Entity summon = MonsterGen.getRandomMonsterByLevel(caster.levelMap, caster.x, caster.y, MatUtils.getIntInRange(1, 9))

        for (Direction dir : directions) {
            int x = caster.x + dir.deltaX * summon.xSize
            int y = caster.y + dir.deltaY * summon.ySize

            if (summon.mover.canOccupy(x, y)) {
                summon.x = x
                summon.y = y
                summon.ai.gameTurn = caster.ai.gameTurn + 1
                summon.faction = Faction.EVIL

                caster.levelMap.update(summon)
                return true
            }
        }
        caster.levelMap.remove(summon)

        return false

    }

    @Override
    String getDescription() {
        "Summons an enemy monster."
    }

}
