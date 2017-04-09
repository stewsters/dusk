package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.Game
import com.stewsters.dusk.game.RenderConfig
import groovy.transform.CompileStatic

/**
 * These are functions common to all AIs
 */
@CompileStatic
abstract class BaseAi implements Ai {

    protected Entity entity
    protected float[][] light
    protected int lightLastCalculated = 0
    protected int sightRange = 20

    protected int speed = 10
    protected int gameTurn

    @Override
    Set<Entity> findAllVisibleEnemies(int maxDistance) {
        if (!entity.faction) return null

        calculateSight()

        int lowXDist = entity.x - maxDistance
        int highXDist = entity.x + maxDistance
        int lowYDist = entity.y - maxDistance
        int highYDist = entity.y + maxDistance

        int lowX = entity.x - sightRange
        int lowY = entity.y - sightRange

        return entity.levelMap.getEntitiesBetween(lowXDist, lowYDist, highXDist, highYDist).findAll { Entity other ->

            if (other.fighter && entity.faction?.hates(other.faction) && entity.distanceTo(other) < maxDistance) {
                int lightX = entity.x - lowX
                int lightY = entity.y - lowY

                if (lightX >= 0 && lightX < this.light.length && lightY >= 0 && lightY < this.light[0].length && this.light[lightX][lightY] > 0f) {
                    return true
                }
            }
            return false

        }
    }

    @Override
    Entity findClosestVisibleEnemy() {
        if (!entity.faction) return null

        calculateSight()

        int lowX = entity.x - sightRange
        int highX = entity.x + sightRange
        int lowY = entity.y - sightRange
        int highY = entity.y + sightRange

        Entity enemy = null

        int maxDistance = Integer.MAX_VALUE

        for (Entity otherEntity : entity.levelMap.getEntitiesBetween(lowX, lowY, highX, highY)) {
            if (otherEntity.fighter && otherEntity.fighter.hp && entity.faction?.hates(otherEntity.faction)) {
                int lightX = otherEntity.x - lowX
                int lightY = otherEntity.y - lowY

                //TODO: this goes out of bounds.  Use advanced lighting?
                if (lightX >= 0 && lightX < light.length && lightY >= 0 && lightY < light[0].length && light[lightX][lightY] > 0f) {

                    int tempDist = entity.distanceTo(otherEntity)
                    if (tempDist <= maxDistance) {
                        enemy = otherEntity
                        maxDistance = tempDist
                    }
                }
            }
        }
        return enemy

    }

    @Override
    Entity findClosestVisibleItem() {

        calculateSight()

        int lowX = entity.x - sightRange
        int highX = entity.x + sightRange
        int lowY = entity.y - sightRange
        int highY = entity.y + sightRange

        int maxDistance = Integer.MAX_VALUE
        Entity item = null

        for (Entity otherEntity : entity.levelMap.getEntitiesBetween(lowX, lowY, highX, highY)) {

            if (otherEntity.x > lowX && otherEntity.x < highX &&
                    otherEntity.y > lowY && otherEntity.y < highY &&
                    otherEntity.item) {
                int lightX = otherEntity.x - lowX
                int lightY = otherEntity.y - lowY
                if (light[lightX][lightY] > 0f) {

                    int tempDist = entity.distanceTo(otherEntity)
                    if (tempDist <= maxDistance) {
                        item = otherEntity
                        maxDistance = tempDist
                    }
                }
            }
        }
        return item

    }

    @Override
    void calculateSight() {
        if (lightLastCalculated == Game.gameTurn)
            return

        int worldLowX = entity.x - sightRange //low is upper left corner
        int worldLowY = entity.y - sightRange

        int range = 2 * sightRange + 1 // this is the total size of the box

        //Get resistance from map
        float[][] resistances = new float[range][range]
        for (int x = 0; x < range; x++) {
            for (int y = 0; y < range; y++) {
                int originalX = x + worldLowX
                int originalY = y + worldLowY

                if (entity.levelMap.contains(originalX, originalY)) {
                    resistances[x][y] = entity.levelMap.getOpacity(originalX, originalY)
                } else {
                    resistances[x][y] = 1f
                }
            }
        }

        //manually set the radius to equal the force
        light = RenderConfig.fov.calculateFOV(resistances, RenderConfig.enemySightRadiusX, RenderConfig.enemySightRadiusY, 10f, 0.3f, RenderConfig.strat)
        lightLastCalculated = Game.gameTurn
    }

    @Override
    int getGameTurn() {
        return gameTurn
    }

    @Override
    void setGameTurn(int gameTurn) {
        this.gameTurn = gameTurn
    }

    @Override
    int getSpeed() {
        return speed
    }

    int setSpeed(int speed) {
        this.speed = speed
    }

    @Override
    Entity getEntity() {
        return entity
    }

    @Override
    void setEntity(Entity entity) {
        this.entity = entity
    }

}
