package com.stewsters.dusk.map.gen.items

import com.stewsters.dusk.component.Equipment
import com.stewsters.dusk.component.Item
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Slot
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.sfx.ItemFunctions
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

class FantasyItemGen {

    public init() {

    }


    private static final List<Map> spawnPerLevel = [
            [name: "Gold", rarity: 20, startLevel: 0, endLevel: 9],
            [name: "Leather Coat", rarity: 20, startLevel: 0, endLevel: 3],
            //Gambesan
            [name: "Chain Hauberk", rarity: 10, startLevel: 3, endLevel: 6],
            [name: "Coat of Plates", rarity: 5, startLevel: 6, endLevel: 9],

            [name: "Healing Potion", rarity: 10, startLevel: 0, endLevel: 9],

            [name: "Scroll of Fireball", rarity: 20, startLevel: 0, endLevel: 9],
            [name: "Scroll of Lightning", rarity: 20, startLevel: 0, endLevel: 9],
            [name: "Scroll of Domination", rarity: 10, startLevel: 0, endLevel: 9],
            [name: "Scroll of Confusion", rarity: 10, startLevel: 0, endLevel: 9],
            [name: "Scroll of Wrath", rarity: 30, startLevel: 0, endLevel: 9],
            // Scroll of Raise Dead
            // Scroll of Kill

            // slash
            [name: "Hand Axe", rarity: 10, startLevel: 0, endLevel: 3],
            [name: "Arming Sword", rarity: 10, startLevel: 2, endLevel: 4],
            [name: "Longsword", rarity: 10, startLevel: 3, endLevel: 9],

            // pierce
            [name: "Stiletto", rarity: 10, startLevel: 0, endLevel: 3],
            [name: "Spear", rarity: 10, startLevel: 2, endLevel: 4],
            [name: "Halberd", rarity: 10, startLevel: 4, endLevel: 9],

            // halberd - piercing

            // Blunt
            [name:"Club", rarity: 10, startLevel: 0, endLevel: 4],
            [name:"Mace", rarity: 10, startLevel: 3, endLevel: 6],
            [name:"Maul", rarity: 10, startLevel: 6, endLevel: 9]
            //warhammer

//            "Helm"                : 10,
//            "Boots"               : 10,
//            "Greaves"             : 10
//            "Quiver"              : 10
    ]


    public static Entity getRandomItemByLevel(LevelMap map, int x, int y, int level) {

        Map<String, Integer> spawnChances = [:]

        spawnPerLevel.findAll { it.startLevel <= level && it.endLevel >= level }.each {
            spawnChances[it.name] = it.rarity
        }

        return createFromName(map, x, y, MatUtils.getChoice(spawnChances))
    }

    public static Entity createFromName(LevelMap map, int x, int y, String name) {
        switch (name) {

//            case ("Quiver"):
//                return new Entity(map: map, pos: new Point2i(x, y),
//                        glyph: [character: 'r', color: SColor.SILVER.name],
//                        name: "Quiver",
//                        item: new Item(useFunction: ItemFunctions.rifleAmmoBox)
//                )
//
//                break;
            case ("Gold"):
                int gp = MatUtils.d(20);

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
                        equipment: new Equipment(
                                slot: Slot.CHEST,
                                evasionModifier: -1,
                                armor: (2..4)
                        )
                )
                break
            case ("Chain Hauberk"):

                return new Entity(map: map, x: x, y: y,
                        ch: '[', color: SColor.SILVER_GREY,
                        name: 'Chain Hauberk',
                        description: "A shirt of mail reaching the thighs.",
                        equipment: new Equipment(
                                slot: Slot.CHEST,
                                evasionModifier: -2,
                                armor: (3..6)
                        )
                )
                break
            case "Coat of Plates":
                return new Entity(map: map, x: x, y: y,
                        ch: '[', color: SColor.SILVER,
                        name: 'Coat of Plates',
                        description: "Suit of armor made from metal plates.",
                        equipment: new Equipment(
                                slot: Slot.CHEST,
                                evasionModifier: -3,
                                armor: (4..8)
                        )
                )
                break
            case "Healing Potion":
                return new Entity(map: map, x: x, y: y,
                        ch: '!', color: SColor.AZURE,
                        name: 'Healing Potion',
                        description: "Restores health at the cost of toxicity buildup",
                        itemComponent: new Item(useFunction: ItemFunctions.castHeal)
                )
            case "Scroll of Fireball":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.RED,
                        name: "Scroll Of Fireball",
                        description: "Launches a fireball.",
                        itemComponent: new Item(useFunction: ItemFunctions.castFireball)
                )
                break
            case "Scroll of Lightning":

                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.ORANGE,
                        name: "Scroll Of Lightning",
                        description: "Electrocutes the nearest target.",
                        itemComponent: new Item(useFunction: ItemFunctions.castLightning)
                )
                break
            case "Scroll of Domination":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.AMBER,
                        name: "Scroll Of Domination",
                        description: "Makes the closest target your ally.",
                        itemComponent: new Item(useFunction: ItemFunctions.castDomination)
                )
                break
            case "Scroll of Confusion":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.AMARANTH,
                        name: "Scroll Of Confusion",
                        description: "Confuses nearby targets",
                        itemComponent: new Item(useFunction: ItemFunctions.castConfuse)
                )
                break
            case "Scroll of Wrath":
                return new Entity(map: map, x: x, y: y,
                        ch: '?', color: SColor.SILVER_GREY,
                        name: "Scroll Of Wrath",
                        description: "Throws enemies away from you.",
                        itemComponent: new Item(useFunction: ItemFunctions.castWrath)
                )
                break

            // Slash
            case "Hand Axe":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: 'Hand Axe',
                        description: "A one handed axe",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: 1,
                                evasionModifier: 0, // parrying
                                damage: (6..12)
                        )
                )
                break
            case "Arming Sword":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.SILVER_GREY,
                        name: 'Arming Sword',
                        description: "A knightly sword.",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: 2,
                                evasionModifier: 1, // parrying
                                damage: (6..10)
                        )
                )
                break
            case "Longsword":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.STEEL_BLUE,
                        name: 'Longsword',
                        description: "A long sword",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: 1,
                                evasionModifier: 1, // parrying
                                damage: (10..14)
                        )
                )
                break

            // Pierce
            case "Stiletto":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: 'Stiletto',
                        description: "A small piercing dagger.",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: 3,
                                evasionModifier: 0, // parrying
                                damage: (3..5)
                        )
                )
                break
            case "Spear":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.GRAY,
                        name: 'Spear',
                        description: "A pointy stick. Classic.",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: 3,
                                evasionModifier: 1, // parrying
                                damage: (8..12)
                        )
                )
                break
            case "Halberd":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.GOLDEN,
                        name: 'Halberd',
                        description: "A halberd.",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: 0,
                                evasionModifier: 2, // parrying
                                damage: (10..14)
                        )
                )
                break

            // Bash
            case "Club":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: 'Club',
                        description: "A stick.",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: 1,
                                evasionModifier: 1, // parrying
                                damage: (4..8)
                        )
                )
                break
            case "Mace":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.WHITE,
                        name: 'Mace',
                        description: "A weight on the end of a stick.",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: -1,
                                evasionModifier: -1, // parrying
                                damage: (6..16)
                        )
                )
                break

            case "Maul":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑', color: SColor.RED_BEAN,
                        name: 'Maul',
                        description: "A great hammer",
                        equipment: new Equipment(
                                slot: Slot.PRIMARY_HAND,
                                accuracyModifier: -3,
                                evasionModifier: -3, // parrying
                                damage: (10..20)
                        )
                )
                break


            default:
                println "Cannot find ${name}"
                assert false

        }

    }

}
