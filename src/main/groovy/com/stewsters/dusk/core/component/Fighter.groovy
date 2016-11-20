package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.DamageType
import com.stewsters.dusk.core.flyweight.GroundCover
import com.stewsters.dusk.core.flyweight.Slot
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class Fighter {
    public Entity entity

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

    Closure deathFunction
    // TODO: Level, Experience

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

    public int takeDamage(int damage, Entity attacker = null, List<DamageType> damageTypes = []) {

        int resistance = resistances.intersect(damageTypes).size()
        int weakness = weaknesses.intersect(damageTypes).size()

        damage *= Math.pow(1.5, weakness - resistance)

        if (damage > 0) {
            hp -= damage
            if (hp <= 0) {
                hp = 0
                if (deathFunction)
                    deathFunction(entity, attacker)
            }

            int range = Math.sqrt(damage)
            for (int i = 0; i < damage; i++) {
                // blood splatter
                int xPos = MatUtils.getIntInRange(-range, range) + entity.x
                int yPos = MatUtils.getIntInRange(-range, range) + entity.y
                entity.levelMap.ground[MatUtils.limit(xPos, 0, entity.levelMap.xSize - 1)][MatUtils.limit(yPos, 0, entity.levelMap.ySize - 1)].groundCover = GroundCover.BLOOD
            }

        }
        return damage
    }

    public def addHealth(int amount) {
        hp = Math.min(amount + hp, maxHP);
    }

    public def addStamina(int amount) {
        stamina = Math.min(amount + stamina, maxStamina)
    }

    public def addToxicity(int amount) {
        toxicity = Math.min(amount + toxicity, maxToxicity)
    }

    public void attack(Entity target) {

        int attackRoll = MatUtils.d(20) + accuracy
        int evasionRoll = MatUtils.d(20) + target.fighter.evasion

        if (attackRoll >= evasionRoll) {
            //figure out damage


            Equipment equipment = entity.inventory?.getEquippedInSlot(Slot.PRIMARY_HAND)
            IntRange damageRange
            List<DamageType> damageTypes

            if (equipment) {
                damageRange = equipment?.damage ?: 0..0
                damageTypes = equipment?.damageTypes
            } else {
                damageRange = unarmedDamage
                damageTypes = unarmedDamageTypes
            }

            int damage = MatUtils.getIntInRange(damageRange.from, damageRange.to)

            target.inventory?.getAllEquippedEquipment()?.each {
                if (it.armor)
                    damage -= MatUtils.getIntInRange(it.armor.from, it.armor.to)
            }


            if (damage > 0) {

                String targetName = target.name
                int actualDamage = target.fighter.takeDamage(damage, entity, damageTypes)

                if (target.fighter && target.fighter.hp > 0) {
                    MessageLogSystem.send "${entity.name} attacks ${targetName} for ${actualDamage} damage.", SColor.WHITE, [entity, target]
                } else {
                    MessageLogSystem.send "${targetName} has been slain by ${entity.name}", SColor.WHITE, [entity, target]
                }

            } else {
                MessageLogSystem.send "${entity.name}'s attack deflects off ${target.name}.", SColor.WHITE, [entity, target]
            }
        } else {
            MessageLogSystem.send "${entity.name} attacks ${target.name} but it has no effect!", SColor.WHITE, [entity, target]
        }
    }

    /* Equipment functions */

    public int getMaxHP() {
//        int bonus = 0
//        if (entity.inventory) {
//            bonus += entity.inventory.getAllEquippedEquipment()?.bonusMaxHp.sum() ?: 0
//        }
        return baseMaxHp //+ bonus
    }

    public int getMaxToxicity() {
//        int bonus = 0
//        if (entity.inventory) {
//            bonus += entity.inventory.getAllEquippedEquipment().bonusMaxToxicity.sum() ?: 0
//        }
        return baseMaxToxicity //+ bonus
    }

    public int getMaxStamina() {
//        int bonus = 0
//        if (entity.inventory) {
//            bonus += entity.inventory.getAllEquippedEquipment().bonusMaxStamina.sum() ?: 0
//        }
        return baseMaxStamina //+ bonus
    }


    public int getAccuracy() {
//        int bonus = 0
//        if (entity.inventory) {
//            bonus += entity.inventory.getAllEquippedEquipment().accuracyModifier.sum() ?: 0
//        }
        return skillMelee //+ bonus
    }

    public int getEvasion() {
//        int bonus = 0
//        if (entity.inventory) {
//            bonus += entity.inventory.getAllEquippedEquipment().evasionModifier.sum() ?: 0
//        }
        return skillEvasion //+ bonus
    }

    public int getMarksman() {
//        int bonus = 0
//        if (entity.inventory) {
//            bonus += entity.inventory.getAllEquippedEquipment().bonusMarksman.sum() ?: 0
//        }
        return skillMarksman
    }

}
