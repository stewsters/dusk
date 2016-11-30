package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.DamageType
import com.stewsters.dusk.core.flyweight.GroundCover
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class Fighter {
    public Entity entity

    int maxHP
    int hp

    int armor
    int moveSpeed

    int maxToxicity //maximum toxicity you can withstand
    int toxicity

//    int maxStamina
//    int stamina // used to make attacks and sprint

    List<DamageType> resistances
    List<DamageType> weaknesses

    //  armor depends on equipment
    IntRange unarmedDamage
    List<DamageType> unarmedDamageTypes

    Closure deathFunction

    public Fighter(params) {

        maxHP = params.hp ?: 1
        hp = params.hp ?: maxHP

//        maxStamina = params.stamina ?: 1
//        stamina = maxStamina
//
        maxToxicity = params.toxicity ?: 1
        toxicity = 0

        unarmedDamage = params.unarmedDamage ?: (0..0)
        unarmedDamageTypes = params.unarmedDamageTypes ?: [DamageType.BASH]

        resistances = params.resistances ?: []
        weaknesses = params.weaknesses ?: []

        deathFunction = params.deathFunction ?: null
    }

    public int takeDamage(int initialDamage, Entity attacker = null, List<DamageType> damageTypes = []) {

        int resistance = resistances.intersect(damageTypes).size()
        int weakness = weaknesses.intersect(damageTypes).size()

        int finalDamage = initialDamage * Math.pow(1.5, weakness - resistance)

        if (finalDamage > 0) {

            int damageToArmor = Math.min(armor, finalDamage)
            armor -= damageToArmor

            int damageToHp = finalDamage - damageToArmor
            hp -= damageToHp
            if (hp <= 0) {
                hp = 0
                if (deathFunction)
                    deathFunction(entity, attacker)
            }

            int range = Math.sqrt(damageToHp) - 1
            for (int i = 0; i < damageToHp; i++) {
                // blood splatter
                int xPos = MatUtils.getIntInRange(-range, range) + entity.x
                int yPos = MatUtils.getIntInRange(-range, range) + entity.y
                entity.levelMap.ground[MatUtils.limit(xPos, 0, entity.levelMap.xSize - 1)][MatUtils.limit(yPos, 0, entity.levelMap.ySize - 1)].groundCover = GroundCover.BLOOD
            }

        }
        return finalDamage
    }

    public def addHealth(int amount) {
        hp = Math.min(amount + hp, maxHP)
    }

    public void attack(Entity target) {

        /*
        int attackRoll = MatUtils.d(20) + accuracy
        int evasionRoll = MatUtils.d(20) + target.fighter.evasion

        if (attackRoll < evasionRoll) {

            MessageLogSystem.send "${entity.name} attacks ${target.name} but it has no effect!", SColor.WHITE, [entity, target]
            return
        }*/

        //figure out damage
        Weapon weapon = entity.inventory?.getEquippedWeapon()
        IntRange damageRange
        List<DamageType> damageTypes

        if (weapon) {
            damageRange = weapon?.damage ?: 0..0
            damageTypes = weapon?.damageTypes
        } else {
            damageRange = unarmedDamage
            damageTypes = unarmedDamageTypes
        }

        int damage = MatUtils.getIntInRange(damageRange.from, damageRange.to)

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

    }

    int getMaxArmor() {
        return entity.inventory?.allEquippedEquipment?.armor?.armor?.sum() ?: 0
    }

}
