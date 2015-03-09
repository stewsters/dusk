package com.stewsters.dusk.map.gen.items

import com.stewsters.dusk.component.Fighter
import com.stewsters.dusk.component.ai.BasicOpponent
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.sfx.DeathFunctions
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class MonsterGen {


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

                return new Entity(map: map, x: x, y: y,
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


    public static Entity generateBossForLevel(LevelMap map, int x, int y, int level) {

        //early on they have more weaknesses, later they have more resistances
        Set<DamageType> resistances = []
        Set<DamageType> weaknesses = []

        int resistanceCount = level / 3
        int weaknessCount = 3 - resistanceCount

        resistanceCount.times {
            resistances.add(MatUtils.randVal(DamageType.values()))
        }

        weaknessCount.times {
            weaknesses.add(MatUtils.randVal(DamageType.values()))
        }

        String name = "Sir " + MatUtils.randVal(new File('assets/names/HUMAN/male.txt').text.split("\\s"))



        return new Entity(map: map, x: x, y: y,
                ch: 'K', name: name, color: SColor.BURNT_ORANGE, blocks: true,
                priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                fighter: new Fighter(hp: 20 + 10 * level, stamina: 8 + level, melee: level + 2, evasion: level / 2,
                        unarmedDamage: (4..8),
                        deathFunction: DeathFunctions.opponentDeath)
        )

    }

}
