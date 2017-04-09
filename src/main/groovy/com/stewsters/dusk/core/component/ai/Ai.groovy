package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import groovy.transform.CompileStatic

@CompileStatic
interface Ai {

    Entity getEntity()

    void setEntity(Entity entity)

    boolean takeTurn()

//    public float[][] getLight()

    Set<Entity> findAllVisibleEnemies(int maxDistance)

    Entity findClosestVisibleEnemy()

    Entity findClosestVisibleItem()

    void calculateSight()

//    public void hearNoise(int x, int y)

    int getGameTurn()

    int getSpeed()

    void setGameTurn(int gameTurn)
}