package com.stewsters.dusk.magic

import com.stewsters.dusk.component.ai.Ai
import com.stewsters.dusk.component.ai.Projectile
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.util.math.Point2i
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
public class Wrath implements Spell {

    public static final int WRATH_RANGE = 10

    public Wrath() {
        name = "Wrath"
        key = 'w'
    }

    @Override
    boolean cast(Entity caster) {

        Set<Entity> enemies = caster.ai.findAllVisibleEnemies(WRATH_RANGE)

        if (!enemies) {
            MessageLog.send("No enemy is within $WRATH_RANGE spaces.", SColor.RED, [caster])
            return false
        } else {
            enemies.each { Entity enemy ->

                float xSlope = enemy.x - caster.x
                float ySlope = enemy.y - caster.y
                float dist = Math.sqrt(xSlope * xSlope + ySlope * ySlope)
                int dx = enemy.x + xSlope / dist * WRATH_RANGE
                int dy = enemy.y + ySlope / dist * WRATH_RANGE


                Ai oldAI = enemy.ai
                enemy.levelMap.actors.remove(oldAI)
                enemy.ai = new Projectile(oldAI: oldAI, caster: caster,
                        target: new Point2i(dx, dy),
                        onImpact: { Entity user, int x, int y ->

                            MessageLog.send("The ${enemy.name} collides!", SColor.RED, [enemy, user])

                            user.levelMap.getEntitiesAtLocation(x, y).findAll { it.fighter }.each {
                                enemy.fighter.takeDamage(1, user, [DamageType.BASH])
                            }

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

    @Override
    public String getDescription() {
        "Throws enemies away from the castor."
    }

}
