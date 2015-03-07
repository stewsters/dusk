package com.stewsters.dusk.main

import com.stewsters.dusk.ApplicationMain
import com.stewsters.dusk.screen.MainMenu
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.fov.BasicRadiusStrategy
import squidpony.squidgrid.fov.EliasLOS
import squidpony.squidgrid.fov.TranslucenceWrapperFOV

import javax.imageio.ImageIO
import javax.swing.*
import java.awt.Font

public class DuskApplicationMain {

    public static void main(String[] args) {

        // setup configs
        RenderConfig.loadConfig()

        RenderConfig.fov = new TranslucenceWrapperFOV()
        RenderConfig.los = new EliasLOS()
        RenderConfig.strat = BasicRadiusStrategy.CIRCLE

        SColorFactory.addPallet("light", SColorFactory.asGradient(RenderConfig.litNear, RenderConfig.litFarDay))
        SColorFactory.addPallet("dark", SColorFactory.asGradient(RenderConfig.litNear, RenderConfig.litFarNight))

        //PlayScreen
        def app = new ApplicationMain(new MainMenu(), new Font(
                RenderConfig.config.font.name as String,
                Font.BOLD,
                RenderConfig.config.font.size as int))

        app.setTitle(RenderConfig.config.title)
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

        try {
            app.setIconImage(ImageIO.read(new File("./assets/icon.png")));
        } catch (IOException ex) {
//            don't do anything if it failed, the default Java icon will be used
        }
//        app.setDefaultLookAndFeelDecorated(true);

       app.dispose()

        //fullscreen
//        app.setExtendedState(Frame.MAXIMIZED_BOTH)
//        app.setUndecorated(true)
//        http://docs.oracle.com/javase/tutorial/extra/fullscreen/index.html

        app.setVisible(true);
        app.requestFocusInWindow()

    }

}
