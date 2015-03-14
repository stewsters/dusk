package com.stewsters.dusk.screen

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.main.RenderConfig
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.VK_ESCAPE
import static java.awt.event.KeyEvent.VK_L
import static java.awt.event.KeyEvent.VK_SPACE
import static java.awt.event.KeyEvent.VK_UNDEFINED
import static java.awt.event.KeyEvent.VK_X

class EscapeScreen implements Screen {



    PlayingScreen playingScreen

    EscapeScreen(PlayingScreen playingScreen) {
        this.playingScreen = playingScreen
    }

    @Override
    void displayOutput(SwingPane display) {

        rightJustifiedText(display, 5, "[L]evel Up")
        rightJustifiedText(display, 15, "[Escape] to continue")
        rightJustifiedText(display, 25, "[x] Exit to main menu")
        rightJustifiedText(display, 35, "Yeah, there is no saving.  Sorry.")


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
        if (code == VK_X) {
            return new MainMenu()
        }
        else if(code==VK_ESCAPE){
            return playingScreen
        }else if(code==VK_L){
           return new LevelupScreen(playingScreen)
        }

        return this
    }
}
