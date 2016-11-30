package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import squidpony.squidcolor.SColor

class Item {
    public Entity entity

    Closure useFunction
    boolean useOnPickup
    boolean autoPickup
    int weight

    public Item(params) {
        useFunction = params?.useFunction
        useOnPickup = params?.useOnPickup
        autoPickup = params.autoPickup != false
        weight = params?.weight ?: 0
    }

    /**
     *
     * @return true if the item should be used up
     */
    public boolean useItem(Entity user) {

        if (entity.equipment) {
            entity.equipment.toggleEquip(user)
            return true
        } else if (useFunction) {
            return useFunction(user)
        } else {
            MessageLogSystem.send("${entity.name} cannot be used.", SColor.RED, [user])
            return false
        }
    }

    public boolean useHeldItem(Entity user) {
        if (useFunction) {
            return useFunction(user)
        } else {
            MessageLogSystem.send("${entity.name} cannot be used.", SColor.RED, [user])
            return false
        }
    }

}
