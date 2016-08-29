package com.stewsters.dusk.magic

import com.stewsters.dusk.component.ai.Projectile
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.system.render.MessageLogSystem
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
class Fireball implements Spell {

    public static final int FIREBALL_MIN_DAMAGE = 10
    public static final int FIREBALL_MAX_DAMAGE = 20

    public Fireball() {
        name = "Fireball"
        key = 'f' as char
    }


    @Override
    public boolean cast(Entity caster) {

        //TODO: create a fireball aimed at the target
//        List<Entity> targets = caster.ai.findVisibleEnemies(FIREBALL_RANGE + level)

        Entity enemy = caster.ai.findClosestVisibleEnemy()
        if (!enemy) {
            MessageLogSystem.send('No enemy is close enough to strike.', SColor.RED, [caster])
            return false
        } else {

            int dx = MatUtils.limit(enemy.x - caster.x, -1, 1)
            int dy = MatUtils.limit(enemy.y - caster.y, -1, 1)

            //TODO: create a fireball in the direction of the opponent.
            new Entity(map: caster.levelMap, x: caster.x + dx, y: caster.y + dy,
                    ch: '*', name: 'Fireball', color: SColor.BLOOD_RED, blocks: false, priority: Priority.OPPONENT,
                    ai: new Projectile(caster: caster, speed: 5, target: new Point2i(enemy.x, enemy.y),
                            onImpact: { Entity fireball, int x, int y ->

                                fireball.levelMap.getEntitiesBetween(x - 1, y - 1, x + 1, y + 1).each {

                                    if (it.fighter) {
                                        int damage = MatUtils.getIntInRange(FIREBALL_MIN_DAMAGE, FIREBALL_MAX_DAMAGE)
                                        int actualDamage = it.fighter.takeDamage(damage, caster, [DamageType.FIRE])

                                        MessageLogSystem.send("Flame envelopes ${it.name}! The damage is ${actualDamage} hit points.", SColor.LIGHT_BLUE, [caster, enemy])
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

    @Override
    public String getDescription() {
        "Fires a ball of fire at the nearest target for ${FIREBALL_MIN_DAMAGE} to ${FIREBALL_MAX_DAMAGE} damage."
    }


}
