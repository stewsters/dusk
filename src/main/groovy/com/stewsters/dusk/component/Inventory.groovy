package com.stewsters.dusk.component

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Slot
import com.stewsters.dusk.main.RenderConfig
import com.stewsters.dusk.system.render.MessageLogSystem
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
            MessageLogSystem.send("Inventory full, cannot pick up ${item.name}", SColor.RED, [owner])

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
            MessageLogSystem.send("${owner.name} picked up ${item.name}", SColor.GREEN, [owner])

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
