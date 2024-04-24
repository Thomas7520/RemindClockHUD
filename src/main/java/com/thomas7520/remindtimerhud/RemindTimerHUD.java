package com.thomas7520.remindtimerhud;


import com.thomas7520.remindtimerhud.object.Clock;
import com.thomas7520.remindtimerhud.util.HUDMode;
import com.thomas7520.remindtimerhud.util.HUDPosition;
import com.thomas7520.remindtimerhud.util.RemindTimerConfig;
import com.thomas7520.remindtimerhud.util.RemindTimerUtil;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.thomas7520.remindtimerhud.RemindTimerHUD.MODID;

@Mod(MODID)
public class RemindTimerHUD {

    public static final String MODID = "remindtimerhud";

    public static final Logger LOGGER = LogManager.getLogger();

    private static Clock clock;

    public RemindTimerHUD() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeybindingEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RemindTimerConfig.CLIENT_SPEC);

    }

    private void setup(final FMLClientSetupEvent event) {
            File directory = new File(FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()) + "/global");

            if (directory.mkdirs() || directory.listFiles() == null) return;

            File directoryAlarms = new File(directory.getAbsolutePath() + "/alarms");
            File directoryReminds = new File(directory.getAbsolutePath() + "/reminds");


            if(!directoryAlarms.mkdirs()) {
                for (File alarmFile : Objects.requireNonNull(directoryAlarms.listFiles())) {

                }
            }

            if(!directoryReminds.mkdirs()) {
                for (File alarmFile : Objects.requireNonNull(directoryAlarms.listFiles())) {

                }
            }

        System.out.println(RemindTimerConfig.CLIENT.alphaBackground.get());

        RemindTimerConfig.Client config = RemindTimerConfig.CLIENT;

        RemindTimerHUD.clock = new Clock(config.formatText.get()
                , config.drawText.get(), config.drawBackground.get(), config.use12HourFormat.get(), config.rgbModeText.get(), config.rgbModeBackground.get(),
                config.redText.get(), config.greenText.get(), config.blueText.get(), config.alphaText.get(), config.rgbSpeedText.get(),
                config.redBackground.get(), config.greenBackground.get(), config.blueBackground.get(), config.alphaBackground.get(),
                config.rgbSpeedBackground.get(), config.textRightToLeftDirection.get(), config.backgroundRightToLeftDirection.get(),
                config.posX.get(), config.posY.get());

        //LOGGER.info(RemindTimerUtil.getGlobalAlarmsMap().size() + " alarms loaded");
        //LOGGER.info(RemindTimerUtil.getGlobalRemindsMap().size() + " reminds loaded");
    }

    private void registerKeybindingEvent(RegisterKeyMappingsEvent event) {
        RemindTimerUtil.guiBind = new KeyMapping("key.remindtimerhud.opengui" , GLFW.GLFW_KEY_H, "key.categories.remindtimerhud");
        RemindTimerUtil.switchChronometerBind = new KeyMapping("key.remindtimerhud.switchchronometer" , GLFW.GLFW_KEY_J, "key.categories.remindtimerhud");
        RemindTimerUtil.addLapsBind = new KeyMapping("key.remindtimerhud.openoptions.addlaps" , GLFW.GLFW_KEY_K, "key.categories.remindtimerhud");
        RemindTimerUtil.resetChronometerBind = new KeyMapping("key.remindtimerhud.openoptions.resetchronometer" , GLFW.GLFW_KEY_M, "key.categories.remindtimerhud");
        RemindTimerUtil.stopAlarm = new KeyMapping("key.remindtimerhud.openoptions.stopalarm" , GLFW.GLFW_KEY_N, "key.categories.remindtimerhud");

        event.register(RemindTimerUtil.guiBind);
        event.register(RemindTimerUtil.switchChronometerBind);
        event.register(RemindTimerUtil.addLapsBind);
        event.register(RemindTimerUtil.resetChronometerBind);
        event.register(RemindTimerUtil.stopAlarm);

    }


    public static Clock getClock() {
        return clock;
    }
}
