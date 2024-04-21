package com.thomas7520.remindtimerhud.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class RemindTimerConfig {

    public static final String CATEGORY_GENERAL = "general";

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;


    static {
        Pair<Client,ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);

        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }
    
    public static class Client {

        public ForgeConfigSpec.ConfigValue<String> formatText;
        public ForgeConfigSpec.BooleanValue drawText, drawBackground;
        public ForgeConfigSpec.BooleanValue use12HourFormat;
        public ForgeConfigSpec.EnumValue<HUDMode> rgbModeText, rgbModeBackground;
        public ForgeConfigSpec.IntValue redText, greenText, blueText, alphaText, rgbSpeedText;
        public ForgeConfigSpec.IntValue redBackground, greenBackground, blueBackground, alphaBackground, rgbSpeedBackground;
        public ForgeConfigSpec.BooleanValue textRightToLeftDirection, backgroundRightToLeftDirection;
        public ForgeConfigSpec.EnumValue<HUDPosition.EnumWidth> textWidth, backgroundWidth;
        public ForgeConfigSpec.EnumValue<HUDPosition.EnumHeight> textHeight, backgroundHeight;
        public ForgeConfigSpec.ConfigValue<Float> posX, posY;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings").push(CATEGORY_GENERAL);

            formatText = builder.comment("The format of the text that will be viewed in ui player.").define("formatText", "%day %dd %smonth %yyyy %hh:%mm:%ss");

            drawText = builder.comment("Allow to draw text.").define("drawText", true);
            drawBackground = builder.comment("Allow to draw background for text.").define("drawBackground", true);

            use12HourFormat = builder.comment("Use 12 hour format or 24 format according to this value.").define("use12HourFormat", false);

            rgbModeText = builder.comment("RGB Mode Text (Static, Wave, Cycle)").defineEnum("rgbModeText", HUDMode.STATIC);
            rgbModeBackground = builder.comment("RGB Mode Background (Static, Wave, Cycle)").defineEnum("rgbModeBackground", HUDMode.STATIC);

            redText = builder.defineInRange("redText", 100, 0, 255);
            greenText = builder.defineInRange("greenText", 100, 0, 255);
            blueText = builder.defineInRange("blueText", 100, 0, 255);
            alphaText = builder.defineInRange("alphaText", 100, 0, 255);
            rgbSpeedText = builder.defineInRange("rgbSpeedText", 50, 0, 100);

            redBackground = builder.defineInRange("redBackground", 100, 0, 255);
            greenBackground = builder.defineInRange("greenBackground", 100, 0, 255);
            blueBackground = builder.defineInRange("blueBackground", 100, 0, 255);
            alphaBackground = builder.defineInRange("alphaBackground", 100, 0, 255);
            rgbSpeedBackground = builder.defineInRange("rgbSpeedBackground", 50, 0, 100);

            textRightToLeftDirection = builder.define("textRightToLeftDirection", false);
            backgroundRightToLeftDirection = builder.define("backgroundRightToLeftDirection", false);

            textWidth = builder.defineEnum("textWidth", HUDPosition.EnumWidth.PERCENT_0);
            textHeight = builder.defineEnum("textHeight", HUDPosition.EnumHeight.PERCENT_0);

            backgroundWidth = builder.defineEnum("backgroundWidth", HUDPosition.EnumWidth.PERCENT_0);
            backgroundHeight = builder.defineEnum("backgroundHeight", HUDPosition.EnumHeight.PERCENT_0);

            posX = builder.define("Value of x clock screen in percentage", 0f);
            posY = builder.define("Value of y clock screen in percentage", 0f);
        }
    }
}
