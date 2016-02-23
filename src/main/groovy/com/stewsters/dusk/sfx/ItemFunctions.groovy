package com.stewsters.dusk.sfx

import com.stewsters.dusk.component.ai.Ai
import com.stewsters.dusk.component.ai.ConfusedOpponent
import com.stewsters.dusk.component.ai.Projectile
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.AmmoType
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.dusk.map.gen.items.MonsterGen
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import squidpony.squidcolor.SColor
import squidpony.squidgrid.util.Direction

class ItemFunctions {

    public static final int HEAL_AMOUNT = 20

    public static Closure castHeal = { Entity user ->

        if (user.fighter.hp == user.fighter.maxHP) {
            MessageLog.send("You are already at full health.", SColor.RED, [user])
            return false
        } else {
            MessageLog.send("Your wounds seal up.", SColor.LIGHT_VIOLET)
            user.fighter.addHealth(HEAL_AMOUNT)

            return true
        }
    }

    public static final int BANDAGE_HEAL_AMOUNT = 6
    public static Closure bandage = { Entity user ->
        if (user.fighter.hp == user.fighter.maxHP) {
            MessageLog.send("You aren't bleeding.", SColor.RED, [user])
            return false
        } else {
            MessageLog.send("${user.name}'s wounds seal up.", SColor.LIGHT_VIOLET, [user])
            user.fighter.addHealth(BANDAGE_HEAL_AMOUNT)
            return true
        }
    }

    public static Closure castMapping = { Entity caster ->

        for (int x = 0; x < caster.levelMap.getXSize(); x++) {
            for (int y = 0; y < caster.levelMap.getYSize(); y++) {
                if (!caster.levelMap.getTileType(x, y).blocks)
                    caster.levelMap.setExplored(x, y, true)
            }
        }
        return true
    }


    public static final int FIREBALL_MIN_DAMAGE = 10
    public static final int FIREBALL_MAX_DAMAGE = 20

    public static Closure castFireball = { Entity user ->

        Entity enemy = user.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLog.send('No enemy is close enough to strike.', SColor.RED, [user])
            return false
        } else {

            int dx = MatUtils.limit(enemy.x - user.x, -1, 1)
            int dy = MatUtils.limit(enemy.y - user.y, -1, 1)

            //TODO: create a fireball in the direction of the opponent.
            new Entity(map: user.levelMap, x: user.x + dx, y: user.y + dy,
                    ch: '*', name: 'Fireball', color: SColor.BLOOD_RED, blocks: false, priority: Priority.OPPONENT,
                    ai: new Projectile(caster: user, target: new Point2i(enemy.x, enemy.y),
                            onImpact: { Entity caster, int x, int y ->

                                caster.levelMap.getEntitiesBetween(x - 1, y - 1, x + 1, y + 1).each {

                                    if (it.fighter) {
                                        int damage = MatUtils.getIntInRange(FIREBALL_MIN_DAMAGE, FIREBALL_MAX_DAMAGE)
                                        int actualDamage = it.fighter.takeDamage(damage, caster, [DamageType.FIRE])

                                        MessageLog.send("Flame envelopes ${it.name}! The damage is ${actualDamage} hit points.", SColor.LIGHT_BLUE, [user, enemy])
                                    }
                                }
                                //TODO: immolate on impact
                                return true
                            }
                    )
            )

            return true
        }
    }


    public static final int LIGHTNING_DAMAGE = 20
    public static final int LIGHTNING_RANGE = 5

    public static Closure castLightning = { Entity user ->

        Entity enemy = user.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLog.send('No enemy is visible to strike.', SColor.RED, [user])
            return false
        } else if (user.distanceTo(enemy) > LIGHTNING_RANGE) {
            MessageLog.send("${enemy.name} is too farther than $LIGHTNING_RANGE.", SColor.RED, [user])
            return false
        } else {
            int actualDamage = enemy.fighter.takeDamage(LIGHTNING_DAMAGE, user, [])
            MessageLog.send("A lightning bolt strikes the ${enemy.name} with a loud thunder for ${actualDamage} damage!", SColor.LIGHT_BLUE, [user, enemy])
            return true
        }
    }


    public static final int DOMINATION_RANGE = 3
    public static Closure castDomination = { Entity user ->
        Entity enemy = user.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLog.send('No enemy is close enough to dominate.', SColor.RED)
            return false
        } else if (user.distanceTo(enemy) > DOMINATION_RANGE) {
            MessageLog.send("${enemy.name} is too far to dominate.", SColor.RED, [user])
            return false
        } else {
            MessageLog.send("Dark magic takes over ${enemy.name}.", SColor.LIGHT_BLUE, [user, enemy])
            enemy.faction = user.faction
            return true
        }

    }

    public static final int CONFUSE_RANGE = 10
    public static final int CONFUSE_NUM_TURNS = 20

    public static Closure castConfuse = { Entity user ->

        Set<Entity> enemies = user.ai.findAllVisibleEnemies(CONFUSE_RANGE)

//        Entity enemy = user.ai.findClosestVisibleEnemy()
        if (!enemies) {
            MessageLog.send('No enemy is close enough to confused.', SColor.RED, [user])
            return false
        } else {

            int turns = (int) Math.ceil(CONFUSE_NUM_TURNS / enemies.size())

            enemies.each { Entity enemy ->
                Ai oldID = enemy.ai
                enemy.levelMap.actors.remove(oldID)
                enemy.ai = new ConfusedOpponent(oldAI: oldID, castor: user, numTurns: turns)
                enemy.ai.owner = enemy
                enemy.levelMap.actors.add(enemy.ai)

                MessageLog.send("${enemy.name} becomes confused.", SColor.LIGHT_BLUE)
            }

            return true
        }

    }


    public static final int WRATH_RANGE = 10

    public static Closure castWrath = { Entity user ->
        Set<Entity> enemies = user.ai.findAllVisibleEnemies(WRATH_RANGE)

        if (!enemies) {
            MessageLog.send("No enemy is within $WRATH_RANGE spaces.", SColor.RED, [user])
            return false
        } else {
            enemies.each { enemy ->

                float xSlope = enemy.x - user.x
                float ySlope = enemy.y - user.y
                float dist = Math.sqrt(xSlope * xSlope + ySlope * ySlope)
                int dx = enemy.x + xSlope / dist * WRATH_RANGE
                int dy = enemy.y + ySlope / dist * WRATH_RANGE


                Ai oldAI = enemy.ai
                enemy.levelMap.actors.remove(oldAI)
                enemy.ai = new Projectile(oldAI: oldAI, caster: user,
                        target: new Point2i(dx, dy),
                        onImpact: { Entity caster, int x, int y ->

                            MessageLog.send("The ${enemy.name} collides!", SColor.RED, [user, caster])

                            caster.levelMap.getEntitiesAtLocation(x, y).findAll { it.fighter }.each {
                                enemy.fighter.takeDamage(1, caster, [DamageType.BASH])
                            }
                            //TODO: immolate on impact
                            return false
                        }

                )
                enemy.ai.owner = enemy
                enemy.levelMap.actors.add(enemy.ai)
                MessageLog.send("${enemy.name} becomes confused.", SColor.LIGHT_BLUE)

            }
            return true
        }

    }


    public static Closure castSummoning = { Entity user ->

        def directions = Direction.OUTWARDS as List
        Collections.shuffle(directions)

        for (Direction dir : directions) {
            if (!user.levelMap.isBlocked(user.x + dir.deltaX, user.y + dir.deltaY)) {
                def summon = MonsterGen.getRandomMonsterByLevel(user.levelMap, user.x + dir.deltaX, user.y + dir.deltaY, MatUtils.getIntInRange(1, 9))

                summon.ai.gameTurn = user.ai.gameTurn + 1
                summon.faction = Faction.GOOD
                return true
            }
        }
        return false
    }

    public static Closure castHostileSummoning = { Entity user ->

        def directions = Direction.OUTWARDS as List
        Collections.shuffle(directions)

        for (Direction dir : directions) {
            if (!user.levelMap.isBlocked(user.x + dir.deltaX, user.y + dir.deltaY)) {
                def summon = MonsterGen.getRandomMonsterByLevel(user.levelMap, user.x + dir.deltaX, user.y + dir.deltaY, MatUtils.getIntInRange(1, 9))

                summon.ai.gameTurn = user.ai.gameTurn + 1
                summon.faction = Faction.EVIL
                return true
            }
        }
        return false
    }


    public static final int STONE_CURSE_RANGE = 10

    public static Closure castStoneCurse = { Entity user ->


        Entity enemy = user.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLog.send('No enemy is close enough to curse.', SColor.RED)
            return false
        } else if (user.distanceTo(enemy) > STONE_CURSE_RANGE) {
            MessageLog.send("${enemy.name} is too far to curse.", SColor.RED, [user])
            return false
        } else {
            MessageLog.send("${enemy.name} turns into stone.", SColor.BRIGHT_GREEN, [user, enemy])

            enemy.levelMap.remove(enemy)

            return new Entity(map: user.levelMap, x: enemy.x, y: enemy.y,
                    ch: 'S', name: "Statue of ${enemy.name}", color: SColor.WHITE_MOUSE, blocks: true,
                    priority: Priority.ITEM
            )

            return true
        }
    }


    public static Closure cleanse = { Entity user ->
        //removes toxicity from the system.
        user.fighter.toxicity = 0
        MessageLog.send("${user.name} is cleansed.", SColor.GREEN_BAMBOO, [user])
        return true
    }


    public static final int EAT_STAMINA_BOOST = 4
    public static Closure eat = { Entity user ->
        if (user.fighter.stamina == user.fighter.maxStamina) {
            MessageLog.send("${user.name} isn't hungry.", SColor.RED, [user])
            return false
        } else {
            MessageLog.send("${user.name} feasts on beef jerkey.", SColor.LIGHT_VIOLET, [user])
            user.fighter.addStamina(EAT_STAMINA_BOOST)
            return true
        }
    }

    public static Closure rifleAmmoBox = { Entity user ->
        int quantity = MatUtils.getIntInRange(12, 20)
        if (user.quiver) {
            user.quiver.addAmmo(AmmoType.rifle, quantity)
            MessageLog.send("${user.name} picked up ${quantity} rounds.")
            return true
        } else {
            MessageLog.send("${user.name} has no use for bullets.", SColor.RED, [user])
            return false
        }
    }
    public static Closure pistolAmmoBox = { Entity user ->
        int quantity = MatUtils.getIntInRange(12, 20)
        if (user.quiver) {
            user.quiver.addAmmo(AmmoType.pistol, quantity)
            MessageLog.send("${user.name} picked up ${quantity} rounds.")
            return true
        } else {
            MessageLog.send("${user.name} has no use for bullets.", SColor.RED, [user])
            return false
        }
    }
    public static Closure shotgunAmmoBox = { Entity user ->
        int quantity = MatUtils.getIntInRange(8, 18)
        if (user.quiver) {
            user.quiver.addAmmo(AmmoType.shotgun, quantity)
            MessageLog.send("${user.name} picked up ${quantity} shells.")
            return true
        } else {
            MessageLog.send("${user.name} has no use for shells.", SColor.RED, [user])
            return false
        }
    }
}
