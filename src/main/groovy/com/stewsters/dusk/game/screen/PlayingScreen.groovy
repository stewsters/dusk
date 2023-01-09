package com.stewsters.dusk.game.screen

import com.stewsters.dusk.core.component.Weapon
import com.stewsters.dusk.core.component.ai.Ai
import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.TileType
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.core.map.WorldMap
import com.stewsters.dusk.game.Game
import com.stewsters.dusk.game.renderSystems.*
import com.stewsters.util.math.Facing2d
import com.stewsters.util.math.Point2i
import com.stewsters.util.shadow.twoDimention.ShadowCaster2d
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static com.stewsters.util.math.Facing2d.*
import static java.awt.event.KeyEvent.*

@CompileStatic
class PlayingScreen implements Screen {

    private static final int a = VK_A
    private static final int z = VK_Z

    public WorldMap mapStack

    public Entity player
    public ShadowCaster2d shadowCaster2d
    public ScreenMode screenMode

    private int selectedItem = -1


    PlayingScreen(WorldMap mapStack, Entity player) {

        this.mapStack = mapStack
        this.player = player

        shadowCaster2d = new ShadowCaster2d(levelMap)
        shadowCaster2d.recalculateFOV(player.x, player.y, 10, 0.3f)

        screenMode = ScreenMode.PLAYING
    }

    LevelMap getLevelMap() {
        mapStack.getLevelMapAt(mapStack.currentX, mapStack.currentY, mapStack.currentZ)
    }


    @Override
    void displayOutput(SwingPane display) {

        // Render the map
        MapRenderSystem.render(display, levelMap, player)

        // Render the UI
        LeftStatBarSystem.render(display, levelMap, player)

        MessageLogSystem.render(display, player)

        InventoryRenderSystem.render(display, player, screenMode, selectedItem)

        ItemsStandingOnRenderSystem.render(display, levelMap, player)

        BottomBarRenderSystem.render(display, mapStack.currentZ)

        //done rendering this frame
    }


    @Override
    Screen respondToUserInput(KeyEvent e) {

        int code = e.getExtendedKeyCode()

        // if ExtendedKeyCode is VK_UNDEFINED (0) use normal keycode
        if (code == VK_UNDEFINED) {
            code = e.getKeyCode()
        }

        boolean shift = e.isShiftDown()

        if (!player?.fighter?.hp) {
            // You are dead
            if (code == VK_SPACE) {
                return new DeathScreen(player)
            }
        } else if (screenMode == ScreenMode.PLAYING) {

            switch (code) {
                case VK_Q:
                    // Auto
                    stepSim()
                    break;
            //movement
                case VK_H:
                case VK_LEFT:
                case VK_NUMPAD4:
                    if (move(WEST, shift)) stepSim()
                    break
                case VK_L:
                case VK_RIGHT:
                case VK_NUMPAD6:
                    if (move(EAST, shift)) stepSim()
                    break
                case VK_K:
                case VK_UP:
                case VK_NUMPAD8:
                    if (move(NORTH, shift)) stepSim()
                    break
                case VK_J:
                case VK_DOWN:
                case VK_NUMPAD2:
                    if (move(SOUTH, shift)) stepSim()
                    break
                case VK_B:
                case VK_NUMPAD1:
                    if (move(SOUTHWEST, shift)) stepSim()
                    break
                case VK_N:
                case VK_NUMPAD3:
                    if (move(SOUTHEAST, shift)) stepSim()
                    break
                case VK_NUMPAD5:
                case VK_SLASH:
                    if (player.standStill()) stepSim() // waste time
                    break
                case VK_Y:
                case VK_NUMPAD7:
                    if (move(NORTHWEST, shift)) stepSim()
                    break
                case VK_U:
                case VK_NUMPAD9:
                    if (move(NORTHEAST, shift)) stepSim()
                    break
                case VK_G:
                    if (player.grab()) stepSim() //pick up item
                    break
                case VK_F:
                    if (fire()) stepSim() //use a bow or spell
                    break
                case VK_COMMA:
                    if (player.levelMap.isType(player.x, player.y, TileType.STAIRS_UP) && mapStack.levelExists(levelMap.chunkX, levelMap.chunkY, levelMap.chunkZ + 1)) {

                        // Ascend
                        Point2i dest = mapStack.getLevelMapAt(mapStack.currentX, mapStack.currentY, mapStack.currentZ + 1).findATile(TileType.STAIRS_DOWN)
                        levelMap.remove(player)

                        mapStack.currentZ++

                        MessageLogSystem.send("${player.name} has ascended from the depths.", SColor.BABY_BLUE, [player])

                        player.ai.gameTurn = levelMap.actors.peek()?.gameTurn ?: player.ai.gameTurn

                        player.levelMap = levelMap
                        player.x = dest.x
                        player.y = dest.y
                        levelMap.add(player)

                        shadowCaster2d = new ShadowCaster2d(levelMap)

                        getLevelMap().incrementTurn()
                        shadowCaster2d.recalculateFOV(player.x, player.y, 10, 0.3f)

                        stepSim()
                    } else {
                        MessageLogSystem.send("Cannot ascend here", SColor.WHITE, [player])
                    }
                    break
                case VK_PERIOD:

                    if (player.levelMap.isType(player.x, player.y, TileType.STAIRS_DOWN) && mapStack.currentZ > 0) {
                        // Descend
                        Point2i dest = mapStack.getLevelMapAt(mapStack.currentX, mapStack.currentY, mapStack.currentZ - 1).findATile(TileType.STAIRS_UP)
                        levelMap.remove(player)

                        mapStack.currentZ--

                        MessageLogSystem.send("${player.name} as descended back into the depths.", SColor.RED_BEAN, [player])

                        player.ai.gameTurn = levelMap.actors.peek()?.gameTurn ?: player.ai.gameTurn

                        player.levelMap = levelMap
                        player.x = dest.x
                        player.y = dest.y
                        levelMap.add(player)

                        shadowCaster2d = new ShadowCaster2d(levelMap)

                        getLevelMap().incrementTurn()
                        shadowCaster2d.recalculateFOV(player.x, player.y, 10, 0.3f)

                        stepSim()
                    } else {
                        MessageLogSystem.send("Cannot descend here", SColor.WHITE, [player])
                    }
                    break
                case VK_I:
                    screenMode = ScreenMode.INVENTORY //use a bow or spell
                    break
                case VK_A:
                    screenMode = ScreenMode.APPLY
                    break
                case VK_E:
                    screenMode = ScreenMode.EQUIP
                    break
                case VK_R:
                    screenMode = ScreenMode.REMOVE
                    break
                case VK_T:
                    screenMode = ScreenMode.THROW
                    break
                case VK_D:
                    screenMode = ScreenMode.DROP
                    break
                case VK_M:
                    screenMode = ScreenMode.CAST
                    break
                case VK_ESCAPE:
                    return new EscapeScreen(this)
                    break
                case VK_P:
                    return new LevelUpScreen(this)
                default:
                    break

            }
        } else if (screenMode == ScreenMode.INVENTORY) {

            if (code == VK_ESCAPE) {
                selectedItem = -1
                screenMode = ScreenMode.PLAYING
            } else if (code >= a && code <= z) {
                selectedItem = code - a
                if (player.inventory.hasItemById(selectedItem)) {
                    screenMode = ScreenMode.INVENTORY_INSPECT
                }
            }
        } else if (screenMode == ScreenMode.INVENTORY_INSPECT) {
            if (code == VK_ESCAPE) {
                selectedItem = -1
                screenMode = ScreenMode.INVENTORY
            } else {
                switch (code) {
                    case VK_I:
                        screenMode = ScreenMode.INVENTORY //use a bow or spell
                        break
                    case VK_A:
                        if (player.inventory.useById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break
                    case VK_E:
                        if (player.inventory.equipById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break
                    case VK_R:
                        if (player.inventory.removeById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break
                    case VK_T:
                        if (player.inventory.throwItemById(selectedItem, 2, 2)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break
                    case VK_D:
                        if (player.inventory.dropItemById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break
                }

            }

        } else if (screenMode == ScreenMode.APPLY) {
            if (code == VK_ESCAPE) {
                screenMode = ScreenMode.PLAYING

            } else if (code >= a && code <= z) {
                if (player.inventory.useById(code - a)) {
                    screenMode = ScreenMode.PLAYING
                    stepSim()
                }
            }
        } else if (screenMode == ScreenMode.EQUIP) {
            if (code == VK_ESCAPE) {
                screenMode = ScreenMode.PLAYING

            } else if (code >= a && code <= z) {
                if (player.inventory.equipById(code - a)) {
                    screenMode = ScreenMode.PLAYING
                    stepSim()
                }
            }
        } else if (screenMode == ScreenMode.REMOVE) {

            if (code == VK_ESCAPE) {
                screenMode = ScreenMode.PLAYING
            } else if (code >= a && code <= z) {
                if (player.inventory.removeById(code - a)) {
                    screenMode = ScreenMode.PLAYING
                    stepSim()
                }
            }
        } else if (screenMode == ScreenMode.THROW) {

            if (code == VK_ESCAPE) {
                screenMode = ScreenMode.PLAYING
            } else if (code >= a && code <= z) {
                if (player.inventory.throwItemById(code - a, 2, 2)) {
                    screenMode = ScreenMode.PLAYING
                    stepSim()
                }
            }
        } else if (screenMode == ScreenMode.DROP) {

            if (code == VK_ESCAPE) {
                screenMode = ScreenMode.PLAYING
            } else if (code >= a && code <= z) {
                if (player.inventory.dropItemById(code - a)) {
                    screenMode = ScreenMode.PLAYING
                    stepSim()
                }
            }
        } else if (screenMode == ScreenMode.CAST) {
            if (code == VK_ESCAPE) {
                screenMode = ScreenMode.PLAYING
            } else if (code >= a && code <= z) {
                if (player.spellbook.castSpell((char) code)) {
                    screenMode = ScreenMode.PLAYING
                    stepSim()
                }
            }
        }

        if (player.levelMap.isType(player.x, player.y, TileType.GAME_WIN)) {
            return new WinScreen(player)
        }

        return this
    }


    void stepSim() {
        //Run sim

        Ai next = levelMap.actors.poll()
        if (next == player.ai) {
            //next is the player now
            player.ai.takeTurn()
            levelMap.incrementTurn()
            shadowCaster2d.recalculateFOV(player.x, player.y, 10, 0.3f)
        }
        levelMap.actors.add next
    }

    @Override
    boolean autoplay() {
        return levelMap.actors.peek() != player.ai
    }

    @Override
    boolean play() {

        Ai next = levelMap.actors.poll()
        if (next != null && next.takeTurn()) {
            levelMap.actors.add(next)
        }

        Game.passTime()
        return true
    }

    boolean move(Facing2d dir, boolean shift = false) {

        int x = player.x + dir.x
        int y = player.y + dir.y

        //check for legality of move based solely on map boundary
        if (levelMap.contains(x, y)) {
            return player.moveOrAttack(dir.x, dir.y)
        }
        return false
    }

    boolean fire() {

        //select a target in view
        if (player.inventory) {
            Weapon weapon = player.inventory.getEquippedWeapon()
            if (weapon) {
                weapon.entity.item.useHeldItem(player)
                return true
            } else {
                MessageLogSystem.send("Find a weapon first.", SColor.RED, [player])
            }
        } else {
            MessageLogSystem.send("You can't use weapons.", SColor.RED, [player])
        }


        return false
    }


}
