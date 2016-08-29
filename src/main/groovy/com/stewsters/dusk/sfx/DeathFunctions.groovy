package com.stewsters.dusk.sfx

import com.stewsters.dusk.component.ai.BasicOpponent
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.system.render.MessageLogSystem
import squidpony.squidcolor.SColor

class DeathFunctions {


    public static Closure playerDeath = { Entity owner, Entity attacker ->

        MessageLogSystem.send("${owner.name} is dead. Press space to continue.", SColor.RED, [owner])
        owner.ch = '%'
        owner.color = SColor.BLOOD_RED
        owner.faction = null

        // Rather than dropping everything, we need to make the death screen that identifies everything

//        owner.priority = Priority.CORPSE
//        owner.faction = null
//        if (owner.inventory) {
//            owner.inventory.dump()
//        }
    }


    public static Closure opponentDeath = { Entity owner, Entity attacker ->
        MessageLogSystem.send("${owner.name} is dead!", SColor.RED, [owner])
        owner.ch = '%'
        owner.color = SColor.BLOOD_RED
        owner.blocks = false
        owner.fighter = null
        owner.levelMap.actors.remove(owner.ai)
        owner.ai.owner = null
        owner.ai = null
        owner.name = "Remains of ${owner.name}"
        owner.priority = Priority.CORPSE
        owner.faction = null
        if (owner.inventory)
            owner.inventory.dump()
    }


    public static Closure bossDeath = { Entity owner, Entity attacker ->
        MessageLogSystem.send("${attacker.name} has slain ${owner.name}.", SColor.RED, [owner])
        owner.ch = '%'
        owner.color = SColor.BLOOD_RED
        owner.blocks = false
        owner.fighter = null
        owner.levelMap.actors.remove(owner.ai)
        owner.ai.owner = null
        owner.ai = null
        owner.name = "Remains of ${owner.name}"
        owner.priority = Priority.CORPSE
        owner.faction = null
        if (owner.inventory)
            owner.inventory.dump()

        //TODO: give exp that can be used for leveling
        attacker.fighter.experience++
    }


    public static Closure zombify = { Entity owner, Entity attacker ->
        MessageLogSystem.send("${owner.name} is changing!", SColor.RED, [owner])
        owner.faction = Faction.EVIL
        owner.ch = 'z'
        owner.color = SColor.GREEN_BAMBOO
        owner.fighter.deathFunction = opponentDeath
        owner.fighter.skillMarksman = 0;
        owner.ai = new BasicOpponent()
        owner.ai.owner = owner
        owner.name = "Zombie of ${owner.name}"
        owner.priority = Priority.OPPONENT

        if (owner.inventory) {
            owner.inventory.dump()
            owner.inventory = null
        }
    }
}
