package com.stewsters.dusk.entity

import com.stewsters.dusk.component.*
import com.stewsters.dusk.component.ai.Ai
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.dusk.map.LevelMap
import com.stewsters.util.math.MatUtils
import squidpony.squidcolor.SColor

/**
 * this is a generic object: the player, an opponent, an item, the stairs...
 * it's always represented by a character on screen.
 */
public class Entity {

    public LevelMap levelMap

    public Priority priority

    public String name
    public boolean blocks
    public int x
    public int y
    public char ch
    public SColor color

    public Fighter fighter
    public Ai ai
    public Faction faction
    public Item itemComponent
    public Equipment equipment
    public Inventory inventory
    public Purse purse
    public Spellbook spellbook
    public Quiver quiver
    public String description

    /**
     * LevelMap map, int x, int y, char ch, String name, def color,
     blocks = false, Fighter fighter = null, Ai ai = null, Faction faction = null,
     Item itemComponent
     * @param params
     */

    public Entity(Map params) {


        x = params.x ?: 0
        y = params.y ?: 0

        ch = (params.ch ?: '@') as char
        name = params.name ?: 'Unnamed'
        blocks = params.blocks ?: false

        color = params.color ?: SColor.WHITE
        faction = params.faction

        priority = params.priority ?: Priority.ITEM

        if (params.description)
            description = params.description

        if (params.fighter) {
            fighter = params.fighter
            fighter.owner = this
        }

        if (params.ai) {
            ai = params.ai
            ai.owner = this
        }

        if (params.inventory) {
            inventory = params.inventory
            inventory.owner = this
        }

        if (params.itemComponent) {
            itemComponent = params.itemComponent
            itemComponent.owner = this
        }

        if (params.equipment) {
            equipment = params.equipment
            equipment.owner = this
            if (!itemComponent) {
                itemComponent = new Item([:])
                itemComponent.owner = this
            }
        }

        if (params.purse) {
            purse = params.purse
        }

        if (params.spellbook) {
            spellbook = params.spellbook
        }

        if (params.quiver) {
            quiver = params.quiver
        }

        //This is last, because it needs to add the locations to the index
        levelMap = params.map
        if (levelMap)
            levelMap.add(this)
    }

    public int distanceTo(Entity other) {
        int dx = other.x - this.x
        int dy = other.y - this.y
        return (int) Math.round(Math.sqrt(dx * dx + dy * dy))
    }

    public boolean move(int xDif, int yDif) {
        int newX = xDif + x
        int newY = yDif + y

        if (!(levelMap.isBlocked(newX, newY))) {
            x = newX
            y = newY
            levelMap.update(this);
            return true
        }
        return false
    }

    public boolean moveOrAttack(int dx, int dy) {
        int newX = dx + x
        int newY = dy + y

        if (x < 0 || x >= levelMap.getXSize() || y < 0 || y >= levelMap.getYSize()) {
            return false;
        }

        HashSet<Entity> entities
        if (inventory || fighter) {
            entities = levelMap.getEntitiesAtLocation(newX, newY)

            if (fighter) {
                Entity target = entities.find {
                    it.fighter && faction?.hates(it?.faction)
                }

                if (target) {
                    fighter.attack(target)
                    return true
                }
            }
            if (inventory) {

                Entity pickup = entities.find { Entity entity ->
                    entity?.itemComponent?.autoPickup
                }

                if (pickup) {
                    boolean picked = inventory.pickUp(pickup)
                    boolean moved = move(dx, dy)
                    return picked || moved
                }
            }
        }

        return move(dx, dy)


    }


    public boolean moveTowards(int targetX, int targetY) {
        int dx = targetX - x
        int dy = targetY - y

        int mx = 0
        int my = 0

        // get direction of the vector
        if (dx > 0) {
            mx = 1
        } else if (dx < 0)
            mx = -1

        if (dy > 0) {
            my = 1
        } else if (dy < 0)
            my = -1

        if (move(mx, my)) {
            return true
        } else {
            if (Math.abs(dx) > Math.abs(dy)) {
                return move(mx, 0) || move(0, my)
            } else {
                return move(0, my) || move(mx, 0)
            }
        }

    }


    public boolean moveTowardsAndAttack(int targetX, int targetY) {
        int dx = targetX - x
        int dy = targetY - y

        int mx = 0
        int my = 0

        // get direction of the vector
        if (dx > 0) {
            mx = 1
        } else if (dx < 0)
            mx = -1

        if (dy > 0) {
            my = 1
        } else if (dy < 0)
            my = -1

        if (moveOrAttack(mx, my)) {
            return true
        } else {
            if (Math.abs(dx) > Math.abs(dy)) {
                return moveOrAttack(mx, 0) || moveOrAttack(0, my)
            } else {
                return moveOrAttack(0, my) || moveOrAttack(mx, 0)
            }
        }
    }

    public boolean moveAway(int targetX, int targetY) {
        int dx = targetX - x
        int dy = targetY - y

        dx = MatUtils.limit(dx, -1, 1)
        dy = MatUtils.limit(dy, -1, 1)
        return move(-dx, -dy)
    }


    public boolean grab() {
        if (!inventory) {
            MessageLog.send("${name} can't hold items.", SColor.WHITE, [this])
            return
        }

        Entity topItem = levelMap.getEntitiesAtLocation(x, y).sort { it.priority }.find { it.itemComponent }

        if (topItem) {
            return inventory.pickUp(topItem)
        }
    }

    public boolean dropItemById(int number) {
        //take held item and put it on the ground where you stand

        if (inventory.items.size() && inventory.items.size() > number) {
            Entity item = inventory.items.remove(number)
            item.x = x
            item.y = y
            if (item.equipment?.isEquipped)
                item.equipment.dequip(this)
            levelMap.add(item)
            return true
        } else {
            MessageLog.send("${name} has nothing to drop.", SColor.WHITE, [this])
            return false
        }

    }

    boolean throwItemById(int i) {
        //TODO: throw
        false
    }

    public boolean standStill() {
        return true
    }

    public boolean randomMovement() {
        return move(MatUtils.getIntInRange(-1, 1), MatUtils.getIntInRange(-1, 1))
    }
}
