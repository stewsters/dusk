package com.stewsters.dusk.core.sfx

import com.stewsters.dusk.core.component.ai.BasicOpponent
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.Faction
import com.stewsters.dusk.core.flyweight.Priority
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import squidpony.squidcolor.SColor

class DeathFunctions {


    public static Closure playerDeath = { Entity target, Entity attacker ->

        MessageLogSystem.send("${target.name} is dead. Press space to continue.", SColor.RED, [target])
        target.ch = '%'
        target.color = SColor.BLOOD_RED
        target.faction = null

        // Rather than dropping everything, we need to make the death screen that identifies everything

//        entity.priority = Priority.CORPSE
//        entity.faction = null
//        if (entity.inventory) {
//            entity.inventory.dump()
//        }
    }


    public static Closure opponentDeath = { Entity target, Entity attacker ->
        MessageLogSystem.send("${target.name} is dead!", SColor.RED, [target])
        target.ch = '%'
        target.color = SColor.BLOOD_RED
        target.blocks = false
        target.fighter = null
        target.levelMap.actors.remove(target.ai)
        target.ai.entity = null
        target.ai = null
        target.name = "Remains of ${target.name}"
        target.priority = Priority.CORPSE
        target.faction = null
        if (target.inventory)
            target.inventory.dump()
    }


    public static Closure bossDeath = { Entity target, Entity attacker ->
        MessageLogSystem.send("${attacker.name} has slain ${target.name}.", SColor.RED, [target])
        target.ch = '%'
        target.color = SColor.BLOOD_RED
        target.blocks = false
        target.fighter = null
        target.levelMap.actors.remove(target.ai)
        target.ai.entity = null
        target.ai = null
        target.name = "Remains of ${target.name}"
        target.priority = Priority.CORPSE
        target.faction = null
        if (target.inventory)
            target.inventory.dump()

        //TODO: give exp that can be used for leveling
        attacker.fighter.experience++
    }


    public static Closure zombify = { Entity target, Entity attacker ->
        MessageLogSystem.send("${target.name} is changing!", SColor.RED, [target])
        target.faction = Faction.EVIL
        target.ch = 'z'
        target.color = SColor.GREEN_BAMBOO
        target.fighter.deathFunction = opponentDeath
        target.fighter.skillMarksman = 0;
        target.ai = new BasicOpponent()
        target.ai.entity = target
        target.name = "Zombie of ${target.name}"
        target.priority = Priority.OPPONENT

        if (target.inventory) {
            target.inventory.dump()
            target.inventory = null
        }
    }
}
