package com.stewsters.dusk.core.map.gen.items

import com.stewsters.dusk.core.component.Armor
import com.stewsters.dusk.core.component.Equipment
import com.stewsters.dusk.core.component.Item
import com.stewsters.dusk.core.component.Weapon
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.DamageType
import com.stewsters.dusk.core.flyweight.Slot
import com.stewsters.dusk.core.flyweight.moveset.Bump
import com.stewsters.dusk.core.flyweight.moveset.Peirce
import com.stewsters.dusk.core.flyweight.moveset.Sweep
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.core.sfx.ItemFunctions
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class FantasyItemGen {

    //TODO: add the ability to choose to generate a category, ie weapon, armor, scroll,

    // PLAN: so you will have a chance to add certain items at certain levels.  whenever one appears, the
    // probability of another is cut by 1/2

    public static final Map<String, List<Map>> spawnPerLevel = [

            treasure: [
                    [name: "Gold", rarity: 20, startLevel: 0, endLevel: 9],
            ],

            armor   : [
                    [name: "Leather Coat", rarity: 20, startLevel: 0, endLevel: 3],
                    [name: "Chain Hauberk", rarity: 10, startLevel: 3, endLevel: 6],
                    [name: "Coat of Plates", rarity: 5, startLevel: 6, endLevel: 9],
            ],

            potions : [
                    [name: "Healing Potion", rarity: 10, startLevel: 0, endLevel: 9],
            ],

            scrolls : [
                    [name: "Scroll of Fireball", rarity: 30, startLevel: 0, endLevel: 9],
                    [name: "Scroll of Lightning", rarity: 20, startLevel: 0, endLevel: 9],
                    [name: "Scroll of Domination", rarity: 10, startLevel: 0, endLevel: 9],
                    [name: "Scroll of Confusion", rarity: 10, startLevel: 0, endLevel: 9],
                    [name: "Scroll of Wrath", rarity: 30, startLevel: 0, endLevel: 9],
                    [name: "Scroll of Mapping", rarity: 10, startLevel: 0, endLevel: 9],
                    [name: "Scroll of Summoning", rarity: 20, startLevel: 0, endLevel: 9],
                    [name: "Scroll of Testing", rarity: 0, startLevel: 0, endLevel: 9],
                    [name: "Scroll of Stone Curse", rarity: 5, startLevel: 5, endLevel: 9],
            ],

            weapons : [
                    // slash
                    [name: "Hand Axe", rarity: 10, startLevel: 0, endLevel: 3],
                    [name: "Arming Sword", rarity: 10, startLevel: 2, endLevel: 4],
                    [name: "Longsword", rarity: 10, startLevel: 3, endLevel: 9],

                    [name: "Reaper Blade", rarity: 10, startLevel: 1, endLevel: 5],

                    // pierce
                    [name: "Stiletto", rarity: 10, startLevel: 0, endLevel: 3],
                    [name: "Rapier", rarity: 10, startLevel: 1, endLevel: 5],
                    [name: "Spear", rarity: 10, startLevel: 2, endLevel: 4],
                    [name: "Halberd", rarity: 10, startLevel: 4, endLevel: 9],

                    // Blunt
                    [name: "Club", rarity: 10, startLevel: 0, endLevel: 4],
                    [name: "Mace", rarity: 10, startLevel: 3, endLevel: 6],
                    [name: "Maul", rarity: 10, startLevel: 6, endLevel: 9]
                    //warhammer
            ]
//            "Helm"                : 10,
//            "Boots"               : 10,
//            "Greaves"             : 10
//            "Quiver"              : 10
    ]


    static Entity getRandomItemByLevel(LevelMap map, int x, int y, int level) {

        Map<String, Integer> spawnChances = [:]

        spawnPerLevel.values().flatten().findAll { it.startLevel <= level && it.endLevel >= level }.each {
            spawnChances[it.name] = it.rarity
        }

        return createFromName(map, x, y, MatUtils.getChoice(spawnChances))
    }

    static Entity createFromName(LevelMap map, int x, int y, String name) {
        switch (name) {

            case ("Gold"):
                int gp = MatUtils.d(20)

                return new Entity(map: map, x: x, y: y,
                        ch: '$', name: "$gp Gold", color: SColor.GOLD,
                        itemComponent: new Item(autoPickup: true, useOnPickup: true, useFunction: { Entity user ->
                            if (user.purse) {
                                user.purse.gold += gp
                                return true
                            }
                            return false

                        })
                )
                break

            case ("Leather Coat"):

                return new Entity(map: map, x: x, y: y,
                        ch: '[', color: SColor.BROWN,
                        name: 'Leather Coat',
                        description: "A sturdy coat made from leather.",
                        itemComponent: new Item(weight: 2),
                        equipment: new Equipment(slot: Slot.CHEST),
                        armor: new Armor(
                                slot: Slot.CHEST,
                                armor: 2,
                        )
                )
                break
            case ("Chain Hauberk"):

                return new Entity(map: map, x: x, y: y,
                        ch: '[', color: SColor.SILVER_GREY,
                        name: 'Chain Hauberk',
                        description: "A shirt of mail reaching the thighs.",
                        itemComponent: new Item(weight: 4),
                        equipment: new Equipment(slot: Slot.CHEST),
                        armor: new Armor(
                                slot: Slot.CHEST,
                                armor: 4
                        )
                )
                break
            case "Coat of Plates":
                return new Entity(map: map, x: x, y: y,
                        ch: '[', color: SColor.SILVER,
                        name: 'Coat of Plates',
                        description: "Suit of armor made from metal plates.",
                        itemComponent: new Item(weight: 6),
                        equipment: new Equipment(slot: Slot.CHEST),
                        armor: new Armor(
                                slot: Slot.CHEST,
                                armor: 8
                        )
                )
                break

        // Potions
            case "Healing Potion":
                return new Entity(map: map, x: x, y: y,
                        ch: '!', color: SColor.AZURE,
                        name: 'Healing Potion',
                        description: "Restores health.",
                        itemComponent: new Item(useFunction: ItemFunctions.castHeal)
                )

        // Scrolls
            case "Scroll of Fireball":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.RED,
                        name: "Scroll of Fireball",
                        description: "Launches a fireball.",
                        itemComponent: new Item(useFunction: ItemFunctions.castFireball)
                )
                break
            case "Scroll of Lightning":

                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.ORANGE,
                        name: "Scroll of Lightning",
                        description: "Electrocutes the nearest target.",
                        itemComponent: new Item(useFunction: ItemFunctions.castLightning)
                )
                break
            case "Scroll of Domination":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.AMBER,
                        name: "Scroll of Domination",
                        description: "Makes the closest target your ally.",
                        itemComponent: new Item(useFunction: ItemFunctions.castDomination)
                )
                break
            case "Scroll of Confusion":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.AMARANTH,
                        name: "Scroll of Confusion",
                        description: "Confuses nearby targets",
                        itemComponent: new Item(useFunction: ItemFunctions.castConfuse)
                )
                break
            case "Scroll of Wrath":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.SILVER_GREY,
                        name: "Scroll of Wrath",
                        description: "Throws enemies away from you.",
                        itemComponent: new Item(useFunction: ItemFunctions.castWrath)
                )
                break

            case "Scroll of Mapping":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.ORANGE_PEEL,
                        name: "Scroll of Mapping",
                        description: "As you read the scroll, you begin to remember this area.",
                        itemComponent: new Item(useFunction: ItemFunctions.castMapping)
                )
                break

            case "Scroll of Summoning":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.YELLOW_GREEN,
                        name: "Scroll of Summoning",
                        description: "This scroll summons a monster in an adjacent square.",
                        itemComponent: new Item(useFunction: ItemFunctions.castSummoning)
                )
                break
            case "Scroll of Testing":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.YELLOW_GREEN,
                        name: "Scroll of Testing",
                        description: "This scroll summons a monster in an adjacent square.",
                        itemComponent: new Item(useFunction: ItemFunctions.castHostileSummoning)
                )
                break


            case "Scroll of Stone Curse":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.YELLOW_GREEN,
                        name: "Scroll of Stone Curse",
                        description: "This scroll turns a creature into a statue, blocking progress.",
                        itemComponent: new Item(useFunction: ItemFunctions.castStoneCurse)
                )
                break

        // Slash
            case "Hand Axe":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: "${material.name} Hand Axe",
                        description: "A one handed axe",
                        itemComponent: new Item(weight: 2),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (6..12),
                                damageTypes: [DamageType.SLASH, material],
                                strengthReq: 10
                        )
                )
                break
            case "Arming Sword":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.SILVER_GREY,
                        name: "${material.name} Arming Sword",
                        description: "A knightly sword.",
                        itemComponent: new Item(weight: 4),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (6..10),
                                damageTypes: [DamageType.SLASH, material],
                                strengthReq: 12
                        )
                )
                break
            case "Longsword":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER])

                return new Entity(map: map, x: x, y: y,
                        ch: '†', color: SColor.STEEL_BLUE,
                        name: "${material.name} Longsword",
                        description: "A long sword",
                        itemComponent: new Item(weight: 8),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (10..14),
                                damageTypes: [DamageType.SLASH, material],
                                moveSet: new Sweep(),
                                strengthReq: 16
                        )
                )
                break
            case "Reaper Blade":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.STEEL_BLUE,
                        name: "Reaper Blade",
                        description: "A cruel scythe",
                        itemComponent: new Item(weight: 8),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (10..14),
                                damageTypes: [DamageType.SLASH, DamageType.SILVER],
                                moveSet: new Sweep(),
                                strengthReq: 16
                        )
                )
                break

        // Pierce
            case "Stiletto":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: "${material.name} Stiletto",
                        description: "A small piercing dagger.",
                        itemComponent: new Item(weight: 2),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (3..5),
                                damageTypes: [DamageType.PIERCE, material],
                                strengthReq: 8
                        )
                )
                break
            case "Rapier":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: "${material.name} Rapier",
                        description: "A sharp peircing sword.",
                        itemComponent: new Item(weight: 2),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (5..7),
                                damageTypes: [DamageType.PIERCE, material],
                                moveSet: new Bump(),
                                postMoveAttack: new Bump(),
                                strengthReq: 8
                        )
                )
                break
            case "Spear":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER, DamageType.WOOD])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.GRAY,
                        name: "${material.name} Spear",
                        description: "A pointy stick. Classic.",
                        itemComponent: new Item(weight: 8),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (8..12),
                                damageTypes: [DamageType.PIERCE, material],
                                moveSet: new Peirce(),
                                strengthReq: 12
                        )
                )
                break
            case "Halberd":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER, DamageType.WOOD])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.GOLDEN,
                        name: "${material.name} Halberd",
                        description: "A halberd.",
                        itemComponent: new Item(weight: 8),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (10..14),
                                damageTypes: [DamageType.PIERCE, material],
                                strengthReq: 12
                        )
                )
                break

        // Bash
            case "Club":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.WOOD])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: "${material.name} Club",
                        description: "A solid club.",
                        itemComponent: new Item(weight: 8),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (4..8),
                                damageTypes: [DamageType.BASH, material],
                                strengthReq: 12
                        )
                )
                break
            case "Mace":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER, DamageType.WOOD])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: "${material.name} Mace",
                        description: "A weight on the end of a stick.",
                        itemComponent: new Item(weight: 8),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (6..16),
                                damageTypes: [DamageType.BASH, material],
                                strengthReq: 12
                        )
                )
                break

            case "Maul":
                DamageType material = MatUtils.rand([DamageType.IRON, DamageType.SILVER, DamageType.WOOD])

                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.RED_BEAN,
                        name: "${material.name} Maul",
                        description: "A great hammer",
                        itemComponent: new Item(weight: 20),
                        equipment: new Equipment(slot: Slot.PRIMARY_HAND),
                        weapon: new Weapon(
                                damage: (10..20),
                                damageTypes: [DamageType.BASH, material],
                                strengthReq: 20
                        )
                )
                break


            default:
                println "Cannot find ${name}"
                assert false
        }
        return null

    }

}
