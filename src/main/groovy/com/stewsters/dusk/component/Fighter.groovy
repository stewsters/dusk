package com.stewsters.dusk.component

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.flyweight.Slot
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class Fighter {
    public Entity owner

    int baseMaxHp
    int hp      //can take more hits

    int baseMaxToxicity //maximum toxicity you can withstand
    int toxicity

    int baseMaxStamina
    int stamina // used to make attacks and sprint


    List<DamageType> resistances
    List<DamageType> weaknesses

    // Skills
    int skillMelee   //strength with melee weapons
    int skillEvasion //make it harder to hit - evasion
    int skillMarksman // attack bonus with ranged weapons

    int experience

    //  armor depends on equipment
    IntRange unarmedDamage
    List<DamageType> unarmedDamageTypes

    // Level
    // Experience


    Closure deathFunction

    public Fighter(params) {

        baseMaxHp = params.hp ?: 1
        hp = params.hp ?: baseMaxHp

        baseMaxStamina = params.stamina ?: 1
        stamina = baseMaxStamina

        baseMaxToxicity = params.toxicity ?: 1
        toxicity = 0

        skillEvasion = params.evasion ?: 0
        skillMelee = params.melee ?: 0
        skillMarksman = params.marksman ?: 0

        unarmedDamage = params.unarmedDamage ?: (0..0)
        unarmedDamageTypes = params.unarmedDamageTypes ?: [DamageType.BASH]

        resistances = params.resistances ?: []
        weaknesses = params.weaknesses ?: []

        deathFunction = params.deathFunction ?: null
    }

    public takeDamage(int damage, Entity attacker = null, List<DamageType> damageTypes = []) {

        int resistance = resistances.intersect(damageTypes).size()
        int weakness = weaknesses.intersect(damageTypes).size()

        damage *= Math.pow(1.5, weakness - resistance)

        if (damage > 0) {
            hp -= damage
            if (hp <= 0) {
                hp = 0
                if (deathFunction)
                    deathFunction(owner, attacker)
            }

            int range = Math.sqrt(damage)
            for (int i = 0; i < damage; i++) {
                // blood splatter
                int xPos = MatUtils.getIntInRange(-range, range) + owner.x
                int yPos = MatUtils.getIntInRange(-range, range) + owner.y
                owner.levelMap.ground[MatUtils.limit(xPos, 0, owner.levelMap.xSize - 1)][MatUtils.limit(yPos, 0, owner.levelMap.ySize - 1)].gore = true
            }

        }
    }

    public def addHealth(int amount) {
        hp = Math.min(amount + hp, maxHP);
    }

    public def addStamina(int amount) {
        stamina = Math.min(amount + stamina, maxStamina)
    }

    public def addToxicity(int amount) {
        toxicity = Math.min(amount + toxicity, maxToxicity)
        if (toxicity >= maxToxicity) {
//            DeathFunctions.zombify(owner)
        }
    }

    public void attack(Entity target) {

        int attackRoll = MatUtils.d(20) + accuracy
        int evasionRoll = MatUtils.d(20) + target.fighter.evasion

        if (attackRoll >= evasionRoll) {
            //figure out damage


            Equipment equipment = owner.inventory?.getEquippedInSlot(Slot.PRIMARY_HAND)
            IntRange damageRange
            List<DamageType> damageTypes

            if (equipment) {
                damageRange = equipment?.damage
                damageTypes = equipment?.damageTypes
            } else {
                damageRange = unarmedDamage
                damageTypes = unarmedDamageTypes
            }

            int damage = MatUtils.getIntInRange(damageRange.from, damageRange.to)

            target.inventory?.getAllEquippedEquipment().each {
                if (it.armor)
                    damage -= MatUtils.getIntInRange(it.armor.from, it.armor.to)
            }


            if (damage > 0) {
                MessageLog.send "${owner.name} attacks ${target.name} for ${damage} hit points.", SColor.WHITE, [owner, target]
                target.fighter.takeDamage(damage, owner, damageTypes)
                //other effects?
                // if (owner.faction == Faction.EVIL) {
                //   target.fighter.infect(1)
                // }

            } else {
                MessageLog.send "${owner.name}'s attack deflects off ${target.name}.", SColor.WHITE, [owner, target]
            }
        } else {
            MessageLog.send "${owner.name} attacks ${target.name} but it has no effect!", SColor.WHITE, [owner, target]
        }
    }


    public int levelUp() {
        skillMelee++
        skillEvasion++
        skillMarksman++
        hp += 5
    }

    /* Equipment functions */

    public int getMaxHP() {
        int bonus = 0
        if (owner.inventory) {
            bonus += owner.inventory.getAllEquippedEquipment()?.bonusMaxHp.sum() ?: 0
        }
        return baseMaxHp + bonus
    }

    public int getMaxToxicity() {
        int bonus = 0
        if (owner.inventory) {
            bonus += owner.inventory.getAllEquippedEquipment().bonusMaxToxicity.sum() ?: 0
        }
        return baseMaxToxicity + bonus
    }

    public int getMaxStamina() {
        int bonus = 0
        if (owner.inventory) {
            bonus += owner.inventory.getAllEquippedEquipment().bonusMaxStamina.sum() ?: 0
        }
        return baseMaxStamina + bonus
    }


    public int getAccuracy() {
        int bonus = 0
        if (owner.inventory) {
            bonus += owner.inventory.getAllEquippedEquipment().accuracyModifier.sum() ?: 0
        }
        return skillMelee + bonus
    }

    public int getEvasion() {
        int bonus = 0
        if (owner.inventory) {
            bonus += owner.inventory.getAllEquippedEquipment().evasionModifier.sum() ?: 0
        }
        return skillEvasion + bonus
    }

    public int getMarksman() {
//        int bonus = 0
//        if (owner.inventory) {
//            bonus += owner.inventory.getAllEquippedEquipment().bonusMarksman.sum() ?: 0
//        }
        return skillMarksman
    }

}
