package com.stewsters.dusk.game.screen

import com.stewsters.dusk.core.component.CoreStats
import com.stewsters.dusk.core.component.Fighter
import com.stewsters.dusk.core.component.Inventory
import com.stewsters.dusk.core.component.Purse
import com.stewsters.dusk.core.component.Quiver
import com.stewsters.dusk.core.component.Spellbook
import com.stewsters.dusk.core.component.ai.LocalPlayer
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.Faction
import com.stewsters.dusk.core.flyweight.Priority
import com.stewsters.dusk.core.magic.Cleanse
import com.stewsters.dusk.core.magic.Confusion
import com.stewsters.dusk.core.magic.Domination
import com.stewsters.dusk.core.magic.Fireball
import com.stewsters.dusk.core.magic.Healing
import com.stewsters.dusk.core.magic.HostileSummoning
import com.stewsters.dusk.core.magic.LightningStrike
import com.stewsters.dusk.core.magic.Mapping
import com.stewsters.dusk.core.magic.StoneCurse
import com.stewsters.dusk.core.magic.Summoning
import com.stewsters.dusk.core.magic.Wrath
import com.stewsters.dusk.core.map.WorldMap
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
        int startX = (int) (RenderConfig.screenWidth / 2) - (int) (txt.length() / 2)
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

            WorldMap mapStack = new WorldMap(1, 1, 4)
            MapGenerator mapgen = new TestMapGenerator()

            mapStack.setLevelMap(mapgen.reGenerate(0, 0, 0))
            mapStack.setLevelMap(new JailMapGenerator().reGenerate(0, 0, 1))
            mapStack.setLevelMap(new SimpleMapGenerator().reGenerate(0, 0, 2))
            mapStack.setLevelMap(new SurfaceMapGenerator().reGenerate(0, 0, 3))


            Entity testPlayer = new Entity(map: mapStack.getLevelMapAt(mapStack.currentX, mapStack.currentY, mapStack.currentZ),
                    x: mapgen.playerStartX, y: mapgen.playerStartY,
                    ch: '@' as char, name: "Test Player", color: SColor.WHITE, blocks: true,
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
                            deathFunction: DeathFunctions.playerDeath),
                    coreStats: new CoreStats(strength: 10, life: 10, stamina: 10)
            )

            testPlayer.spellbook.spells.addAll([
                    new Cleanse(),
                    new Confusion(),
                    new Domination(),
                    new Fireball(),
                    new Healing(),
                    new HostileSummoning(),
                    new LightningStrike(),
                    new Mapping(),
                    new StoneCurse(),
                    new Summoning(),
                    new Wrath()
            ])

            return new PlayingScreen(mapStack, testPlayer)
        } else if (code == VK_X) {
            System.exit(0)
        }

        return this
    }
}
