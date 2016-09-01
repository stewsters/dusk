package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity

public interface Ai {

    public boolean takeTurn()

    //getters and setters
    public Entity getOwner()

    public void setOwner(Entity owner)

//    public float[][] getLight()

    public Set<Entity> findAllVisibleEnemies(int maxDistance)

    public Entity findClosestVisibleEnemy()

    public Entity findClosestVisibleItem()

    public void calculateSight()

//    public void hearNoise(int x, int y)

    public Integer getGameTurn()

    public void setGameTurn(Integer gameTurn)
}