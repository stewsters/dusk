package com.stewsters.dusk.game.screen

import com.stewsters.dusk.core.component.Fighter
import com.stewsters.dusk.core.component.Inventory
import com.stewsters.dusk.core.component.Purse
import com.stewsters.dusk.core.component.Quiver
import com.stewsters.dusk.core.component.Spellbook
import com.stewsters.dusk.core.component.ai.LocalPlayer
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.Faction
import com.stewsters.dusk.core.flyweight.Priority
import com.stewsters.dusk.core.map.MapStack
import com.stewsters.dusk.core.map.gen.JailMapGenerator
import com.stewsters.dusk.core.map.gen.MapGenerator
import com.stewsters.dusk.core.map.gen.SimpleMapGenerator
import com.stewsters.dusk.core.map.gen.SurfaceMapGenerator
import com.stewsters.dusk.core.map.gen.TestMapGenerator
import com.stewsters.dusk.core.sfx.DeathFunctions
import com.stewsters.dusk.game.RenderConfig
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.VK_N
import static java.awt.event.KeyEvent.VK_T
import static java.awt.event.KeyEvent.VK_UNDEFINED
import static java.awt.event.KeyEvent.VK_X

@CompileStatic
class MainMenu implements Screen {

    @Override
    void displayOutput(SwingPane display) {

        centerJustifiedText(display, 5, "Dusk of a Shattered Kingdom")
        centerJustifiedText(display, 10, "N New Game")
        centerJustifiedText(display, 15, "X Exit Game")
        centerJustifiedText(display, 45, "T Testing Arena")

    }

    static void centerJustifiedText(SwingPane display, int y, String txt) {
        int startX = (int)(RenderConfig.screenWidth / 2) - (int)(txt.length() / 2)
        display.placeHorizontalString(startX, y, txt)
    }

    static void rightJustifiedText(SwingPane display, int y, String txt) {
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

            MapStack mapStack = new MapStack(1, 1, 4)
            MapGenerator mapgen = new TestMapGenerator()

            mapStack.levelMaps[0][0][0] = mapgen.reGenerate(0)
            mapStack.levelMaps[0][0][1] = new JailMapGenerator().reGenerate(1)
            mapStack.levelMaps[0][0][2] = new SimpleMapGenerator().reGenerate(2)
            mapStack.levelMaps[0][0][3] = new SurfaceMapGenerator().reGenerate(3)


            Entity testPlayer = new Entity(map: mapStack.levelMaps[mapStack.currentX][mapStack.currentY][mapStack.currentZ],
                    x: mapgen.playerStartX, y: mapgen.playerStartY,
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
