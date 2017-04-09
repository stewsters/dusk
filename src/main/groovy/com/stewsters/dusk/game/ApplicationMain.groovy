package com.stewsters.dusk.game

import com.stewsters.dusk.game.screen.Screen
import groovy.transform.CompileStatic
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

@CompileStatic
class ApplicationMain extends JFrame implements KeyListener, MouseInputListener {

    private SwingPane display
    private Screen screen

    ApplicationMain(Screen startScreen, Font font) {
        super()

        getContentPane().setBackground(SColorFactory.dimmest(SColor.DARK_INDIGO))

        display = new SwingPane()
        display.initialize(RenderConfig.screenWidth, RenderConfig.screenHeight, font)
        add(display)
        pack()

        Dimension d = display.getCellDimension()
        MouseInputListener mil = new SGMouseListener((int) d.getWidth(), (int) d.getHeight(), this)
        display.addMouseListener(mil) //listens for clicks and releases
        display.addMouseMotionListener(mil) //listens for movement based events

        addKeyListener(this)

        screen = startScreen

        repaint()
    }

    void repaint() {

        clear()
        screen.displayOutput(display)
        display.refresh()
        super.repaint()
    }

    void clear() {
        for (int x = 0; x < RenderConfig.screenWidth; x++) {
            for (int y = 0; y < RenderConfig.screenHeight; y++) {
                display.clearCell(x, y)
            }
        }
    }

    /*
     * KEYBOARD
     */

    @Override
    void keyPressed(KeyEvent e) {

        screen = screen.respondToUserInput(e)

        if (screen == null) {
            throw new NullPointerException("Screen must always return another one.")
        }

        while (screen.autoplay()) {
            screen.play()
        }

        repaint()
    }

    @Override
    void keyReleased(KeyEvent e) {
    }

    @Override
    void keyTyped(KeyEvent e) {
    }

    /*
     * MOUSE
     */

    @Override
    void mouseClicked(MouseEvent e) {

    }

    @Override
    void mousePressed(MouseEvent e) {

    }

    @Override
    void mouseReleased(MouseEvent e) {

    }

    @Override
    void mouseEntered(MouseEvent e) {

    }

    @Override
    void mouseExited(MouseEvent e) {

    }

    @Override
    void mouseDragged(MouseEvent e) {

    }

    @Override
    void mouseMoved(MouseEvent e) {

    }
}