package com.stewsters.dusk.core.magic

import com.stewsters.dusk.core.component.ai.Ai
import com.stewsters.dusk.core.component.ai.Projectile
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.DamageType
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import com.stewsters.util.math.Point2i
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
class Wrath implements Spell {

    public static final int WRATH_RANGE = 10

    Wrath() {
        name = "Wrath"
        key = 'w' as char
    }

    @Override
    boolean cast(Entity caster) {

        Set<Entity> enemies = caster.ai.findAllVisibleEnemies(WRATH_RANGE)

        if (!enemies) {
            MessageLogSystem.send("No enemy is within $WRATH_RANGE spaces.", SColor.RED, [caster])
            return false
        } else {
            enemies.each { Entity enemy ->

                float xSlope = enemy.x - caster.x
                float ySlope = enemy.y - caster.y
                float dist = (float) Math.sqrt(xSlope * xSlope + ySlope * ySlope)
                int dx = (int) (enemy.x + xSlope / dist * WRATH_RANGE)
                int dy = (int) (enemy.y + ySlope / dist * WRATH_RANGE)

                Ai oldAI = enemy.ai
                enemy.levelMap.actors.remove(oldAI)
                enemy.ai = new Projectile(oldAI: oldAI, caster: caster,
                        target: new Point2i(dx, dy),
                        onImpact: { Entity user, int x, int y ->

                            MessageLogSystem.send("The ${enemy.name} collides!", SColor.RED, [enemy, user])

                            user.levelMap.getEntitiesAtLocation(x, y).findAll { it.fighter }.each {
                                enemy.fighter.takeDamage(1, user, [DamageType.BASH])
                            }

                            return false
                        }

                )
                enemy.ai.entity = enemy
                enemy.levelMap.actors.add(enemy.ai)
                MessageLogSystem.send("${enemy.name} becomes confused.", SColor.LIGHT_BLUE)

            }
            return true
        }

    }

    @Override
    String getDescription() {
        "Throws enemies away from the castor."
    }

}
