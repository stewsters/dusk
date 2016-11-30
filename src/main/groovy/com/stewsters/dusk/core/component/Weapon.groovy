package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.DamageType

class Weapon {

    Entity entity
    IntRange damage
    List<DamageType> damageTypes

    // TODO: moveset, special, SP
    int strengthReq

    public Weapon(Map params) {
        damage = params?.damage ?: null
        damageTypes = params?.damageTypes ?: []
        strengthReq = params?.strengthReq ?: 0
    }

}
