package com.stewsters.dusk.core.magic

import com.stewsters.dusk.core.entity.Entity
import groovy.transform.CompileStatic

@CompileStatic
public class Mapping implements Spell {

    public static final int HEAL_AMOUNT = 20

    public Mapping() {
        name = "Mapping"
        key = 'm' as char
    }

    @Override
    boolean cast(Entity caster) {

        for (int x = 0; x < caster.levelMap.getXSize(); x++) {
            for (int y = 0; y < caster.levelMap.getYSize(); y++) {
                if (!caster.levelMap.getTileType(x, y).blocks)
                    caster.levelMap.setExplored(x, y, true)
            }
        }
        return true

    }

    @Override
    public String getDescription() {
        //TODO: make the map distance/vision effected by level
        "Maps the current dungeon level"
    }

}
