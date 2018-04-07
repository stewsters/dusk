package com.stewsters.dusk.game.renderSystems

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.core.graphic.Message
import com.stewsters.dusk.game.RenderConfig
import groovy.transform.CompileStatic
import org.apache.commons.lang3.text.WordUtils
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

@CompileStatic
class MessageLogSystem {

    private static LinkedList<Message> gameMessages = new LinkedList<>()

    static void send(String message, SColor color = SColor.WHITE, List<Entity> concerning = []) {
        WordUtils.wrap('>' + message, RenderConfig.screenWidth - RenderConfig.leftWindow - 1).eachLine {
            gameMessages.addLast(new Message(message: it, color: color, concerning: concerning))

            if (gameMessages.size() > RenderConfig.messageHeight)
                gameMessages.poll()
        }
    }

    static void render(SwingPane display, Entity player = null) {

        (RenderConfig.leftWindow..RenderConfig.screenWidth - 1).each { int x ->
            (0..RenderConfig.messageHeight).each { int y ->
                display.clearCell(x, y)
            }
        }

        List<Message> displayedMessages
        if (player) {
            displayedMessages = gameMessages.findAll {
                it.concerning && it.concerning.contains(player)
            }
        } else {
            displayedMessages = gameMessages
        }

        displayedMessages.eachWithIndex { Message msg, Integer index ->
            display.placeHorizontalString(RenderConfig.leftWindow + 1, index, msg.message, msg.color, SColor.BLACK)
        }


    }

    /* Should consider actually using some kind of logging framework*/

    static void log(String message) {
        println message
    }
}
