package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity

class Armor {

    Entity entity
    int armor

    Armor(Map params) {
        armor = params?.armor ?: 0
    }

}