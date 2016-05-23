package com.stewsters.dusk

import com.stewsters.dusk.main.RenderConfig
import com.stewsters.dusk.screen.Screen
import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.gui.awt.event.SGMouseListener
import squidpony.squidgrid.gui.swing.SwingPane

import javax.swing.*
import javax.swing.event.MouseInputListener
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent

public class ApplicationMain extends JFrame implements KeyListener, MouseInputListener {

    private SwingPane display;
    private Screen screen;

    public ApplicationMain(Screen startScreen, Font font) {
        super();

        getContentPane().setBackground(SColorFactory.dimmest(SColor.DARK_INDIGO));

        display = new SwingPane();
        display.initialize(RenderConfig.screenWidth, RenderConfig.screenHeight, font);
        add(display);
        pack();

        Dimension d = display.getCellDimension();
        MouseInputListener mil = new SGMouseListener((int) d.getWidth(), (int) d.getHeight(), this);
        display.addMouseListener(mil); //listens for clicks and releases
        display.addMouseMotionListener(mil); //listens for movement based events

        addKeyListener(this);

        screen = startScreen;

        repaint();
    }

    public void repaint() {

        clear();
        screen.displayOutput(display);
        display.refresh();
        super.repaint();
    }

    public void clear() {
        for (int x = 0; x < RenderConfig.screenWidth; x++) {
            for (int y = 0; y < RenderConfig.screenHeight; y++) {
                display.clearCell(x, y);
            }
        }
    }

    /*
     * KEYBOARD
     */

    @Override
    public void keyPressed(KeyEvent e) {

        screen = screen.respondToUserInput(e);

        if (screen == null) {
            throw new NullPointerException("Screen must always return another one.");
        }

        while (screen.autoplay()) {
            screen.play()
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /*
     * MOUSE
     */

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}