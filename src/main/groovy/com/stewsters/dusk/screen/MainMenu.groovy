package com.stewsters.dusk.screen

import com.stewsters.dusk.component.*
import com.stewsters.dusk.component.ai.LocalPlayer
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.main.RenderConfig
import com.stewsters.dusk.map.MapStack
import com.stewsters.dusk.map.gen.MapGenerator
import com.stewsters.dusk.map.gen.TestMapGenerator
import com.stewsters.dusk.sfx.DeathFunctions
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.*

/**
 * Created by stewsters on 1/30/15.
 */
class MainMenu implements Screen {
    @Override
    void displayOutput(SwingPane display) {

        rightJustifiedText(display, 5, "Dusk of a Shattered Kingdom")
        rightJustifiedText(display, 10, "(N)ew Game")
        rightJustifiedText(display, 15, "(T)esting Arena")
        rightJustifiedText(display, 20, "E(x)it Game")
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
            MapGenerator mapGen = new TestMapGenerator();

            MapStack mapStack = new MapStack(10)

            10.times {
                mapStack.levelMaps[it] = mapGen.reGenerate()
            }


            Entity testPlayer = new Entity(map: mapStack.levelMaps[mapStack.currentLevel], x: mapGen.playerStartX, y: mapGen.playerStartY,
                    ch: '@', name: "Test Player", color: SColor.WHITE, blocks: true,
                    priority: Priority.PLAYER, faction: Faction.GOOD,
                    ai: new LocalPlayer(),
                    inventory: new Inventory(),
                    purse: new Purse(),
                    quiver: new Quiver(),
                    spellbook: new Spellbook(),
                    fighter: new Fighter(
                            hp: 10,
                            stamina: 10,
                            toxicity: 10,
                            melee: 1,
                            evasion: 1,
                            marksman: 1,
                            unarmedDamage: (1..4),
                            deathFunction: DeathFunctions.playerDeath)
            )

            return new DuskApplicationScreen(mapStack, testPlayer)
        } else if (code == VK_X) {
            System.exit(0)
        }

        return this
    }
}
