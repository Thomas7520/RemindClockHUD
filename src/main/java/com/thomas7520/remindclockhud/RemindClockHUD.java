package com.thomas7520.remindclockhud;


import com.thomas7520.remindclockhud.object.Chronometer;
import com.thomas7520.remindclockhud.object.Clock;
import com.thomas7520.remindclockhud.util.RemindClockConfig;
import com.thomas7520.remindclockhud.util.RemindClockUtil;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import static com.thomas7520.remindclockhud.RemindClockHUD.MODID;

@Mod(MODID)
public class RemindClockHUD {

    public static final String MODID = "remindclockhud";

    public static final Logger LOGGER = LogManager.getLogger();

    private static Clock clock;
    private static Chronometer chronometer;

    public RemindClockHUD() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeybindingEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RemindClockConfig.CLIENT_SPEC);

    }

    private void setup(final FMLClientSetupEvent event) {
//        File directory = new File(FMLPaths.CONFIGDIR.get().resolve(MODID) + "/global");
//
//        if (directory.mkdirs() || directory.listFiles() == null) return;
//
//        File directoryAlarms = new File(directory.getAbsolutePath() + "/alarms");
//        File directoryReminds = new File(directory.getAbsolutePath() + "/reminds");
//
//
//        if(!directoryAlarms.mkdirs()) {
//            for (File alarmFile : Objects.requireNonNull(directoryAlarms.listFiles())) {
//
//            }
//        }
//
//        if(!directoryReminds.mkdirs()) {
//            for (File alarmFile : Objects.requireNonNull(directoryAlarms.listFiles())) {
//
//            }
//        }

        RemindClockConfig.Client.Clock clock = RemindClockConfig.CLIENT.clock;

        RemindClockHUD.clock = new Clock(clock.enable.get(), clock.formatText.get(), clock.drawBackground.get(), clock.use12HourFormat.get(), clock.rgbModeText.get(), clock.rgbModeBackground.get(),
                clock.redText.get(), clock.greenText.get(), clock.blueText.get(), clock.alphaText.get(), clock.rgbSpeedText.get(),
                clock.redBackground.get(), clock.greenBackground.get(), clock.blueBackground.get(), clock.alphaBackground.get(),
                clock.rgbSpeedBackground.get(), clock.textRightToLeftDirection.get(), clock.backgroundRightToLeftDirection.get(),
                clock.posX.get(), clock.posY.get());

        RemindClockConfig.Client.Chronometer chronometer = RemindClockConfig.CLIENT.chronometer;

        RemindClockHUD.chronometer = new Chronometer(chronometer.enable.get(), chronometer.format.get(), chronometer.drawBackground.get(), chronometer.rgbModeText.get(), chronometer.rgbModeBackground.get(),
                chronometer.redText.get(), chronometer.greenText.get(), chronometer.blueText.get(), chronometer.alphaText.get(), chronometer.rgbSpeedText.get(),
                chronometer.redBackground.get(), chronometer.greenBackground.get(), chronometer.blueBackground.get(), chronometer.alphaBackground.get(),
                chronometer.rgbSpeedBackground.get(), chronometer.textRightToLeftDirection.get(), chronometer.backgroundRightToLeftDirection.get(),
                chronometer.idleRender.get(), chronometer.posX.get(), chronometer.posY.get());

        //LOGGER.info(RemindTimerUtil.getGlobalAlarmsMap().size() + " alarms loaded");
        //LOGGER.info(RemindTimerUtil.getGlobalRemindsMap().size() + " reminds loaded");
    }

    private void registerKeybindingEvent(RegisterKeyMappingsEvent event) {
        RemindClockUtil.guiBind = new KeyMapping("key.remindclockhud.opengui" , GLFW.GLFW_KEY_H, "key.categories.remindclockhud");
        RemindClockUtil.switchChronometerBind = new KeyMapping("key.remindclockhud.switchchronometer" , GLFW.GLFW_KEY_J, "key.categories.remindclockhud");
        //RemindTimerUtil.addLapsBind = new KeyMapping("key.remindclockhud.openoptions.addlaps" , GLFW.GLFW_KEY_K, "key.categories.remindclockhud");
        RemindClockUtil.resetChronometerBind = new KeyMapping("key.remindclockhud.openoptions.resetchronometer" , GLFW.GLFW_KEY_M, "key.categories.remindclockhud");
        //RemindTimerUtil.stopAlarm = new KeyMapping("key.remindclockhud.openoptions.stopalarm" , GLFW.GLFW_KEY_N, "key.categories.remindclockhud");

        event.register(RemindClockUtil.guiBind);
        event.register(RemindClockUtil.switchChronometerBind);
        //event.register(RemindTimerUtil.addLapsBind);
        event.register(RemindClockUtil.resetChronometerBind);
        //event.register(RemindTimerUtil.stopAlarm);

    }


    public static Clock getClock() {
        return clock;
    }
    
    public static Chronometer getChronometer() {
        return chronometer;
    }
}
