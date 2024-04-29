package com.thomas7520.remindclockhud.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class RemindClockConfig {

    public static final String CLOCK_CATEGORY = "clock";
    public static final String CHRONOMETER_CATEGORY = "chronometer";

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;


    static {
        Pair<Client,ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);

        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();


    }
    
    public static class Client {
        public Clock clock = new Clock();
        public Chronometer chronometer = new Chronometer();
        public static class Clock {
            public ForgeConfigSpec.BooleanValue enable;
            public ForgeConfigSpec.ConfigValue<String> formatText;
            public ForgeConfigSpec.BooleanValue drawBackground;
            public ForgeConfigSpec.BooleanValue use12HourFormat;
            public ForgeConfigSpec.EnumValue<HUDMode> rgbModeText, rgbModeBackground;
            public ForgeConfigSpec.IntValue redText, greenText, blueText, alphaText, rgbSpeedText;
            public ForgeConfigSpec.IntValue redBackground, greenBackground, blueBackground, alphaBackground, rgbSpeedBackground;
            public ForgeConfigSpec.BooleanValue textRightToLeftDirection, backgroundRightToLeftDirection;
            public ForgeConfigSpec.DoubleValue posX;
            public ForgeConfigSpec.DoubleValue posY;
        }

        public static class Chronometer {
            public ForgeConfigSpec.BooleanValue enable;

            public ForgeConfigSpec.EnumValue<ChronometerFormat> format;
            public ForgeConfigSpec.BooleanValue drawBackground;
            public ForgeConfigSpec.EnumValue<HUDMode> rgbModeText, rgbModeBackground;
            public ForgeConfigSpec.IntValue redText, greenText, blueText, alphaText, rgbSpeedText;
            public ForgeConfigSpec.IntValue redBackground, greenBackground, blueBackground, alphaBackground, rgbSpeedBackground;
            public ForgeConfigSpec.BooleanValue textRightToLeftDirection, backgroundRightToLeftDirection, idleRender;
            public ForgeConfigSpec.DoubleValue posX;
            public ForgeConfigSpec.DoubleValue posY;
        }


        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Clock settings").push(CLOCK_CATEGORY);

            clock.enable = builder.comment("Enable chronometer").define("enable", true);

            clock.formatText = builder.comment("Clock format.").define("formatText", "%day %dd %smonth %yyyy %hh:%mm:%ss");

            clock.drawBackground = builder.comment("Allow to draw background for text.").define("drawBackground", true);

            clock.use12HourFormat = builder.comment("Use 12 hour format or 24 format.").define("use12HourFormat", false);

            clock.rgbModeText = builder.comment("RGB Mode Text (Static, Wave, Cycle)").defineEnum("rgbModeText", HUDMode.STATIC);
            clock.rgbModeBackground = builder.comment("RGB Mode Background (Static, Wave, Cycle)").defineEnum("rgbModeBackground", HUDMode.STATIC);

            clock.redText = builder.defineInRange("redText", 100, 0, 255);
            clock.greenText = builder.defineInRange("greenText", 100, 0, 255);
            clock.blueText = builder.defineInRange("blueText", 100, 0, 255);
            clock.alphaText = builder.defineInRange("alphaText", 100, 0, 255);
            clock.rgbSpeedText = builder.defineInRange("rgbSpeedText", 50, 0, 100);

            clock.redBackground = builder.defineInRange("redBackground", 100, 0, 255);
            clock.greenBackground = builder.defineInRange("greenBackground", 100, 0, 255);
            clock.blueBackground = builder.defineInRange("blueBackground", 100, 0, 255);
            clock.alphaBackground = builder.defineInRange("alphaBackground", 100, 0, 255);
            clock.rgbSpeedBackground = builder.defineInRange("rgbSpeedBackground", 50, 0, 100);

            clock.textRightToLeftDirection = builder.define("textRightToLeftDirection", false);
            clock.backgroundRightToLeftDirection = builder.define("backgroundRightToLeftDirection", false);

            clock.posX = builder.comment("Value of x screen in percentage.").defineInRange("posX",0, 0, 100d);
            clock.posY = builder.comment("Value of y screen in percentage.").defineInRange("posY", 0,0 , 100d);

            builder.pop();

            builder.comment("Chronometer settings").push(CHRONOMETER_CATEGORY);

            // TODO change to false after doing switch on/off
            chronometer.enable = builder.comment("Enable chronometer").define("enable", true);

            chronometer.format = builder.comment("Chronometer format.").defineEnum("format", ChronometerFormat.MN_SS);

            chronometer.drawBackground = builder.comment("Allow to draw background for text.").define("drawBackground", true);

            chronometer.rgbModeText = builder.comment("RGB Mode Text (Static, Wave, Cycle)").defineEnum("rgbModeText", HUDMode.STATIC);
            chronometer.rgbModeBackground = builder.comment("RGB Mode Background (Static, Wave, Cycle)").defineEnum("rgbModeBackground", HUDMode.STATIC);

            chronometer.redText = builder.defineInRange("redText", 100, 0, 255);
            chronometer.greenText = builder.defineInRange("greenText", 100, 0, 255);
            chronometer.blueText = builder.defineInRange("blueText", 100, 0, 255);
            chronometer.alphaText = builder.defineInRange("alphaText", 100, 0, 255);
            chronometer.rgbSpeedText = builder.defineInRange("rgbSpeedText", 50, 0, 100);

            chronometer.redBackground = builder.defineInRange("redBackground", 100, 0, 255);
            chronometer.greenBackground = builder.defineInRange("greenBackground", 100, 0, 255);
            chronometer.blueBackground = builder.defineInRange("blueBackground", 100, 0, 255);
            chronometer.alphaBackground = builder.defineInRange("alphaBackground", 100, 0, 255);
            chronometer.rgbSpeedBackground = builder.defineInRange("rgbSpeedBackground", 50, 0, 100);

            chronometer.textRightToLeftDirection = builder.define("textRightToLeftDirection", false);
            chronometer.backgroundRightToLeftDirection = builder.define("backgroundRightToLeftDirection", false);

            chronometer.idleRender = builder.comment("Enable chronometer render when it is idle.").define("idleRender", false);

            chronometer.posX = builder.comment("Value of x screen in percentage.").defineInRange("posX",0, 0, 100d);
            chronometer.posY = builder.comment("Value of y screen in percentage.").defineInRange("posY", 0,0 , 100d);
        }
    }
}
