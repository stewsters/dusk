package com.stewsters.dusk.map.gen.items

import com.stewsters.dusk.component.Fighter
import com.stewsters.dusk.component.ai.BasicOpponent
import com.stewsters.dusk.component.ai.ChargerAi
import com.stewsters.dusk.component.ai.KnightAi
import com.stewsters.dusk.component.ai.SkirmisherAi
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.DamageType
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.map.gen.name.KnightNameGen
import com.stewsters.dusk.sfx.DeathFunctions
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class MonsterGen {


    private static final List<Map> spawnPerLevel = [

            [name: "Rat", rarity: 40, startLevel: 0, endLevel: 3],
            [name: "Craven", rarity: 20, startLevel: 0, endLevel: 6],
            [name: "Goblin", rarity: 20, startLevel: 0, endLevel: 5],
            [name: "Wolf", rarity: 10, startLevel: 1, endLevel: 4],
            [name: "Orc", rarity: 20, startLevel: 2, endLevel: 4],
            [name: "Dark Elf", rarity: 20, startLevel: 3, endLevel: 6],
            [name: "Imprisoned Spirit", rarity: 5, startLevel: 5, endLevel: 7],
            [name: "Troll", rarity: 10, startLevel: 4, endLevel: 6],
            [name: "Vampire", rarity: 20, startLevel: 8, endLevel: 9],
            [name: "Minotaur", rarity: 20, startLevel: 4, endLevel: 9],
            [name: "Armored Hulk", rarity: 10, startLevel: 6, endLevel: 9]

            // Puddi
            // Scavenger -  ScavengerAI
            // Unseen - flee if injured
            // Bile Beast
            // Blink Bat- These are annoying
            // Zombie - slow
            // Minotaur - Charger
            // Fallen Beast
    ]


    public static Entity getRandomMonsterByLevel(LevelMap map, int x, int y, int level) {

        Map<String, Integer> spawnChances = [:]

        spawnPerLevel.findAll { it.startLevel <= level && it.endLevel >= level }.each { Map it ->
            spawnChances[it.name] = it.rarity
        }

        return createFromName(map, x, y, MatUtils.getChoice(spawnChances))
    }

    public static Entity createFromName(LevelMap map, int x, int y, String name) {
        switch (name) {

            case ("Rat"):

                return new Entity(map: map, x: x, y: y,
                        ch: 'r', name: 'Rat', color: SColor.AMETHYST, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                        fighter: new Fighter(hp: 2, stamina: 2, melee: 0, evasion: 2,
                                unarmedDamage: (1..2),
                                deathFunction: DeathFunctions.opponentDeath)
                )
                break

            case ("Craven"):

                return new Entity(map: map, x: x, y: y,
                        ch: 'c', name: 'Craven', color: SColor.TANGERINE, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new SkirmisherAi(),
                        fighter: new Fighter(hp: 6, stamina: 4, melee: 0, evasion: 3,
                                unarmedDamage: (1..4),
                                deathFunction: DeathFunctions.opponentDeath)
                )
                break

            case ("Goblin"):

                return new Entity(map: map, x: x, y: y,
                        ch: 'g', name: 'Goblin', color: SColor.SEA_GREEN, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new SkirmisherAi(),
                        fighter: new Fighter(hp: 4, stamina: 4, melee: 0, evasion: 3,
                                unarmedDamage: (1..4),
                                deathFunction: DeathFunctions.opponentDeath)
                )
                break

        //wolf
            case ("Wolf"):
                return new Entity(map: map, x: x, y: y,
                        ch: 'w', name: 'Wolf', color: SColor.BLUE_VIOLET, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(8),
                        fighter: new Fighter(hp: 4, stamina: 6, melee: 2, evasion: 3,
                                unarmedDamage: (2..6),
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

            case ("Minotaur"):

                return new Entity(map: map, x: x, y: y,
                        xSize: 2, ySize: 2,
                        ch: 'm', name: 'Minotaur', color: SColor.LAWN_GREEN, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new ChargerAi(),
                        fighter: new Fighter(hp: 20, stamina: 8, melee: 5, evasion: -2,
                                unarmedDamage: (3..8),
                                deathFunction: DeathFunctions.opponentDeath)
                )
                break

                break
            case ("Dark Elf"):

                return new Entity(map: map, x: x, y: y,
                        ch: 'e', name: 'Dark Elf', color: SColor.PURPLE_DYE, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                        fighter: new Fighter(hp: 10, stamina: 4, melee: 8, evasion: 3,
                                unarmedDamage: (1..6),
                                deathFunction: DeathFunctions.opponentDeath)
                )
                break

            case ("Imprisoned Spirit"):

                return new Entity(map: map, x: x, y: y,
                        ch: 'i', name: "Imprisoned Spirit", color: SColor.LIGHT_BLUE, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                        fighter: new Fighter(hp: 20, stamina: 8, melee: 4, evasion: 8,
                                unarmedDamage: (4..8),
                                deathFunction: DeathFunctions.opponentDeath,
                                resistances: [],
                                weaknesses: [DamageType.SILVER, DamageType.IRON]
                        )
                )
                break


            case ("Troll"):

                return new Entity(map: map, x: x, y: y,
                        xSize: 2, ySize: 2,
                        ch: 'T', name: 'Troll', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(12),
                        fighter: new Fighter(hp: 20, stamina: 8, melee: 4, evasion: -2,
                                unarmedDamage: (4..8),
                                deathFunction: DeathFunctions.opponentDeath,
                                weaknesses: [DamageType.FIRE]
                        )
                )
                break

            case ("Vampire"):

                return new Entity(map: map, x: x, y: y,
                        ch: 'v', name: 'Vampire', color: SColor.WHITE_MOUSE, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(),
                        fighter: new Fighter(hp: 20, stamina: 8, melee: 4, evasion: 8,
                                unarmedDamage: (10..16),
                                deathFunction: DeathFunctions.opponentDeath,
                                resistances: [DamageType.IRON, DamageType.SLASH, DamageType.BASH],
                                weaknesses: [DamageType.SILVER, DamageType.WOOD, DamageType.PIERCE]
                        )

                )
                break


            case ("Armored Hulk"):
                //TODO: slow, add armor

                return new Entity(map: map, x: x, y: y,
                        xSize: 2, ySize: 2,
                        ch: 'A', name: 'Armored Hulk', color: SColor.LIGHT_GRAY, blocks: true,
                        priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new BasicOpponent(15),
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
        List<DamageType> resistances = []
        List<DamageType> weaknesses = []

        int resistanceCount = level / 3
        int weaknessCount = 3 - resistanceCount

        resistanceCount.times {
            resistances.add(MatUtils.randVal(DamageType.values()))
        }

        weaknessCount.times {
            weaknesses.add(MatUtils.randVal(DamageType.values()))
        }
        resistances.unique()
        weaknesses.unique()

        String name = KnightNameGen.generate()
        println name

        return new Entity(map: map, x: x, y: y,
                ch: 'K', name: name, color: SColor.BURNT_ORANGE, blocks: true,
                priority: Priority.OPPONENT, faction: Faction.EVIL, ai: new KnightAi(),
                fighter: new Fighter(hp: 20 + 10 * level, stamina: 8 + level, melee: level + 2, evasion: level / 2,
                        weaknesses: weaknesses,
                        resistances: resistances,
                        unarmedDamage: (4..8),
                        deathFunction: DeathFunctions.bossDeath)
        )

    }

}
