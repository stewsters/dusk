package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity

public interface Ai {

    public Entity getEntity()

    public void setEntity(Entity entity)

    public boolean takeTurn()

//    public float[][] getLight()

    public Set<Entity> findAllVisibleEnemies(int maxDistance)

    public Entity findClosestVisibleEnemy()

    public Entity findClosestVisibleItem()

    public void calculateSight()

//    public void hearNoise(int x, int y)

    public int getGameTurn()

    public int getSpeed()

    public void setGameTurn(int gameTurn)
}