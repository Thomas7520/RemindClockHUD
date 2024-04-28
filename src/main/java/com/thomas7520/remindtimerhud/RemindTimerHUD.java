package com.thomas7520.remindtimerhud;


import com.thomas7520.remindtimerhud.object.Chronometer;
import com.thomas7520.remindtimerhud.object.Clock;
import com.thomas7520.remindtimerhud.util.RemindTimerConfig;
import com.thomas7520.remindtimerhud.util.RemindTimerUtil;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.Objects;

import static com.thomas7520.remindtimerhud.RemindTimerHUD.MODID;

@Mod(MODID)
public class RemindTimerHUD {

    public static final String MODID = "remindtimerhud";

    public static final Logger LOGGER = LogManager.getLogger();

    private static Clock clock;
    private static Chronometer chronometer;

    public RemindTimerHUD() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeybindingEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RemindTimerConfig.CLIENT_SPEC);

    }

    private void setup(final FMLClientSetupEvent event) {
        File directory = new File(FMLPaths.CONFIGDIR.get().resolve(MODID) + "/global");

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

        RemindTimerConfig.Client.Clock clock = RemindTimerConfig.CLIENT.clock;

        RemindTimerHUD.clock = new Clock(clock.formatText.get(), clock.drawBackground.get(), clock.use12HourFormat.get(), clock.rgbModeText.get(), clock.rgbModeBackground.get(),
                clock.redText.get(), clock.greenText.get(), clock.blueText.get(), clock.alphaText.get(), clock.rgbSpeedText.get(),
                clock.redBackground.get(), clock.greenBackground.get(), clock.blueBackground.get(), clock.alphaBackground.get(),
                clock.rgbSpeedBackground.get(), clock.textRightToLeftDirection.get(), clock.backgroundRightToLeftDirection.get(),
                clock.posX.get(), clock.posY.get());

        RemindTimerConfig.Client.Chronometer chronometer = RemindTimerConfig.CLIENT.chronometer;

        RemindTimerHUD.chronometer = new Chronometer(chronometer.format.get(), chronometer.drawBackground.get(), chronometer.rgbModeText.get(), chronometer.rgbModeBackground.get(),
                chronometer.redText.get(), chronometer.greenText.get(), chronometer.blueText.get(), chronometer.alphaText.get(), chronometer.rgbSpeedText.get(),
                chronometer.redBackground.get(), chronometer.greenBackground.get(), chronometer.blueBackground.get(), chronometer.alphaBackground.get(),
                chronometer.rgbSpeedBackground.get(), chronometer.textRightToLeftDirection.get(), chronometer.backgroundRightToLeftDirection.get(),
                chronometer.posX.get(), chronometer.posY.get(), 0, false);

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
    
    public static Chronometer getChronometer() {
        return chronometer;
    }
}
