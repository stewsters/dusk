package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.Slot
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor
import squidpony.squidmath.Bresenham

@CompileStatic
class Inventory {

    Entity entity
    List<Entity> items = []
    int capacity = 26


    boolean pickUp(Entity item) {

        if (items.size() >= capacity) {
            MessageLogSystem.send("Inventory full, cannot pick up ${item.name}", SColor.RED, [entity])

        } else {

            // use item on pickup
            if (item.item.useOnPickup) {
                Closure use = item.item.useFunction
                use(entity)
            } else {
                items.add item

                if (item.equipment) {
                    Equipment oldEquipment = entity.inventory.getEquippedInSlot(item.equipment.slot)
                    if (!oldEquipment)
                        item.equipment.equip(entity)
                }
            }

            item.levelMap.remove(item)
            MessageLogSystem.send("${entity.name} picked up ${item.name}", SColor.GREEN, [entity])

            return true
        }
        return false
    }

    boolean isFull() {
        return capacity <= items.size() - 1
    }

    void dump() {

        for (Entity item : entity.inventory.items) {

            int xPos = MatUtils.getIntInRange(-1, 1) + entity.x
            int yPos = MatUtils.getIntInRange(-1, 1) + entity.y
            if (!entity.levelMap.isBlocked(xPos, yPos)) {
                item.x = xPos
                item.y = yPos
            } else {
                item.x = entity.x
                item.y = entity.y
            }
            if (item.equipment?.isEquipped)
                item.equipment.dequip(entity)
            entity.levelMap.add(item)
        }
        entity.inventory.items.clear()
    }

    int findIndex(Entity entity) {
        items.indexOf(entity)
    }

    boolean dropItemById(int number) {
        //take held item and put it on the ground where you stand

        if ( items.size() > number) {
            Entity item = items.remove(number)
            item.x = entity.x
            item.y = entity.y
            if (item.equipment?.isEquipped)
                item.equipment.dequip(entity)
            entity.levelMap.add(item)
            return true
        } else {
            MessageLogSystem.send("${entity.name} has nothing to drop.", SColor.WHITE, [entity])
            return false
        }

    }

    boolean hasItemById(int id) {
        if (items.size() > id) {
            return true
        }
        return false
    }

    boolean useById(int id) {
        if (items.size() > id) {
            Entity item = items.get(id)
            if (item) {
                if (item.item.useItem(entity)) {
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
                if (item.equipment.equip(entity)) {
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
                if (item.equipment.dequip(entity)) {
                    return true
                }
            }
        }
        return false
    }

    boolean throwItemById(int id, int wx, int wy) {


        if (items.size() > id) {
            Entity item = items.remove(id)
            if (item.equipment?.isEquipped)
                item.equipment.dequip(entity)

            int x = entity.x
            int y = entity.y
            for (def p : Bresenham.line2D(entity.x, entity.y, wx, wy)) {
                if (entity.levelMap.isBlocked(x, y)){
                   // TODO: blocked by something.  if its the thrower ignore it, if its a target hit it

                    break
                }
                x = p.x as Integer
                y = p.y as Integer
            }

            item.x = x
            item.y = y
            entity.levelMap.add(item)
            return true


        } else {
            MessageLogSystem.send("${entity.name} has nothing to drop.", SColor.WHITE, [entity])
            return false
        }


        false
    }


    Equipment getEquippedInSlot(Slot slot) {
        for (Entity item : items) {
            if (item.equipment && item.equipment.slot == slot && item.equipment.isEquipped) {
                return item.equipment
            }
        }
        return null
    }

    List<Entity> getAllEquippedEquipment() {
        return items.findAll { item -> item.equipment && item.equipment.isEquipped }
    }

    List<Entity> getAllNonEquippedItems() {
        return items.findAll { item -> !item.equipment || !item.equipment.isEquipped }
    }

    Weapon getEquippedWeapon() {
        return items.find { Entity item -> item.weapon && item.equipment && item.equipment.isEquipped }?.weapon
    }

}
