package com.stewsters.dusk.screen

import com.stewsters.dusk.component.Equipment
import com.stewsters.dusk.component.ai.Ai
import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.flyweight.Faction
import com.stewsters.dusk.flyweight.Priority
import com.stewsters.dusk.flyweight.Slot
import com.stewsters.dusk.flyweight.TileType
import com.stewsters.dusk.game.Game
import com.stewsters.dusk.graphic.MessageLog
import com.stewsters.dusk.graphic.StatusBar
import com.stewsters.dusk.main.RenderConfig
import com.stewsters.dusk.map.LevelMap
import com.stewsters.dusk.map.MapStack
import com.stewsters.util.math.Point2i
import com.stewsters.util.shadow.twoDimention.ShadowCaster2d
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane
import squidpony.squidgrid.util.Direction

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.*
import static squidpony.squidgrid.util.Direction.*

public class PlayingScreen implements Screen {

    private static final int a = VK_A
    private static final int z = VK_Z

    public MapStack mapStack

    public Entity player
    public ShadowCaster2d shadowCaster2d
    public ScreenMode screenMode

    private int selectedItem = -1

    public PlayingScreen(MapStack mapStack, Entity player) {

        this.mapStack = mapStack;
        this.player = player;

        shadowCaster2d = new ShadowCaster2d(levelMap);
        shadowCaster2d.recalculateFOV(player.x, player.y, 10, 0.3f)

        screenMode = ScreenMode.PLAYING
    }

    public LevelMap getLevelMap() {
        mapStack.levelMaps[mapStack.currentLevel]
    }


    @Override
    void displayOutput(SwingPane display) {

        // Render the map
        levelMap.render(display, player)

        // Render the UI

        //Left Bar
        int linesTaken = 0 //renderStats(display, 0, player)

        List<Entity> nearbyEntities = new ArrayList<Entity>(levelMap.getEntitiesBetween(player.x - 10, player.y - 10, player.x + 10, player.y + 10).findAll {
            levelMap.getLight(it.x, it.y) > 0
        })

        nearbyEntities.sort {
            return Math.abs(player.x - it.x) + Math.abs(player.y - it.y) - ((float) it.priority.ordinal() / Priority.values().size())
        }

        nearbyEntities.each {
            linesTaken += renderStats(display, linesTaken, it)
        }

        MessageLog.render(display, player)

        //render inventory
        if (player.inventory) {

            switch (screenMode) {
                case screenMode.APPLY:
                    player.inventory.render(display, "Push (a-z) for more info. Esc to Cancel",)
                    break

                case screenMode.DROP:
                    player.inventory.render(display, "Drop what? (a-z), Esc to Cancel")
                    break

                case screenMode.INVENTORY:
                    player.inventory.render(display, "View an item (a-z), Esc to Cancel")
                    break

                case screenMode.INVENTORY_INSPECT:
                    player.inventory.render(display, "View an item (a-z), Esc to Cancel")
                    player.inventory.renderInspect(display, selectedItem)
                    break

                case screenMode.EQUIP:
                    player.inventory.render(display, "Equip what? (a-z), Esc to Cancel")
                    break

                case screenMode.REMOVE:
                    player.inventory.render(display, "Unequip what? (a-z), Esc to Cancel")
                    break

                case screenMode.THROW:
                    player.inventory.render(display, "Throw what? (a-z), Esc to Cancel")
                    break
            }
        }
        def names = (levelMap.getEntitiesAtLocation(player.x, player.y) - player).sort { Entity entity -> entity.priority }.name

        names.eachWithIndex { String name, Integer i ->
            if (i < RenderConfig.surroundingHeight) {
                display.placeHorizontalString(RenderConfig.mapScreenWidth + RenderConfig.leftWindow, RenderConfig.surroundingY + i, name)
            }
        }

        //Depth
        //Render bottom key reminder

        String text = "Level ${mapStack.currentLevel + 1}    (I)nventory (A)pply (E)quip (R)emove (D)rop (G)rab"
        display.placeHorizontalString(1, RenderConfig.mapScreenHeight - 2, text)

        //done rendering this frame
    }


    public int renderStats(SwingPane display, int verticalOffset, Entity entity) {

        int linesTaken = 0

        if (entity.ch) {
            display.placeHorizontalString(0, verticalOffset + linesTaken, "${entity.ch}: ${entity.name}")
            linesTaken++
        }

        if (entity?.fighter?.hp) {
            StatusBar.renderTextBar(display, 0, verticalOffset + linesTaken, 20, "Health", entity?.fighter?.hp ?: 0, entity?.fighter?.maxHP ?: 1, SColor.DARK_BLUE)
            linesTaken++
        }
        if (entity?.fighter?.stamina) {
            StatusBar.renderTextBar(display, 0, verticalOffset + linesTaken, 20, "Stamina", entity?.fighter?.stamina ?: 0, entity?.fighter?.maxStamina ?: 1, SColor.BLUE_VIOLET)
            linesTaken++
        }
        if (entity?.fighter?.toxicity) {
            StatusBar.renderTextBar(display, 0, verticalOffset + linesTaken, 20, "Toxicity", entity?.fighter?.toxicity ?: 0, entity?.fighter?.maxToxicity ?: 1, SColor.DARK_RED)
            linesTaken++
        }

        if (entity?.fighter?.weaknesses) {
            display.placeHorizontalString(0, verticalOffset + linesTaken, entity?.fighter?.weaknesses?.name?.join(", ") ?: "", SColor.RED, SColor.BLACK)
            linesTaken++
        }
        if (entity?.fighter?.resistances) {
            display.placeHorizontalString(0, verticalOffset + linesTaken, entity?.fighter?.resistances?.name?.join(", ") ?: "", SColor.GREEN, SColor.BLACK)
            linesTaken++
        }

        //if player, show money
        if (entity == player) {
            //money
            display.placeHorizontalString(0, verticalOffset + linesTaken, "Gold:${player?.purse?.gold ?: 0}")
            linesTaken++
        } else if (entity.faction == Faction.GOOD) {
            display.placeHorizontalString(0, verticalOffset + linesTaken, "(Ally)")
            linesTaken++
        }
//        else if(entity.ai.currentState) {
//            display.placeHorizontalString(0, verticalOffset + linesTaken, entity.ai.currentState)
//            linesTaken++
//        }


        if (linesTaken > 0)
            linesTaken++
        return linesTaken
    }

    @Override
    Screen respondToUserInput(KeyEvent e) {

        int code = e.getExtendedKeyCode();

        // if ExtendedKeyCode is VK_UNDEFINED (0) use normal keycode
        if (code == VK_UNDEFINED) {
            code = e.getKeyCode();
        }

        boolean shift = e.isShiftDown();

        if (!player?.fighter?.hp) {
            // You are dead
            if (code == VK_SPACE) {
                return new DeathScreen(player)
            }
        } else if (screenMode == ScreenMode.PLAYING) {
            switch (code) {
            //movement
                case VK_H:
                case VK_LEFT:
                case VK_NUMPAD4:
                    if (move(LEFT, shift)) stepSim()
                    break;
                case VK_L:
                case VK_RIGHT:
                case VK_NUMPAD6:
                    if (move(RIGHT, shift)) stepSim()
                    break;
                case VK_K:
                case VK_UP:
                case VK_NUMPAD8:
                    if (move(UP, shift)) stepSim()
                    break;
                case VK_J:
                case VK_DOWN:
                case VK_NUMPAD2:
                    if (move(DOWN, shift)) stepSim()
                    break;
                case VK_B:
                case VK_NUMPAD1:
                    if (move(DOWN_LEFT, shift)) stepSim()
                    break;
                case VK_N:
                case VK_NUMPAD3:
                    if (move(DOWN_RIGHT, shift)) stepSim()
                    break;
                case VK_NUMPAD5:
                    if (player.standStill()) stepSim() // waste time
                    break;
                case VK_Y:
                case VK_NUMPAD7:
                    if (move(UP_LEFT, shift)) stepSim()
                    break;
                case VK_U:
                case VK_NUMPAD9:
                    if (move(UP_RIGHT, shift)) stepSim()
                    break;
                case VK_G:
                    if (player.grab()) stepSim() //pick up item
                    break;
                case VK_F:
                    if (fire()) stepSim() //use a bow or spell
                    break;
                case VK_COMMA:
                    if (player.levelMap.isType(player.x, player.y, TileType.STAIRS_UP) && mapStack.levelMaps.length - 1 > mapStack.currentLevel) {

                        // Ascend
                        Point2i dest = mapStack.levelMaps[mapStack.currentLevel + 1].findATile(TileType.STAIRS_DOWN)
                        levelMap.remove(player)

                        mapStack.currentLevel++

                        MessageLog.send("${player.name} has ascended from the depths.", SColor.BABY_BLUE, [player])


                        player.ai.gameTurn = levelMap.actors.peek()?.gameTurn ?: player.ai.gameTurn

                        player.levelMap = levelMap
                        player.x = dest.x
                        player.y = dest.y


                        levelMap.add(player)

                        shadowCaster2d = new ShadowCaster2d(levelMap);

                        levelMap.incrementTurn();
                        shadowCaster2d.recalculateFOV(player.x, player.y, 10, 0.3f);

                        stepSim()
                    }
                    break
                case VK_PERIOD:

                    if (player.levelMap.isType(player.x, player.y, TileType.STAIRS_DOWN) && mapStack.currentLevel > 0) {
                        // Descend
                        Point2i dest = mapStack.levelMaps[mapStack.currentLevel - 1].findATile(TileType.STAIRS_UP)
                        levelMap.remove(player)

                        mapStack.currentLevel--

                        MessageLog.send("${player.name} as descended back into the depths.", SColor.RED_BEAN, [player])

                        player.ai.gameTurn = levelMap.actors.peek()?.gameTurn ?: player.ai.gameTurn

                        player.levelMap = levelMap
                        player.x = dest.x
                        player.y = dest.y
                        levelMap.add(player)

                        shadowCaster2d = new ShadowCaster2d(levelMap)

                        levelMap.incrementTurn()
                        shadowCaster2d.recalculateFOV(player.x, player.y, 10, 0.3f);

                        stepSim()
                    }
                    break
                case VK_I:
                    screenMode = ScreenMode.INVENTORY //use a bow or spell
                    break;
                case VK_A:
                    screenMode = ScreenMode.APPLY
                    break;
                case VK_E:
                    screenMode = ScreenMode.EQUIP
                    break;
                case VK_R:
                    screenMode = ScreenMode.REMOVE
                    break;
                case VK_T:
                    screenMode = ScreenMode.THROW
                    break;
                case VK_D:
                    screenMode = ScreenMode.DROP
                    break;
                case VK_ESCAPE:
                    return new EscapeScreen(this)
                    break

                default:
                    break;

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
                        break;
                    case VK_A:
                        if (player.inventory.useById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break;
                    case VK_E:
                        if (player.inventory.equipById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break;
                    case VK_R:
                        if (player.inventory.removeById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break;
                    case VK_T:
                        if (player.throwItemById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break;
                    case VK_D:
                        if (player.dropItemById(selectedItem)) {
                            screenMode = ScreenMode.PLAYING
                            stepSim()
                        }
                        break;
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
                if (player.throwItemById(code - a)) {
                    screenMode = ScreenMode.PLAYING
                    stepSim()
                }
            }
        } else if (screenMode == ScreenMode.DROP) {

            if (code == VK_ESCAPE) {
                screenMode = ScreenMode.PLAYING
            } else if (code >= a && code <= z) {
                if (player.dropItemById(code - a)) {
                    screenMode = ScreenMode.PLAYING
                    stepSim()
                }
            }
        }

        if (player.levelMap.isType(player.x, player.y, TileType.GAME_WIN)) {
            return new WinScreen(player)
        }

        return this;
    }


    public void stepSim() {
        //Run sim

        Ai next = levelMap.actors.poll()
        if (next == player.ai) {
            //next is the player now
            player.ai.takeTurn()
            levelMap.incrementTurn();
            shadowCaster2d.recalculateFOV(player.x, player.y, 10, 0.3f);

        }
        levelMap.actors.add next

    }

    @Override
    public boolean autoplay() {
        return levelMap.actors.peek() != player.ai
    }

    @Override
    public boolean play() {

        Ai next = levelMap.actors.poll()
        if (next != null && next.takeTurn()) {
            levelMap.actors.add(next)
        }

        Game.passTime()



        return true
    }

    public boolean move(Direction dir, boolean shift = false) {

        if (shift && player.fighter.stamina) {
            //TODO: instead of moving faster, this should keep moving until
            //  A hostile monster is visible
            //  A message is sent to the message log
            //  you are about to step on anything that is not regular floor

            // Double move
            player.fighter.stamina--
            int x = player.x + dir.deltaX
            int y = player.y + dir.deltaY

            //check for legality of move based solely on map boundary
            if (x >= 0 && x < levelMap.getXSize() && y >= 0 && y < levelMap.getYSize()) {
                player.moveOrAttack(dir.deltaX, dir.deltaY)
                //TODO: run can have issues with not stepping
            }
        }

        int x = player.x + dir.deltaX
        int y = player.y + dir.deltaY

        //check for legality of move based solely on map boundary
        if (x >= 0 && x < levelMap.getXSize() && y >= 0 && y < levelMap.getYSize()) {
            return player.moveOrAttack(dir.deltaX, dir.deltaY)
        }

        return false;
    }

    public boolean fire() {

        //select a target in view
        if (player.inventory) {
            Equipment weapon = player.inventory.getEquippedInSlot(Slot.PRIMARY_HAND)
            if (weapon) {
                weapon.owner.itemComponent.useHeldItem(player)
                return true
            } else {
                MessageLog.send("Find a weapon first.", SColor.RED, [player])
            }
        } else {
            MessageLog.send("You can't use weapons.", SColor.RED, [player])
        }


        return false
    }


}
