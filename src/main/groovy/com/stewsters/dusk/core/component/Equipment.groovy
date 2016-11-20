package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.DamageType
import com.stewsters.dusk.core.flyweight.Slot
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import squidpony.squidcolor.SColor

class Equipment {

    Entity entity
    Slot slot
    boolean isEquipped = false

    //    int strengthReq // A soft requirement, but the item is less useful without

    int accuracyModifier // to hit modifier from this equipment
    IntRange damage

    int evasionModifier // evasion bonus or penalty
    IntRange armor

    List<DamageType> damageTypes

    //TODO: magic, identification

    public Equipment(Map params) {
        slot = params?.slot

//        strengthReq = params?.strengthReq ?: 0

        accuracyModifier = params?.accuracyModifier ?: 0
        damage = params?.damage ?: null

        evasionModifier = params?.evasionModifier ?: 0
        armor = params?.armor ?: null

        damageTypes = params?.damageTypes ?: []
    }

    public void toggleEquip(Entity holder) {
        if (isEquipped)
            dequip(holder)
        else
            equip(holder)
    }


    void equip(Entity holder) {

        Equipment oldEquipment = holder.inventory.getEquippedInSlot(slot)
        if (oldEquipment)
            oldEquipment.dequip(holder)

        isEquipped = true
        MessageLogSystem.send("Equipped ${entity.name}", SColor.WHITE, [holder])
    }

    void dequip(Entity holder) {
        if (isEquipped) {
            isEquipped = false
            MessageLogSystem.send("Dequipped ${entity.name} from ${slot.name}", SColor.WHITE, [holder])
        }

    }
}
