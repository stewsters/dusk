package com.stewsters.dusk.screen;

import squidpony.squidgrid.gui.swing.SwingPane;

import java.awt.event.KeyEvent;


public interface Screen {
    public void displayOutput(SwingPane display);

    public Screen respondToUserInput(KeyEvent key);

    default public boolean autoplay() {
        return false;
    }

    default public boolean play() {
        return false;
    }
}