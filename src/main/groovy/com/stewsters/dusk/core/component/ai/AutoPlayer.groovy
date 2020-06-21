package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.component.Item
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import com.stewsters.util.pathing.twoDimention.heuristic.ChebyshevHeuristic2d
import com.stewsters.util.pathing.twoDimention.pathfinder.AStarPathFinder2d
import com.stewsters.util.pathing.twoDimention.searcher.DjikstraSearcher2d
import com.stewsters.util.pathing.twoDimention.shared.PathNode2d
import groovy.transform.CompileStatic

@CompileStatic
class AutoPlayer extends BaseAi implements Ai {

    private float morale
    private float chargeProbability
    private float retreatProbability

    AutoPlayer() {
        morale = 0.5f
        chargeProbability = 0.5f
        retreatProbability = 0.5f
        gameTurn = MatUtils.getIntInRange(0, speed)
    }

//    AutoPlayer(Map params) {
//        morale = params?.morale ?: 0.5f
//        chargeProbability = params?.chargeProbability ?: 0.5f
//        retreatProbability = params?.retreatProbability ?: 0.5f
//
//    }

    // http://dillingers.com/blog/2014/05/10/roguelike-ai/
    boolean takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()
        Entity nearestItem = findClosestVisibleItem()

        if (enemy) {
            println "Enemy"
            int enemyDistance = entity.distanceTo(enemy)

            if (enemyDistance <= 1) {
                entity.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {
                AStarPathFinder2d pathFinder2d = new AStarPathFinder2d(entity.levelMap, 1000)
                Optional<List<Point2i>> path = pathFinder2d.findPath(
                        { sx, sy, tx, ty -> !entity.levelMap.isBlocked(tx, ty,entity.xSize, entity.ySize, entity) },
                        { tx, ty -> !entity.levelMap.isBlocked(tx, ty,entity.xSize, entity.ySize, entity) },
                        { sx, sy, tx, ty -> 1.0 },
                        new ChebyshevHeuristic2d(),
                        true,
                        entity.x, entity.y, enemy.x, enemy.y)

                if (path.isPresent()) {
                    def step = path.get().get(1)
                    entity.moveTowardsAndAttack(step.x, step.y)
                } else {
                    entity.randomMovement()
                }
            }

//            if (entity.fighter && (((float) entity.fighter.hp / entity.fighter.maxHP) < morale)) {
//                if (!entity.moveAway(enemy.x, enemy.y)) {
//                    entity.moveTowardsAndAttack(enemy.x, enemy.y)
//                }
//
//            } else if (canMoveToward(enemy)) {
//                entity.moveTowardsAndAttack(enemy.x, enemy.y)
//
//            } else {//} if (canMoveAway(enemy)) {
//                entity.randomMovement()
//                //moveAway(enemy.x, enemy.y)
//
//            }

        } else if (entity.inventory && !entity.inventory.isFull() && nearestItem) {
            println "Grabbing item..."

            if (nearestItem.x == entity.x && nearestItem.y == entity.y) {
                entity.inventory.pickUp(nearestItem)
            } else {
                entity.moveTowardsAndAttack(nearestItem.x, nearestItem.y)
            }

        } else {
            println "Exploring..."
            DjikstraSearcher2d searcher2d = new DjikstraSearcher2d(entity.levelMap, 1000)
            Optional<List<Point2i>> path = searcher2d.search(
                    { PathNode2d current -> !entity.levelMap.ground[current.x][current.y].isExplored },
                    { int sx, int sy, int tx, int ty -> !entity.levelMap.isBlocked(tx, ty,entity.xSize, entity.ySize, entity) },
                    { sx, sy, tx, ty -> 1.0 },
                    true,
                    entity.x, entity.y,
            )
            if (path.isPresent()) {
                Point2i step = path.get().get(1)
                entity.moveTowardsAndAttack(step.x, step.y)
                println "Explored ${step.x}, ${step.y}"
            } else {
                println "No exploration left"
//                    entity.randomMovement()
            }
        }

//        else if (MatUtils.boolean) {
//            entity.randomMovement()
//        }
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


//    boolean canMoveToward(Entity target) {
//        float dx = target.x - entity.x
//        float dy = target.y - entity.y
//        float distance = (float) Math.sqrt((dx**2 + dy**2) as double)
//        dx = (int) Math.round(dx / distance)
//        dy = (int) Math.round(dy / distance)
//
//        return entity.levelMap.isBlocked(entity.x + dx, entity.y + dy)
//    }

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
