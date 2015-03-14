package com.stewsters.dusk.screen

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.main.RenderConfig
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.VK_SPACE
import static java.awt.event.KeyEvent.VK_UNDEFINED

class WinScreen implements Screen {

    Entity player;

    WinScreen(Entity player) {
        this.player = player
    }

    @Override
    void displayOutput(SwingPane display) {

        rightJustifiedText(display, 5, "${player.name} has escaped!")
        rightJustifiedText(display, 15, "Push [space] to continue")
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
        if (code == VK_SPACE) {
            return new MainMenu()
        }

        return this
    }
}
