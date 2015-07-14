package com.stewsters.dusk.screen

import com.stewsters.dusk.component.*
import com.stewsters.dusk.component.ai.LocalPlayer
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.main.RenderConfig
import com.stewsters.dusk.map.MapStack
import com.stewsters.dusk.map.gen.JailMapGenerator
import com.stewsters.dusk.map.gen.MapGenerator
import com.stewsters.dusk.map.gen.SimpleMapGenerator
import com.stewsters.dusk.map.gen.SurfaceMapGenerator
import com.stewsters.dusk.map.gen.TestMapGenerator
import com.stewsters.dusk.sfx.DeathFunctions
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.*

class MainMenu implements Screen {
    @Override
    void displayOutput(SwingPane display) {

        rightJustifiedText(display, 5, "Dusk of a Shattered Kingdom")
        rightJustifiedText(display, 10, "(N)ew Game")
        rightJustifiedText(display, 15, "E(x)it Game")
        rightJustifiedText(display, 45, "(T)esting Arena")

    }


    void rightJustifiedText(SwingPane display, int y, String txt) {
        int startX = RenderConfig.screenWidth - txt.length() - 1
        display.placeHorizontalString(startX, y, txt)
    }

    @Override
    Screen respondToUserInput(KeyEvent key) {
        int code = key.getExtendedKeyCode()

        // if ExtendedKeyCode is VK_UNDEFINED (0) use normal keycode
        if (code == VK_UNDEFINED) {
            code = key.getKeyCode()
        }

        if (code == VK_N) {
            return new CharacterGeneration()

        } else if (code == VK_T) {

            MapStack mapStack = new MapStack(4)
            MapGenerator mapgen = new TestMapGenerator();

            mapStack.levelMaps[0] = mapgen.reGenerate(0)
            mapStack.levelMaps[1] = new JailMapGenerator().reGenerate(1)
            mapStack.levelMaps[2] = new SimpleMapGenerator().reGenerate(2)
            mapStack.levelMaps[3] = new SurfaceMapGenerator().reGenerate(3)


            Entity testPlayer = new Entity(map: mapStack.levelMaps[mapStack.currentLevel], x: mapgen.playerStartX, y: mapgen.playerStartY,
                    ch: '@', name: "Test Player", color: SColor.WHITE, blocks: true,
                    priority: Priority.PLAYER, faction: Faction.GOOD,
                    ai: new LocalPlayer(),
                    inventory: new Inventory(),
                    purse: new Purse(),
                    quiver: new Quiver(),
                    spellbook: new Spellbook(),
                    fighter: new Fighter(
                            hp: 30,
                            stamina: 10,
                            toxicity: 10,
                            melee: 1,
                            evasion: 1,
                            marksman: 1,
                            unarmedDamage: (1..4),
                            deathFunction: DeathFunctions.playerDeath)
            )

            return new PlayingScreen(mapStack, testPlayer)
        } else if (code == VK_X) {
            System.exit(0)
        }

        return this
    }
}
