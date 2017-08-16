package com.stewsters.dusk.core.component.ai.auto

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.planner.BaseWorldState
import com.stewsters.util.planner.Prerequisite
import com.stewsters.util.planner.World

class AutoPlayWorld extends BaseWorldState implements Comparable<AutoPlayWorld>, World<AutoPlayWorld> {

    public int health
    public int maxHealth
    public int visableOpponents
    public int visableItems
    public int items = 0

    public AutoPlayWorld() {
        parentState = null;
        parentAction = null;
        cost = 0;

        health = 0;
        maxHealth = 0;
        visableOpponents = 0
        visableItems = 0
    }

    public AutoPlayWorld(Entity e) {
        parentState = null;
        parentAction = null;
        cost = 0;

        health = e.fighter.maxHP
        maxHealth = e.fighter.hp
        visableOpponents = e.ai.findAllVisibleEnemies(10).size()
    }

    @Override
    AutoPlayWorld getNext() {
        def w = new AutoPlayWorld()

        w.health = health
        w.items = items
        w.maxHealth = maxHealth
        w.visableOpponents = visableOpponents
        w.visableItems = visableItems
        return w
    }

    @Override
    boolean meetsPrerequisite(Prerequisite prerequisite) {
        return prerequisite.has(this)
    }

    @Override
    public int compareTo(AutoPlayWorld o) {
        return Float.compare(cost, o.cost);
    }
}
