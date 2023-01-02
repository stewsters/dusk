package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.component.Equipment
import com.stewsters.dusk.core.component.Item
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.Slot
import com.stewsters.util.math.MatUtils

class AdvancedStats extends BaseAi implements Ai {

    private float morale
    private float chargeProbability
    private float retreatProbability


    AdvancedStats() {
        morale = 0.5f
        chargeProbability = 0.5f
        retreatProbability = 0.5f
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

    AdvancedStats(Map params) {
        morale = params?.morale ?: 0.5f as float
        chargeProbability = params?.chargeProbability ?: 0.5f as float
        retreatProbability = params?.retreatProbability ?: 0.5f as float

    }

    // http://dillingers.com/blog/2014/05/10/roguelike-ai/
    boolean takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()
        int optimalRange = 1


        if (enemy) {
            int enemyDistance = entity.distanceTo(enemy)

            Equipment weapon
            Item item

            if (entity.inventory) {
                weapon = entity.inventory.getEquippedInSlot(Slot.PRIMARY_HAND)
                if (weapon) {
                    item = weapon.entity.item
//                    if (item) {
//                        optimalRange = (item.minRange + item.maxRange) / 2
//                    }
                }
            }

            //if we have a gun, and they are getting too close, shoot them
            if (entity.fighter && (((float) entity.fighter.hp / entity.fighter.maxHP) < morale)) {
//                //flee
                if (!entity.moveAway(enemy.x, enemy.y)) {
                    entity.moveTowardsAndAttack(enemy.x, enemy.y)
                }
            } else if (item && enemyDistance > optimalRange && canAttack(enemyDistance, item) && canMoveToward(enemy)) {

                if (MatUtils.getFloatInRange(0, 1) < chargeProbability) {
                    entity.moveTowardsAndAttack(enemy.x, enemy.y)
                } else {
                    attackTarget(enemy, item, enemyDistance)
                }
            } else if (item && enemyDistance < optimalRange && canAttack(enemyDistance, item) && canMoveAway(enemy)) {
                // to close
                if (MatUtils.getFloatInRange(0, 1) < retreatProbability) {
                    entity.moveAway(enemy.x, enemy.y)
                } else {
                    attackTarget(enemy, item, enemyDistance)
                }
            } else if (item && canAttack(enemyDistance, item)) {
                attackTarget(enemy, item, enemyDistance)

            } else if (enemyDistance > optimalRange && canMoveToward(enemy)) {
                entity.moveTowardsAndAttack(enemy.x, enemy.y)

            } else if (enemyDistance < optimalRange && canMoveAway(enemy)) {
                entity.moveAway(enemy.x, enemy.y)

            } else if (MatUtils.boolean) {
                entity.randomMovement()
            }


        } else if (entity.inventory) {

            //if we are standing on an item and we have room, pick it up
            if (entity.inventory.isFull()) {
                entity.randomMovement()
            } else {
                //find nearest visible item
                Entity item = entity.ai.findClosestVisibleItem()

                //if we are standing on it, pickUp
                if (item) {
                    if (item.x == entity.x && item.y == entity.y) {
                        entity.inventory.pickUp(item)
                    } else {
                        entity.moveTowardsAndAttack(item.x, item.y)
                    }
                } else {
                    entity.randomMovement()
                }

            }
        } else if (MatUtils.boolean) {
            entity.randomMovement()
        }
        gameTurn += speed

        return true
    }

    boolean canMoveAway(Entity target) {
        int dx = target.x - entity.x
        int dy = target.y - entity.y

        dx = MatUtils.limit(dx, -1, 1)
        dy = MatUtils.limit(dy, -1, 1)

        return entity.levelMap.isBlocked(entity.x + dx, entity.y + dy)
    }


    boolean canMoveToward(Entity target) {
        int dx = target.x - entity.x
        int dy = target.y - entity.y
        float distance = (float) Math.sqrt(dx**2 + dy**2)
        dx = (int) Math.round(dx / distance)
        dy = (int) Math.round(dy / distance)

        return entity.levelMap.isBlocked(entity.x + dx, entity.y + dy)
    }

    private static boolean canAttack(int targetRange, Item item) {
        //todo: los?  we select based on view, so it may not matter

//        if (item && item.minRange >= targetRange && item.maxRange <= targetRange) {
//            return true
//        }
        return targetRange <= 1 //melee
    }

    void attackTarget(Entity target, Item item, int distance) {

        if (item && item.useFunction != null) {
            item.useHeldItem(entity)
            println "AI Bang!"
        } else {
            entity.moveTowardsAndAttack(target.x, target.y)
        }
    }

}
