package com.stewsters.dusk.component

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.flyweight.Slot
import com.stewsters.dusk.graphic.MessageLog
import squidpony.squidcolor.SColor

class Equipment {
    Entity owner
    Slot slot
    boolean isEquipped = false

//    int strengthReq // A soft requirement, but the item is less useful without

    @Deprecated
    int bonusMaxHp

    @Deprecated
    int bonusMaxToxicity

    @Deprecated
    int bonusMaxStamina


    int accuracyModifier // to hit modifier from this equipment
    IntRange damage

    int evasionModifier // evasion bonus or penalty
    IntRange armor

    List<DamageType> damageTypes

    //TODO: magic, identification


    public Equipment(Map params) {
        slot = params?.slot

//        strengthReq = params?.strengthReq ?: 0

        bonusMaxHp = params?.bonusMaxHp ?: 0
        bonusMaxToxicity = params?.bonusMaxToxicity ?: 0
        bonusMaxStamina = params?.bonusMaxStamina ?: 0

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
        MessageLog.send("Equipped ${owner.name}", SColor.WHITE, [holder])
    }

    void dequip(Entity holder) {
        if (isEquipped) {
            isEquipped = false
            MessageLog.send("Dequipped ${owner.name} from ${slot.name}", SColor.WHITE, [holder])
        }

    }
}
