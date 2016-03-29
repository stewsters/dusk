package com.stewsters.dusk.component.ai

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.game.Game
import com.stewsters.dusk.main.RenderConfig
import com.stewsters.util.math.Point2i
import groovy.transform.CompileStatic

/**
 * These are functions common to all AIs
 */
@CompileStatic
abstract class BaseAi implements Ai {

    protected Entity owner
    protected float[][] light
    protected int lightLastCalculated = 0
    protected int sightRange = 20

    protected int speed = 10
    protected int gameTurn

    protected Point2i lastNoise;

    @Override
    public Set<Entity> findAllVisibleEnemies(int maxDistance) {
        if (!owner.faction) return null

        calculateSight()

        int lowXDist = owner.x - maxDistance
        int highXDist = owner.x + maxDistance
        int lowYDist = owner.y - maxDistance
        int highYDist = owner.y + maxDistance

        int lowX = owner.x - sightRange
        int lowY = owner.y - sightRange

        return owner.levelMap.getEntitiesBetween(lowXDist, lowYDist, highXDist, highYDist).findAll { Entity entity ->

            if (entity.fighter && this.owner.faction?.hates(entity.faction) && this.owner.distanceTo(entity) < maxDistance) {
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
    public Entity findClosestVisibleEnemy() {
        if (!owner.faction) return null

        calculateSight()

        int lowX = owner.x - sightRange
        int highX = owner.x + sightRange
        int lowY = owner.y - sightRange
        int highY = owner.y + sightRange

        Entity enemy = null

        int maxDistance = Integer.MAX_VALUE

        for (Entity entity : owner.levelMap.getEntitiesBetween(lowX, lowY, highX, highY)) {
            if (entity.fighter && owner.faction?.hates(entity.faction)) {
                int lightX = entity.x - lowX
                int lightY = entity.y - lowY

                //TODO: this goes out of bounds.  Use advanced lighting?
                if (lightX >= 0 && lightX < light.length && lightY >= 0 && lightY < light[0].length && light[lightX][lightY] > 0f) {

                    int tempDist = owner.distanceTo(entity)
                    if (tempDist <= maxDistance) {
                        enemy = entity
                        maxDistance = tempDist
                    }
                }
            }
        }
        return enemy

    }

    @Override
    public Entity findClosestVisibleItem() {

        calculateSight()

        int lowX = owner.x - sightRange
        int highX = owner.x + sightRange
        int lowY = owner.y - sightRange
        int highY = owner.y + sightRange

        int maxDistance = Integer.MAX_VALUE
        Entity item = null

        for (Entity entity : owner.levelMap.getEntitiesBetween(lowX, lowY, highX, highY)) {

            if (entity.x > lowX && entity.x < highX &&
                    entity.y > lowY && entity.y < highY &&
                    entity.itemComponent) {
                int lightX = entity.x - lowX
                int lightY = entity.y - lowY
                if (light[lightX][lightY] > 0f) {

                    int tempDist = owner.distanceTo(entity)
                    if (tempDist <= maxDistance) {
                        item = entity
                        maxDistance = tempDist
                    }
                }
            }
        }
        return item

    }

    @Override
    public void calculateSight() {
        if (lightLastCalculated == Game.gameTurn)
            return

        int worldLowX = owner.x - sightRange //low is upper left corner
        int worldLowY = owner.y - sightRange

        int range = 2 * sightRange + 1 // this is the total size of the box

        //Get resistance from map
        float[][] resistances = new float[range][range];
        for (int x = 0; x < range; x++) {
            for (int y = 0; y < range; y++) {
                int originalX = x + worldLowX
                int originalY = y + worldLowY

                if (originalX >= 0 && originalX < owner.levelMap.getXSize()
                        && originalY >= 0 && originalY < owner.levelMap.getYSize()) {
                    resistances[x][y] = owner.levelMap.getOpacity(originalX, originalY)
                } else {
                    resistances[x][y] = 1f
                }
            }
        }

        //manually set the radius to equal the force
        light = RenderConfig.fov.calculateFOV(resistances, RenderConfig.windowRadiusX, RenderConfig.windowRadiusY, 10f, 0.3f, RenderConfig.strat);
        lightLastCalculated = Game.gameTurn
    }

    @Override
    public void hearNoise(int x, int y) {
        if (lastNoise) {
            println "heard noise"
            lastNoise.x = x;
            lastNoise.y = y;
        } else {
            lastNoise = new Point2i(x, y)
        }
    }

    @Override
    public Integer getGameTurn() {
        return gameTurn
    }

    @Override
    void setGameTurn(Integer gameTurn) {
        this.gameTurn = gameTurn
    }

    @Override
    Entity getOwner() {
        return owner
    }

    @Override
    void setOwner(Entity owner) {
        this.owner = owner
    }


}
