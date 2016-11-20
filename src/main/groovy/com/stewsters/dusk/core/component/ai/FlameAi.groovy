package com.stewsters.dusk.core.component.ai

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.GroundCover
import com.stewsters.util.math.Facing2d
import squidpony.squidcolor.SColor

public class FlameAi extends BaseAi implements Ai {

    int intensity

    public FlameAi(int intensity, int speed) {
        this.speed = speed
        this.intensity = intensity
    }

    @Override
    boolean takeTurn() {
        int x = entity.x
        int y = entity.y

        if (intensity <= 0 ||
                entity.levelMap.getTileType(x, y).water) {
            entity.levelMap.remove(entity)
            return false
        }

        entity.levelMap.ground[entity.x][entity.y].groundCover = GroundCover.ASH
        // If we have fuel, turn it into intensity


        Facing2d spreadDirection = Facing2d.randomCardinal()
        int nextX = entity.x + spreadDirection.x
        int nextY = entity.y + spreadDirection.y

        // do we have fire there?  do we have burnable stuff?
        if (!entity.levelMap.getTileType(nextX, nextY).blocks) {


            Entity existing = entity.levelMap.getEntitiesAtLocation(nextX, nextY).find { Entity e ->
                e?.ai?.class == FlameAi.class
            }

            if (!existing) {
                // TODO: light up
                Entity newFire = new Entity(
                        x: nextX,
                        y: nextY,
                        map: entity.levelMap,
                        name: 'fire',
                        ch: '^',
                        color: SColor.RED,
                        ai: new FlameAi(intensity - 1, 10),
                )
                entity.levelMap.add(newFire)
            }
        }

        intensity--;
        gameTurn += speed

        return true

    }
}
