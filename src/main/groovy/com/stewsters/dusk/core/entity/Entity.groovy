package com.stewsters.dusk.core.entity

import com.stewsters.dusk.core.component.Armor
import com.stewsters.dusk.core.component.CoreStats
import com.stewsters.dusk.core.component.Equipment
import com.stewsters.dusk.core.component.Fighter
import com.stewsters.dusk.core.component.Inventory
import com.stewsters.dusk.core.component.Item
import com.stewsters.dusk.core.component.Purse
import com.stewsters.dusk.core.component.Quiver
import com.stewsters.dusk.core.component.Spellbook
import com.stewsters.dusk.core.component.Weapon
import com.stewsters.dusk.core.component.ai.Ai
import com.stewsters.dusk.core.component.mover.DuskMover2d
import com.stewsters.dusk.core.flyweight.Faction
import com.stewsters.dusk.core.flyweight.Priority
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.game.renderSystems.MessageLogSystem
import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import squidpony.squidcolor.SColor

/**
 * this is a generic object: the player, an opponent, an item, the stairs...
 * it's always represented by a character on screen.
 */
class Entity {

    public LevelMap levelMap
    public int x
    public int y

    public int xSize
    public int ySize

    public char ch
    public SColor color
    public Priority priority

    public String name
    public String description

    public boolean blocks

    public Fighter fighter
    public CoreStats coreStats
    public Ai ai
    public Faction faction
    public Item item

    public Equipment equipment
    public Armor armor
    public Weapon weapon

    public Inventory inventory
    public Purse purse
    public Quiver quiver
    public Spellbook spellbook

    public DuskMover2d mover

    Entity(Map params) {

        x = params.x ?: 0
        y = params.y ?: 0

        xSize = params.xSize ?: 1
        ySize = params.ySize ?: 1

        ch = (params.ch ?: '@') as char
        name = params.name ?: 'Unnamed'
        blocks = params.blocks ?: false

        color = params.color ?: SColor.WHITE
        faction = params.faction

        priority = params.priority ?: Priority.ITEM

        if (params.description)
            description = params.description

        if (params.coreStats) {
            coreStats = params.coreStats
            coreStats.entity = this
        }

        if (params.fighter) {
            fighter = params.fighter
            fighter.entity = this
        }

        if (params.ai) {
            ai = params.ai
            ai.entity = this
            mover = new DuskMover2d(this)
        }

        if (params.inventory) {
            inventory = params.inventory
            inventory.entity = this
        }

        if (params.itemComponent) {
            item = params.itemComponent
            item.entity = this
        }

        if (params.equipment) {
            equipment = params.equipment
            equipment.entity = this
            if (!item) {
                item = new Item([:])
                item.entity = this
            }
        }

        if (params.armor) {
            armor = params.armor
            armor.entity = this
        }

        if (params.weapon) {
            weapon = params.weapon
            weapon.entity = this
        }

        if (params.purse) {
            purse = params.purse
        }

        if (params.spellbook) {
            spellbook = params.spellbook
            spellbook.entity = this
        }

        if (params.quiver) {
            quiver = params.quiver
        }

        //This is last, because it needs to add the locations to the index
        levelMap = params.map
        if (levelMap)
            levelMap.add(this)
    }

    int distanceTo(Entity other) {
        int dx = other.x - this.x
        int dy = other.y - this.y
        return (int) Math.round(Math.sqrt(dx * dx + dy * dy))
    }

    boolean move(int xDif, int yDif) {
        int newX = xDif + x
        int newY = yDif + y

        if (mover.canTraverse(x, y, newX, newY)) {
            x = newX
            y = newY
            levelMap.update(this)
            return true
        }
        return false
    }

    boolean moveOrAttack(int dx, int dy) {
        int newX = dx + x
        int newY = dy + y

        if (levelMap.outsideMap(newX, newY, xSize, ySize)) {
            return false
        }

        if (inventory || fighter) {

            if (fighter) {
                Set<Entity> target = []
                Weapon weapon = inventory?.getEquippedWeapon()
                if (weapon) {

                    boolean doAttack = weapon.moveSet.getTriggerArea(x, y, dx, dy).find { Point2i p ->
                        for (Entity e : levelMap.getEntitiesAtLocation(p.x, p.y)) {
                            if (e.fighter && faction?.hates(e?.faction))
                                return true
                        }
                        return false
                    }

                    //Weapon attack
                    if (doAttack) {
                        weapon.moveSet.getAttackArea(x, y, dx, dy).each { Point2i p ->
                            for (Entity e : levelMap.getEntitiesAtLocation(p.x, p.y)) {
                                if (e.fighter && faction?.hates(e?.faction))
                                    target.add(e)
                            }
                        }
                    }
                } else {
                    //unarmed attack
                    if (xSize > 1 || ySize > 1)
                        target = levelMap.getEntitiesBetween(newX, newY, newX + xSize - 1, newY + ySize - 1).findAll {
                            it.fighter && faction?.hates(it?.faction)
                        }
                    else
                        target = levelMap.getEntitiesAtLocation(newX, newY).findAll {
                            it.fighter && faction?.hates(it?.faction)
                        }
                }


                if (target) {
                    target.each {
                        fighter.attack(it)
                    }
                    return true
                }
            }
            if (inventory) {

                HashSet<Entity> entities
                if (xSize > 1 || ySize > 1)
                    entities = levelMap.getEntitiesBetween(newX, newY, newX + xSize - 1, newY + ySize - 1)
                else
                    entities = levelMap.getEntitiesAtLocation(newX, newY)

                Entity pickup = entities.find { Entity possibleItem ->
                    possibleItem?.item?.autoPickup
                }

                if (pickup) {
                    boolean picked = inventory.pickUp(pickup)
                    boolean moved = move(dx, dy)
                    return picked || moved
                }
            }
        }

        if (move(dx, dy)) {

            if (fighter) {
                Weapon weapon = inventory?.getEquippedWeapon()
                if (weapon?.postMoveAttack) {
                    Set<Entity> target = []
                    //Weapon attack
                    boolean doAttack = weapon.postMoveAttack.getTriggerArea(x, y, dx, dy).contains { Point2i p ->
                        target.addAll(levelMap.getEntitiesAtLocation(p.x, p.y))
                    }
                    if (doAttack) {
                        weapon.postMoveAttack.getAttackArea(x, y, dx, dy).each { Point2i p ->
                            target.addAll(levelMap.getEntitiesAtLocation(p.x, p.y))
                        }
                        target = target.findAll {
                            it.fighter && faction?.hates(it?.faction)
                        }

                        if (target) {
                            target.each {
                                fighter.attack(it)
                            }
                        }
                    }
                }
            }

            return true
        }
        return false

    }


    boolean moveTowards(int targetX, int targetY) {
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


    boolean moveTowardsAndAttack(int targetX, int targetY) {
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

    boolean moveAway(int targetX, int targetY) {
        int dx = targetX - x
        int dy = targetY - y

        dx = MatUtils.limit(dx, -1, 1)
        dy = MatUtils.limit(dy, -1, 1)
        return move(-dx, -dy)
    }


    boolean grab() {
        if (!inventory) {
            MessageLogSystem.send("${name} can't hold items.", SColor.WHITE, [this])
            return false
        }

        Entity topItem = levelMap.getEntitiesAtLocation(x, y).sort { it.priority }.find { it.item }

        if (topItem) {
            return inventory.pickUp(topItem)
        }
        return false
    }

    boolean dropItemById(int number) {
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
            MessageLogSystem.send("${name} has nothing to drop.", SColor.WHITE, [this])
            return false
        }

    }

    boolean throwItemById(int i) {
        //TODO: throw
        false
    }

    boolean standStill() {

        if (fighter) {
            int max = fighter.maxArmor
            if (fighter.armor < max) {
                fighter.armor++
            }
        }

        return true
    }

    boolean randomMovement() {
        return move(MatUtils.getIntInRange(-1, 1), MatUtils.getIntInRange(-1, 1))
    }

    String getName() {
        return name

        //TODO: figure this out
//
//        if (!equipment) {
//            return name
//        }
//
//        List<String> offenceStats = []
//        if (equipment.accuracyModifier)
//            offenceStats += "${equipment.accuracyModifier}"
//        if (equipment.damage)
//            offenceStats += "${equipment.damage.from}-${equipment.damage.to}"
//
//        List<String> defenceStats = []
//        if (equipment.evasionModifier)
//            defenceStats += equipment.evasionModifier
//        if (equipment.armor)
//            defenceStats += "${equipment.armor.from}-${equipment.armor.to}"
//
//        return name + (offenceStats ? " (${offenceStats.join(',')})" : "") + (defenceStats ? " [${defenceStats.join(',')}]" : "")
    }
}
