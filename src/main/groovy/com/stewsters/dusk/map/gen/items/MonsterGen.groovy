package com.stewsters.dusk.map.gen.items

import com.stewsters.dusk.component.Equipment
import com.stewsters.dusk.component.Fighter
import com.stewsters.dusk.component.Item
import com.stewsters.dusk.component.ai.BasicOpponent
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.flyweight.Slot
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.sfx.DeathFunctions
import com.stewsters.dusk.sfx.ItemFunctions
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class MonsterGen {

    public init() {

    }


    private static final List<Map> spawnPerLevel = [
            [name: "Goblin", rarity: 20, startLevel: 0, endLevel: 9],
            [name: "Orc", rarity: 20, startLevel: 2, endLevel: 4],
            [name: "Troll", rarity: 10, startLevel: 3, endLevel: 6]

    ]


    public static Entity getRandomMonsterByLevel(LevelMap map, int x, int y, int level) {

        Map<String, Integer> spawnChances = [:]

        spawnPerLevel.findAll { it.startLevel <= level && it.endLevel >= level }.each {
            spawnChances[it.name] = it.rarity
        }

        return createFromName(map, x, y, MatUtils.getChoice(spawnChances))
    }

    public static Entity createFromName(LevelMap map, int x, int y, String name) {
        switch (name) {


            case ("Goblin"):

                return new Entity(map: map, x: x, y: y,
                        ch: 'g', name: 'Goblin', color: SColor.SEA_GREEN, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                        fighter: new Fighter(hp: 4, stamina: 4, melee: 0, evasion: 3,
                                unarmedDamage: (1..4),
                                deathFunction: DeathFunctions.opponentDeath)


                )
                break

            case ("Orc"):

                return new Entity(map: map, x: x, y: y,
                        ch: 'o', name: 'Orc', color: SColor.LAWN_GREEN, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                        fighter: new Fighter(hp: 10, stamina: 4, melee: 3, evasion: 2,
                                unarmedDamage: (1..6),
                                deathFunction: DeathFunctions.opponentDeath)
                )
                break

            case ("Troll"):

                new Entity(map: map, x: x, y: y,
                        ch: 'T', name: 'Troll', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                        fighter: new Fighter(hp: 20, stamina: 8, melee: 4, evasion: -2,
                                unarmedDamage: (4..8),
                                deathFunction: DeathFunctions.opponentDeath)
                )
                break

            default:
                println "Cannot find ${name}"
                assert false

        }

    }

}
