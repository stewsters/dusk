package com.stewsters.dusk.graphic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.main.RenderConfig
import org.apache.commons.lang3.text.WordUtils
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

public class MessageLog {


    private static LinkedList<Message> gameMessages = []

    public static void send(String message, SColor color = SColor.WHITE, List<Entity> concerning = []) {
        WordUtils.wrap('>' + message, RenderConfig.screenWidth - RenderConfig.leftWindow - 1).eachLine {
            gameMessages.addLast(new Message(message: it, color: color, concerning: concerning))

            if (gameMessages.size() > RenderConfig.messageHeight)
                gameMessages.poll()
        }
    }

    public static void render(SwingPane display, Entity player = null) {

        (RenderConfig.leftWindow..RenderConfig.screenWidth - 1).each { int x ->
            (0..RenderConfig.messageHeight).each { int y ->
                display.clearCell(x, y)
            }
        }

        def displayedMessages
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
