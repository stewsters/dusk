package com.stewsters.dusk.component.ai

import com.stewsters.dusk.entity.Entity

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

    public void hearNoise(int x, int y)

    public int getGameTurn()
    public void setGameTurn(int gameTurn)
}