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
        int x = owner.x
        int y = owner.y

        if (intensity <= 0 ||
                owner.levelMap.getTileType(x, y).water) {
            owner.levelMap.remove(owner)
            return false
        }

        owner.levelMap.ground[owner.x][owner.y].groundCover = GroundCover.ASH
        // If we have fuel, turn it into intensity


        Facing2d spreadDirection = Facing2d.randomCardinal()
        int nextX = owner.x + spreadDirection.x
        int nextY = owner.y + spreadDirection.y

        // do we have fire there?  do we have burnable stuff?
        if (!owner.levelMap.getTileType(nextX, nextY).blocks) {


            Entity existing = owner.levelMap.getEntitiesAtLocation(nextX, nextY).find { Entity e ->
                e?.ai?.class == FlameAi.class
            }

            if (!existing) {
                // TODO: light up
                Entity newFire = new Entity(
                        x: nextX,
                        y: nextY,
                        map: owner.levelMap,
                        name: 'fire',
                        ch: '^',
                        color: SColor.RED,
                        ai: new FlameAi(intensity-1, 10),
                )
                owner.levelMap.add(newFire)
            }
        }

        intensity--;
        gameTurn += speed

        return true

    }
}
