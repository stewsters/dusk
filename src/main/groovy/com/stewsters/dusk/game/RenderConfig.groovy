package com.stewsters.dusk.game

import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.fov.LOSSolver
import squidpony.squidgrid.fov.RadiusStrategy
import squidpony.squidgrid.fov.TranslucenceWrapperFOV

/**
 * This file just really stores some configs
 */

public class RenderConfig {

    public static def config

    public static void loadConfig() {
        File propertiesFile = new File('assets/settings.props');
        config = new ConfigSlurper().parse(propertiesFile.text)

        screenWidth = config?.graphics?.width ?: 120
        screenHeight = config?.graphics?.height ?: 60
    }

    //total
    public static int screenWidth;
    public static int screenHeight;


    public static SColor litNear = SColorFactory.asSColor(SColor.WHITE);
    public static SColor litFarDay = SColorFactory.asSColor(SColor.LIGHT_YELLOW_DYE);
    public static SColor litFarNight = SColorFactory.asSColor(SColor.DARK_BLUE);

    public static float lightTintPercentage = 0.02f; //0 to 1

    public static LOSSolver los;
    public static TranslucenceWrapperFOV fov;
    public static RadiusStrategy strat;


    public static final int leftWindow = 20;

    public static final int windowRadiusX = 20;
    public static final int windowRadiusY = 20;

    // for game window
    public static final int mapScreenWidth = 100;
    public static final int mapScreenHeight = 60;

    //Messaging sizes
    public static final int messageHeight = 3;

    public static final int surroundingY = 42;
    public static final int surroundingHeight = 5;

    // Inventory screens should start just below the message log.
    public static final int inventoryY = messageHeight;
    public static final int inventoryMaxHeight = 26;
    public static final int inventoryWidth = 40;


}
