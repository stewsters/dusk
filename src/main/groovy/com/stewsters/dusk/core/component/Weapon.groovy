package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.DamageType
import com.stewsters.dusk.core.flyweight.moveset.Bump
import com.stewsters.dusk.core.flyweight.moveset.Moveset

class Weapon {

    Entity entity
    IntRange damage
    List<DamageType> damageTypes

    // TODO: moveset, special, SP
    int strengthReq

    boolean SP
    boolean maxSP
    Moveset moveSet
    Moveset postMoveAttack

    Weapon(Map params) {
        damage = params?.damage ?: null
        damageTypes = params?.damageTypes ?: []
        strengthReq = params?.strengthReq ?: 0

        moveSet = params?.moveSet ?: new Bump()
        postMoveAttack = params?.postMoveAttack

        SP = 0;
        maxSP = params?.maxSP ?: 1
    }

}
