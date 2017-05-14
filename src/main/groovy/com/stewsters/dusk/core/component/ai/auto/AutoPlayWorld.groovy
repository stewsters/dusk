package com.stewsters.dusk.core.component.ai.auto

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.util.planner.BaseWorldState
import com.stewsters.util.planner.Prerequisite
import com.stewsters.util.planner.World

class AutoPlayWorld extends BaseWorldState implements Comparable<AutoPlayWorld>, World<AutoPlayWorld> {

    public int health
    public int maxHealth
    List<Entity> visableOpponents

    public AutoPlayWorld() {
        parentState = null;
        parentAction = null;
        cost = 0;

        health = 0;
        maxHealth = 0;
        visableOpponents = []
    }

    public AutoPlayWorld(Entity e) {
        parentState = null;
        parentAction = null;
        cost = 0;

        health = e.fighter.maxHP
        maxHealth = e.fighter.hp
        visableOpponents = []
    }

    @Override
    AutoPlayWorld getNext() {
        def w = new AutoPlayWorld()

        w.health = health
        w.maxHealth = w.maxHealth
        w.visableOpponents = visableOpponents.clone()
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
