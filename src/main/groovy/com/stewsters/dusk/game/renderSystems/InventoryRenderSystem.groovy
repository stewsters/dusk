package com.stewsters.dusk.game.renderSystems

import com.stewsters.dusk.core.component.Inventory
import com.stewsters.dusk.core.component.Spellbook
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.magic.Spell
import com.stewsters.dusk.game.RenderConfig
import com.stewsters.dusk.game.screen.ScreenMode
import org.apache.commons.lang3.text.WordUtils
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

class InventoryRenderSystem {

    static void render(SwingPane display, Entity player, ScreenMode screenMode, int selectedItem) {

        if (!player.inventory) return

        switch (screenMode) {
            case screenMode.CAST:
                renderSpellbook(display, player.spellbook)
                break

            case screenMode.APPLY:
                renderInventory(display, player.inventory, "Push (a-z) for more info. Esc to Cancel")
                break

            case screenMode.DROP:
                renderInventory(display, player.inventory, "Drop what? (a-z), Esc to Cancel")
                break

            case screenMode.INVENTORY:
                renderInventory(display, player.inventory, "View an item (a-z), Esc to Cancel")
                break

            case screenMode.INVENTORY_INSPECT:
                renderInventory(display, player.inventory, "View an item (a-z), Esc to Cancel")
                renderInspect(display, player.inventory, selectedItem)
                break

            case screenMode.EQUIP:
                renderInventory(display, player.inventory, "Equip what? (a-z), Esc to Cancel")
                break

            case screenMode.REMOVE:
                renderInventory(display, player.inventory, "Unequip what? (a-z), Esc to Cancel")
                break

            case screenMode.THROW:
                renderInventory(display, player.inventory, "Throw what? (a-z), Esc to Cancel")
                break
        }
    }


    private static void renderSpellbook(SwingPane display, Spellbook spellbook) {

        String header = "Cast What?"
        for (int x = 0; x < RenderConfig.inventoryWidth; x++) {
            for (int y = 0; y < Math.min(RenderConfig.inventoryMaxHeight, spellbook.spells.size()) + 1 + (header ? 1 : 0); y++) {
                display.clearCell(x + RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY)
            }
        }

        Integer y = 0
        spellbook.spells.sort { it.name }.each { Spell spell ->

            String out = "${spell.key}) ${spell.name}"
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, out)
            y++
        }

        if (header)
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, "-- " + header,
                    SColor.GRAY, SColor.BLACK
            )
    }

    private static void renderInventory(SwingPane display, Inventory inventory, String header) {
        for (int x = 0; x < RenderConfig.inventoryWidth; x++) {
            for (int y = 0; y < Math.min(RenderConfig.inventoryMaxHeight, inventory.items.size()) + 1 + (header ? 1 : 0); y++) {
                display.clearCell(x + RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY)
            }
        }

        Integer y = 0

        int xLineStart = RenderConfig.screenWidth - RenderConfig.inventoryWidth

        inventory.allEquippedEquipment.each { Entity item ->
            char c = (char) (((char) 'a') + inventory.findIndex(item))

            display.placeHorizontalString(xLineStart, y + RenderConfig.inventoryY, "${c})")
            display.placeCharacter(xLineStart + 2, y + RenderConfig.inventoryY, item.ch, item.color, SColor.BLACK)
            display.placeHorizontalString(xLineStart + 3, y + RenderConfig.inventoryY, " ${item.name.substring(0, Math.min(RenderConfig.inventoryWidth - 2, item.name.length()))}")
            y++
        }

        if (y != 0 && y != inventory.items.size()) {
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, "     ---", SColor.GRAY, null)
            y++
        }

        inventory.allNonEquippedItems.each { Entity item ->
            char c = (char) (((char) 'a') + inventory.findIndex(item))

            display.placeHorizontalString(xLineStart, y + RenderConfig.inventoryY, "${c})")
            display.placeCharacter(xLineStart + 2, y + RenderConfig.inventoryY, item.ch, item.color, SColor.BLACK)
            display.placeHorizontalString(xLineStart + 3, y + RenderConfig.inventoryY, " ${item.name.substring(0, Math.min(RenderConfig.inventoryWidth - 2, item.name.length()))}")
            y++
        }


        display.placeHorizontalString(
                RenderConfig.screenWidth - RenderConfig.inventoryWidth,
                y + RenderConfig.inventoryY,
                "   You have room for ${inventory.capacity - inventory.items.size()} more items",
                SColor.GRAY, SColor.BLACK
        )
        y++

        if (header)
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, "-- " + header,
                    SColor.GRAY, SColor.BLACK
            )

        //
    }

    static void renderInspect(SwingPane display, Inventory inventory, int itemIndex) {


        final int boxWidth = 40
        final int xStart = RenderConfig.screenWidth - RenderConfig.inventoryWidth - boxWidth + 1

        //TODO: need to figure out the height before we need it?
        for (int x = 0; x < boxWidth; x++) {
            for (int y = 0; y < 10; y++) {
                display.placeCharacter(x + RenderConfig.screenWidth - RenderConfig.inventoryWidth - boxWidth, y + RenderConfig.inventoryY,
                        ' ' as char, SColor.WHITE, SColor.DARK_GRAY
                )
            }
        }

        Entity item = inventory.items.get(itemIndex)

        int i = 1
        display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, item.name)
        i++

        // render description
        if (item.description) {
            WordUtils.wrap(item.description, boxWidth - 2).eachLine {
                display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, it)
                i++
            }
        }

        if (item?.weapon?.damageTypes) {
            i++
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, item?.weapon?.damageTypes?.name?.join(", ") ?: "")
            i++
        }

        if (item?.weapon?.damage) {
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, "Damage: ${item?.weapon?.damage?.from} - ${item?.weapon?.damage?.to}")
            i++
        }
        if (item?.armor?.armor) {
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, "Armor: ${item?.armor?.armor}")
            i++
        }

//        if (item?.equipment?.accuracyModifier) {
//            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, "Accuracy: ${item?.equipment?.accuracyModifier}")
//            i++
//        }
//        if (item?.equipment?.evasionModifier) {
//            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, "Evasion: ${item?.equipment?.evasionModifier}")
//            i++
//        }

        String actions = ""

        // Render possible actions
        if (item.item.useFunction) {
            actions += "[Apply] "
        }
        if (item.equipment && !item.equipment.isEquipped) {
            actions += "[Equip] "
        }
        if (item.equipment && item.equipment.isEquipped) {
            actions += "[Remove] "
        }

        actions += "[Drop] [Throw]"

        WordUtils.wrap(actions, boxWidth - 2).eachLine {
            i++
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, it)
        }

    }

}
