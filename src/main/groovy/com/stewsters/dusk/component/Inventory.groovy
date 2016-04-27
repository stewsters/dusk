package com.stewsters.dusk.component

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Slot
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.dusk.main.RenderConfig
import com.stewsters.util.math.MatUtils
import org.apache.commons.lang3.text.WordUtils
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

public class Inventory {

    Entity owner
    List<Entity> items = []
    int capacity = 26


    public boolean pickUp(Entity item) {

        if (items.size() >= capacity) {
            MessageLog.send("Inventory full, cannot pick up ${item.name}", SColor.RED, [owner])

        } else {

            // use item on pickup
            if (item.itemComponent.useOnPickup) {
                item.itemComponent.useFunction(owner);
            } else {
                items.add item

                if (item.equipment) {
                    Equipment oldEquipment = owner.inventory.getEquippedInSlot(item.equipment.slot)
                    if (!oldEquipment)
                        item.equipment.equip(owner)
                }
            }

            item.levelMap.remove(item)
            MessageLog.send("${owner.name} picked up ${item.name}", SColor.GREEN, [owner])

            return true
        }
        return false
    }

    public boolean isFull() {
        return capacity <= items.size() - 1
    }

    public dump() {

        for (Entity item : owner.inventory.items) {

            int xPos = MatUtils.getIntInRange(-1, 1) + owner.x
            int yPos = MatUtils.getIntInRange(-1, 1) + owner.y
            if (!owner.levelMap.isBlocked(xPos, yPos)) {
                item.x = xPos
                item.y = yPos
            } else {
                item.x = owner.x
                item.y = owner.y
            }
            if (item.equipment?.isEquipped)
                item.equipment.dequip(owner)
            owner.levelMap.add(item)
        }
        owner.inventory.items.clear()
    }

    public render(SwingPane display, String header) {
        for (int x = 0; x < RenderConfig.inventoryWidth; x++) {
            for (int y = 0; y < Math.min(RenderConfig.inventoryMaxHeight, items.size()) + 1 + (header ? 1 : 0); y++) {
                display.clearCell(x + RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY)
            }
        }

        Integer y = 0

        allEquipment.each { Entity item ->
            char c = (char) (((char) 'a') + findIndex(item))

            String out = "${c}) ${item.ch ?: ' '} ${item.name.substring(0, Math.min(RenderConfig.inventoryWidth - 2, item.name.length()))}"
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, out)
            y++
        }

        if (y != 0 && y != items.size()) {
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, "     ---", SColor.GRAY, null)
            y++
        }

        allNonEquipment.each { Entity item ->
            char c = (char) (((char) 'a') + findIndex(item))

            String out = "${c}) ${item.ch ?: ' '} ${item.name.substring(0, Math.min(RenderConfig.inventoryWidth - 2, item.name.length()))}"
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, out)
            y++
        }


        display.placeHorizontalString(
                RenderConfig.screenWidth - RenderConfig.inventoryWidth,
                y + RenderConfig.inventoryY,
                "   You have room for ${capacity - items.size()} more items",
                SColor.GRAY, SColor.BLACK
        )
        y++

        if (header)
            display.placeHorizontalString(RenderConfig.screenWidth - RenderConfig.inventoryWidth, y + RenderConfig.inventoryY, "-- " + header,
                    SColor.GRAY, SColor.BLACK
            )

        //
    }

    public void renderInspect(SwingPane display, int itemIndex) {


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

        Entity item = items.get(itemIndex)

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

        if (item?.equipment?.damageTypes) {
            i++
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, item?.equipment?.damageTypes?.name?.join(", ") ?: "")
            i++
        }

        if (item?.equipment?.damage) {
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, "Damage: ${item?.equipment?.damage.from} - ${item?.equipment?.damage.to}")
            i++
        }
        if (item?.equipment?.armor) {
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, "Armor: ${item?.equipment?.armor.from} - ${item?.equipment?.armor.to}")
            i++
        }

        if (item?.equipment?.accuracyModifier) {
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, "Accuracy: ${item?.equipment?.accuracyModifier}")
            i++
        }
        if (item?.equipment?.evasionModifier) {
            display.placeHorizontalString(xStart, RenderConfig.inventoryY + i, "Evasion: ${item?.equipment?.evasionModifier}")
            i++
        }

        String actions = ""

        // Render possible actions
        if (item.itemComponent.useFunction) {
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

    public int findIndex(Entity entity) {
        items.indexOf(entity)
    }

    public boolean dropById(int id) {
        if (items.size() > id) {
            Entity item = items.get(id)
            if (item) {
                if (item.itemComponent.useItem(owner)) {
                    items.remove(item)
                    return true
                }
            }
        }
        return false
    }

    public boolean hasItemById(int id) {
        if (items.size() > id) {
            return true
        }
        return false
    }

    public boolean useById(int id) {
        if (items.size() > id) {
            Entity item = items.get(id)
            if (item) {
                if (item.itemComponent.useItem(owner)) {
                    items.remove(item)
                    return true
                }
            }
        }
        return false
    }

    boolean equipById(int id) {
        if (items.size() > id) {
            Entity item = items.get(id)
            if (item) {
                if (item.equipment.equip(owner)) {
                    return true
                }
            }
        }
        return false
    }

    boolean removeById(int id) {
        if (items.size() > id) {
            Entity item = items.get(id)
            if (item) {
                if (item.equipment.dequip(owner)) {
                    return true
                }
            }
        }
        return false
    }


    public Equipment getEquippedInSlot(Slot slot) {
        for (Entity item : items) {
            if (item.equipment && item.equipment.slot == slot && item.equipment.isEquipped) {
                return item.equipment
            }
        }
        return null
    }

    public List<Equipment> getAllEquippedEquipment() {
        return items.findAll { item -> item.equipment && item.equipment.isEquipped }.equipment
    }

    public List<Entity> getAllEquipment() {
        return items.findAll { item -> item.equipment && item.equipment.isEquipped }
    }

    public List<Entity> getAllNonEquipment() {
        return items.findAll { item -> !item.equipment || !item.equipment.isEquipped }
    }

}
