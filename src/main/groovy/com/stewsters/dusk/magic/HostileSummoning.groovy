package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.map.gen.items.MonsterGen
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic
import squidpony.squidgrid.util.Direction

@CompileStatic
public class HostileSummoning implements Spell {

    @Override
    boolean cast(Entity caster) {

        List<Direction> directions = Direction.OUTWARDS as List
        Collections.shuffle(directions)

        for (Direction dir : directions) {
            if (!caster.levelMap.isBlocked(caster.x + dir.deltaX, caster.y + dir.deltaY)) {
                def summon = MonsterGen.getRandomMonsterByLevel(caster.levelMap, caster.x + dir.deltaX, caster.y + dir.deltaY, MatUtils.getIntInRange(1, 9))

                summon.ai.gameTurn = caster.ai.gameTurn + 1
                summon.faction = Faction.EVIL
                return true
            }
        }
        return false

    }

    @Override
    public String getDescription() {
        "Summons an enemy monster."
    }

}
