package com.stewsters.dusk.game.renderSystems

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.flyweight.Faction
import com.stewsters.dusk.core.flyweight.Priority
import com.stewsters.dusk.core.map.LevelMap
import com.stewsters.dusk.game.RenderConfig
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

@CompileStatic
class LeftStatBarSystem {

    static void render(SwingPane display, LevelMap levelMap, Entity player) {

        Integer linesTaken = 0

        List<Entity> nearbyEntities = new ArrayList<Entity>(levelMap.getEntitiesBetween(player.x - 10, player.y - 10, player.x + 10, player.y + 10).findAll {
            levelMap.getLight(it.x, it.y) > 0
        })

        nearbyEntities.sort {
            return Math.abs(player.x - it.x) + Math.abs(player.y - it.y) - ((float) it.priority.ordinal() / Priority.values().size())
        }

        nearbyEntities.each {
            linesTaken += renderStats(display, linesTaken, it, player)
        }

    }


    private static int renderStats(SwingPane display, int verticalOffset, Entity entity, Entity player) {

        int linesTaken = 0

        if (entity.ch && RenderConfig.screenHeight > verticalOffset + linesTaken) {
            display.placeCharacter(0, verticalOffset + linesTaken, entity.ch, entity.color, SColor.BLACK)
            display.placeHorizontalString(1, verticalOffset + linesTaken, ": ${entity.name}")
            linesTaken++
        }

        if (entity?.fighter?.hp != null && RenderConfig.screenHeight > verticalOffset + linesTaken) {
            renderTextBar(display, 0, verticalOffset + linesTaken, 20, "Health", entity?.fighter?.hp ?: 0, entity?.fighter?.maxHP ?: 1, SColor.DARK_BLUE)
            linesTaken++
        }

        if (entity?.fighter?.maxArmor && RenderConfig.screenHeight > verticalOffset + linesTaken) {
            renderTextBar(display, 0, verticalOffset + linesTaken, 20, "Armor", entity?.fighter?.armor ?: 0, entity?.fighter?.maxArmor ?: 0, SColor.LIGHT_BLUE)
            linesTaken++
        }

//        if (entity?.fighter?.stamina && RenderConfig.screenHeight > verticalOffset + linesTaken) {
//            renderTextBar(display, 0, verticalOffset + linesTaken, 20, "Stamina", entity?.fighter?.stamina ?: 0, entity?.fighter?.maxStamina ?: 1, SColor.BLUE_VIOLET)
//            linesTaken++
//        }
        if (entity?.fighter?.toxicity && RenderConfig.screenHeight > verticalOffset + linesTaken) {
            renderTextBar(display, 0, verticalOffset + linesTaken, 20, "Toxicity", entity?.fighter?.toxicity ?: 0, entity?.fighter?.maxToxicity ?: 1, SColor.DARK_RED)
            linesTaken++
        }

        if (entity?.fighter?.weaknesses && RenderConfig.screenHeight > verticalOffset + linesTaken) {
            display.placeHorizontalString(0, verticalOffset + linesTaken, "wkn:" + entity?.fighter?.weaknesses?.name?.join(", ") ?: "", SColor.RED, SColor.BLACK)
            linesTaken++
        }
        if (entity?.fighter?.resistances && RenderConfig.screenHeight > verticalOffset + linesTaken) {
            display.placeHorizontalString(0, verticalOffset + linesTaken, "res:" + entity?.fighter?.resistances?.name?.join(", ") ?: "", SColor.GREEN, SColor.BLACK)
            linesTaken++
        }

        //if player, show money
        if (entity == player && RenderConfig.screenHeight > verticalOffset + linesTaken) {
            //money
            display.placeHorizontalString(0, verticalOffset + linesTaken, "Gold:${player?.purse?.gold ?: 0}")
            linesTaken++
        } else if (entity.faction == Faction.GOOD && RenderConfig.screenHeight > verticalOffset + linesTaken) {
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


    static void render(SwingPane display, int x, int y, int totalWidth, String name, int value, int maximum, SColor barColor) {

        double ratio = (double) value / (double) maximum
        int barWidth = (int) Math.ceil(ratio * (double) totalWidth)

        totalWidth.times { xPos ->
            2.times { yPos ->
                display.clearCell(xPos + x, yPos + y)
            }
        }


        (0..(totalWidth - 1)).each { int xOffset ->
            if (xOffset < barWidth || value == maximum)
                display.placeCharacter(x + xOffset, y + 1, '#' as char, barColor)
            else
                display.placeCharacter(x + xOffset, y + 1, '-' as char, barColor)
        }
        display.placeHorizontalString(x, y, "${name}: ${value}/${maximum}")
    }

    //width 20
    static void renderTextOnly(SwingPane display, int x, int y, String name, int value, int maximum) {

        20.times { xPos ->
            display.clearCell(xPos + x, y)
        }

        display.placeHorizontalString(x, y, "${name}: ${value}/${maximum}")
    }


    static void renderTextBar(SwingPane display, int x, int y, int totalWidth, String barName, int value, int maximum, SColor barColor) {

        double ratio = (double) value / (double) maximum
        int barWidth = (int) Math.ceil(ratio * (double) totalWidth)

        String name = "$barName: ${value}/${maximum}"

        if (name.size() > totalWidth)
            name = name.substring(0, totalWidth)

        int leftJust = (int) ((totalWidth - name.size()) / 2)

        RenderConfig.leftWindow.times { int xPos ->

            int offset = xPos - leftJust

            char character

            if (offset < 0 || offset >= name.size()) {
                character = ' '
            } else {
                character = name.charAt(offset)
            }

            if (xPos < barWidth) {
                display.placeCharacter(x + xPos, y, character, SColor.WHITE, barColor)
            } else {
                display.placeCharacter(x + xPos, y, character, SColor.WHITE, SColor.BLACK)
            }

        }

//        display.placeHorizontalString(x, y, "${name}: ${value}/${maximum}")
    }

}
