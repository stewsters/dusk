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

    public static Map<String, Integer> spawnChance = [
            "Gold"                : 20,
            "Leather Coat"        : 20,
            "Chain Hauberk"       : 10,
            "Coat of Plates"      : 5,

            "Healing Potion"      : 10,

            "Scroll of Fireball"  : 20,
            "Scroll of Lightning" : 20,
            "Scroll of Domination": 10,
            "Scroll of Confusion" : 10,
            "Scroll of Wrath"     : 80,

            "Longsword"           : 10,
            "Arming Sword"        : 10,

//            "Helm"                : 10,
//            "Boots"               : 10,
//            "Greaves"             : 10
//            "Quiver"              : 10
    ]

    public static Entity getRandomItem(LevelMap map, int x, int y) {
        return createFromName(map, x, y, MatUtils.getChoice(spawnChance))
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
                        ch: '[',color: SColor.SILVER,
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
                        ch: '!',  color: SColor.AZURE,
                        name: 'Healing Potion',
                        description: "Restores health at the cost of toxicity buildup",
                        itemComponent: new Item(useFunction: ItemFunctions.castHeal)
                )
            case "Scroll of Fireball":
                return new Entity(map: map, x: x, y: y,
                        ch: '?',  color: SColor.RED,
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
                        ch: '?',  color: SColor.AMBER,
                        name: "Scroll Of Domination",
                        description: "Makes the closest target your ally.",
                        itemComponent: new Item(useFunction: ItemFunctions.castDomination)
                )
                break
            case "Scroll of Confusion":
                return new Entity(map: map, x: x, y: y,
                        ch: '?',  color: SColor.AMARANTH,
                        name: "Scroll Of Confusion",
                        description: "Confuses nearby targets",
                        itemComponent: new Item(useFunction: ItemFunctions.castConfuse)
                )
                break
            case "Scroll of Wrath":
                return new Entity(map: map, x: x, y: y,
                        ch: '?',  color: SColor.AMARANTH,
                        name: "Scroll Of Wrath",
                        description: "Throws enemies away from you.",
                        itemComponent: new Item(useFunction: ItemFunctions.castWrath)
                )
                break
            case "Longsword":
                return new Entity(map: map, x: x, y: y,
                        ch: '↑',  color: SColor.STEEL_BLUE,
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
            default:
                println "Cannot find ${name}"
                assert false

        }

    }

}
