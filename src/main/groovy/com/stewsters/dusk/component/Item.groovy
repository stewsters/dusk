package com.stewsters.dusk.component

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
import squidpony.squidcolor.SColor

class Item {
    public Entity owner

    Closure useFunction
    boolean useOnPickup
    boolean autoPickup

    @Deprecated
    int minRange

    @Deprecated
    int maxRange

    public Item(params) {
        useFunction = params?.useFunction
        useOnPickup = params?.useOnPickup
        autoPickup = params?.autoPickup

        minRange = params?.minRange ?: 1
        maxRange = params?.minRange ?: 1
    }

    /**
     *
     * @return true if the item should be used up
     */
    public boolean useItem(Entity user) {

        if (owner.equipment) {
            owner.equipment.toggleEquip(user)
            return false
        } else if (useFunction) {
            return useFunction(user)
        } else {
            MessageLog.send("${owner.name} cannot be used.", SColor.RED, [user])
            return false
        }
    }

    public boolean useHeldItem(Entity user) {
        if (useFunction) {
            return useFunction(user)
        } else {
            MessageLog.send("${owner.name} cannot be used.", SColor.RED, [user])
            return false
        }
    }

}
