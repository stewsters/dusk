package com.stewsters.dusk.game.screen

import com.stewsters.dusk.core.entity.Entity
import com.stewsters.dusk.game.RenderConfig
import groovy.transform.CompileStatic
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.VK_SPACE
import static java.awt.event.KeyEvent.VK_UNDEFINED

@CompileStatic
class DeathScreen implements Screen {

    Entity player

    DeathScreen(Entity player) {
        this.player = player
    }

    @Override
    void displayOutput(SwingPane display) {

        rightJustifiedText(display, 5, "${player.name} is dead")
        rightJustifiedText(display, 15, "Push [space] to continue")
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
        if (code == VK_SPACE) {
            return new MainMenu()
        }

        return this
    }
}
