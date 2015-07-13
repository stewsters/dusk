package com.stewsters.dusk.screen;

import squidpony.squidgrid.gui.swing.SwingPane;

import java.awt.event.KeyEvent;


public interface Screen {
    public void displayOutput(SwingPane display);

    public Screen respondToUserInput(KeyEvent key);


    /**
     * Should we keep going, or wait for player input?
     */
    default public boolean autoplay() {
        return false;
    }

    /**
     * Runs the next character's turn
     *
     * @return whethor we need to repaint the screen
     */
    default public boolean play() {
        return false;
    }
}