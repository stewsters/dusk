package com.stewsters.dusk.screen

import com.stewsters.dusk.main.RenderConfig
import squidpony.squidgrid.gui.swing.SwingPane

import java.awt.event.KeyEvent

import static java.awt.event.KeyEvent.*

class LevelupScreen implements Screen {


    PlayingScreen playingScreen

    LevelupScreen(PlayingScreen playingScreen) {
        this.playingScreen = playingScreen
    }

    @Override
    void displayOutput(SwingPane display) {

        int x = 1
        int y = 1

        display.placeHorizontalString(x, y, "Name: " + playingScreen?.player?.name)
        y++


        display.placeHorizontalString(x, y, "XP: " + playingScreen?.player?.fighter?.experience)
        y+=2

        display.placeHorizontalString(x, y, "Melee: " + playingScreen?.player?.fighter?.skillMelee)
        y++

        display.placeHorizontalString(x, y, "Evasion: " + playingScreen?.player?.fighter?.skillEvasion)
        y++

        //TODO: marksman, you know, after there are bows or something

        display.placeHorizontalString(x, y, "HP: " + playingScreen?.player?.fighter?.baseMaxHp)
        y++

//        rightJustifiedText(display, 5, "[L]evel Up")
//        rightJustifiedText(display, 15, "[Escape] to continue")
//        rightJustifiedText(display, 25, "[x] Exit to main menu")
//        rightJustifiedText(display, 35, "Yeah, there is no saving.  Sorry.")


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

        if (code == VK_ESCAPE) {
            return playingScreen

        } else if (code == VK_M) {
            // boost melee
            if (playingScreen?.player?.fighter?.experience) {
                playingScreen.player.fighter.experience--
                playingScreen.player.fighter.skillMelee++
            }

        } else if (code == VK_E) {
            // boost evasion
            if (playingScreen?.player?.fighter?.experience) {
                playingScreen.player.fighter.experience--
                playingScreen.player.fighter.skillEvasion++
            }

        } else if (code == VK_H) {
            // boost HP
            if (playingScreen?.player?.fighter?.experience) {
                playingScreen.player.fighter.experience--
                playingScreen.player.fighter.baseMaxHp += 5
            }
        }



        return this
    }
}
