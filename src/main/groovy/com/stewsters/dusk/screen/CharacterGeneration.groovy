package com.stewsters.dusk.screen

import com.stewsters.dusk.component.*
import com.stewsters.dusk.component.ai.LocalPlayer
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.*
import com.stewsters.dusk.magic.*
import com.stewsters.dusk.map.MapStack
import com.stewsters.dusk.map.gen.JailMapGenerator
import com.stewsters.dusk.map.gen.MapGenerator
import com.stewsters.dusk.map.gen.SimpleMapGenerator
import com.stewsters.dusk.map.gen.SurfaceMapGenerator
import com.stewsters.dusk.map.gen.name.KnightNameGen
import com.stewsters.dusk.screen.subscreen.ListSelector
import com.stewsters.dusk.sfx.DeathFunctions
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.*

class CharacterGeneration implements Screen {

    int pointerColumn = 0;

//    ListSelector<Race> raceSelect
    ListSelector<SocialClass> socialClassSelect
    ListSelector<Gender> genderSelect

    public CharacterGeneration() {
        //TODO: generate the map in another thread and wait for it

//        raceSelect = new ListSelector<>("Select Race", Race.values() as List)
        socialClassSelect = new ListSelector<>("Select Class", SocialClass.values() as List)
        genderSelect = new ListSelector<>("Select Gender", Gender.values() as List)

    }

    @Override
    void displayOutput(SwingPane display) {

//        raceSelect.render(display, 10, 10, pointerColumn == 0);
        socialClassSelect.render(display, 30, 10, pointerColumn == 0)
        genderSelect.render(display, 50, 10, pointerColumn == 1)

        // Whats your name today?  Generate

        display.placeHorizontalString(30, 30, "Push Space to begin your adventure")

    }


    @Override
    Screen respondToUserInput(KeyEvent key) {

        int code = key.getExtendedKeyCode();

        // if ExtendedKeyCode is VK_UNDEFINED (0) use normal keycode
        if (code == VK_UNDEFINED) {
            code = key.getKeyCode();
        }

        switch (code) {
            case VK_K:
            case VK_UP:
            case VK_NUMPAD8:
                switch (pointerColumn) {
//                    case 0:
//                        raceSelect.up()
//                        break
                    case 0:
                        socialClassSelect.up()
                        break
                    case 1:
                        genderSelect.up()
                        break
                }
                break
            case VK_J:
            case VK_DOWN:
            case VK_NUMPAD2:
                switch (pointerColumn) {
//                    case 0:
//                        raceSelect.down()
//                        break
                    case 0:
                        socialClassSelect.down()
                        break
                    case 1:
                        genderSelect.down()
                        break
                }
                break;
            case VK_H:
            case VK_LEFT:
            case VK_NUMPAD4:
                pointerColumn = Math.max(0, pointerColumn - 1)
                break;
            case VK_L:
            case VK_RIGHT:
            case VK_NUMPAD6:
                pointerColumn = Math.min(1, pointerColumn + 1)
                break
            case VK_SPACE:
                return startGame()
                break
            case VK_ESCAPE:
                return new MainMenu()
                break
        }

        return this
    }

    private Screen startGame() {

        MapStack mapStack = new MapStack(10)

        MapGenerator jailMapGen = new JailMapGenerator()
        MapGenerator simpleMapGen = new SimpleMapGenerator()
        MapGenerator surfaceMapGen = new SurfaceMapGenerator()
//        MapGenerator mapGen = new StaticMapGenerator();
//        MapGenerator mapGen = new TestMapGenerator();
//        mapGen = new SimpleMapGenerator()
//        MapGenerator mapGen = new CityMapGenerator()


        int playerStartX, playerStartY
        10.times {

            if (it == 0) {
                mapStack.levelMaps[it] = jailMapGen.reGenerate(it)
                playerStartX = jailMapGen.playerStartX
                playerStartY = jailMapGen.playerStartY

            } else if (it < 8) {
                mapStack.levelMaps[it] = simpleMapGen.reGenerate(it)
            } else {
                mapStack.levelMaps[it] = surfaceMapGen.reGenerate(it)
            }
        }

        String name = KnightNameGen.generate(genderSelect.selected)



        Entity player = new Entity(map: mapStack.levelMaps[mapStack.currentLevel],
                x: playerStartX, y: playerStartY,
                xSize: 1,ySize: 2,
                ch: '@', name: name, color: SColor.WHITE, blocks: true,
                priority: Priority.PLAYER, faction: Faction.GOOD,
                ai: new LocalPlayer(),
                inventory: new Inventory(),
                purse: new Purse(),
                quiver: new Quiver(),
                spellbook: new Spellbook(),
                fighter: new Fighter(
                        hp: (socialClassSelect.selected == SocialClass.PRIEST) ? 35 : 30,
                        stamina: 10,
                        toxicity: 10,
                        melee: (socialClassSelect.selected == SocialClass.KNIGHT) ? 2 : 1,
                        evasion: (socialClassSelect.selected == SocialClass.POACHER) ? 2 : 1,
                        marksman: 1,
                        unarmedDamage: (1..4),
                        deathFunction: DeathFunctions.playerDeath)
        )

        player.mover.owner = player

        player.spellbook.spells.addAll([
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


        Entity defaultArmor = new Entity(map: mapStack.levelMaps[mapStack.currentLevel], x: playerStartX, y: playerStartY,
                ch: '[', color: SColor.DARK_BLUE,
                name: 'Tattered Rags',
                description: "Rags covered in filth.",
                equipment: new Equipment(
                        slot: Slot.CHEST,
                        armor: (0..1)
                )
        )
        player.inventory.pickUp(defaultArmor)




        player.ai.owner = player
        return new PlayingScreen(mapStack, player)


    }


}
