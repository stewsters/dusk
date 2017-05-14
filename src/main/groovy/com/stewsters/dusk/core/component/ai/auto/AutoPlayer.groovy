package com.stewsters.dusk.core.component.ai.auto

import com.stewsters.dusk.core.component.Item
import com.stewsters.dusk.core.component.ai.Ai
import com.stewsters.dusk.core.component.ai.BaseAi
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.math.MatUtils
import com.stewsters.util.pathing.twoDimention.pathfinder.AStarPathFinder2d
import com.stewsters.util.pathing.twoDimention.searcher.DjikstraSearcher2d
import com.stewsters.util.pathing.twoDimention.shared.FullPath2d
import com.stewsters.util.pathing.twoDimention.shared.PathNode2d
import com.stewsters.util.planner.Action
import com.stewsters.util.planner.Planner
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

//        List<Action<AutoPlayWorld>> actions = Arrays.asList(
//                new Action<>(
//                        "Wait",
//                        { AutoPlayWorld w ->
//                            return w;
//                        },
//                        { AutoPlayWorld w ->
//
//                            return w;
//                        }),
//                new Action<>(
//                        "Attack",
//                        { AutoPlayWorld w ->
//                            return w.visableOpponents;
//                        },
//                        { AutoPlayWorld w ->
//
//                            w.health -= 1
//
//
//                            return w;
//                        }),
//                new Action<>(
//                        "GrabItem",
//                        { AutoPlayWorld w ->
//                            return w;
//                        },
//                        { AutoPlayWorld w ->
//
//                            return w;
//                        }),
//                new Action<>(
//                        "Explore",
//                        { AutoPlayWorld w ->
//                            return w;
//                        },
//                        { AutoPlayWorld w ->
//
//                            return w;
//                        }),
//        )

//        Planner<AutoPlayWorld> planner = new Planner<>()
//        planner.plan(new AutoPlayWorld(entity),
//
//        )

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
                FullPath2d path = pathFinder2d.findPath(entity.mover, entity.x, entity.y, enemy.x, enemy.y)

                if (path) {
                    def step = path.getStep(1)
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
            FullPath2d path = searcher2d.search(entity.mover, entity.x, entity.y, { PathNode2d current -> !entity.levelMap.ground[current.x][current.y].isExplored })
            if (path && path.length > 1) {
                FullPath2d.Step step = path.getStep(1)
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
