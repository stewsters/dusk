package com.stewsters.dusk.game.screen;

import squidpony.squidgrid.gui.swing.SwingPane;

import java.awt.event.KeyEvent;


public interface Screen {
    void displayOutput(SwingPane display);

    Screen respondToUserInput(KeyEvent key);


    /**
     * Should we keep going, or wait for player input?
     */
    default boolean autoplay() {
        return false;
    }

    /**
     * Runs the next character's turn
     *
     * @return whether we need to repaint the screen
     */
    default boolean play() {
        return false;
    }
}