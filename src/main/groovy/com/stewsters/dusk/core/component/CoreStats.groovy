package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity

class CoreStats {
    public Entity entity

    int strength
    int life
    int stamina

    CoreStats(int strength = 10, int life = 10, int stamina = 10) {
        this.strength = strength
        this.life = life
        this.stamina = stamina
    }

}
