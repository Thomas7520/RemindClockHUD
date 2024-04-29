package com.thomas7520.remindclockhud.events;

import com.thomas7520.remindclockhud.RemindClockHUD;
import com.thomas7520.remindclockhud.object.Chronometer;
import com.thomas7520.remindclockhud.object.Clock;
import com.thomas7520.remindclockhud.object.Remind;
import com.thomas7520.remindclockhud.screens.MenuScreen;
import com.thomas7520.remindclockhud.util.HUDMode;
import com.thomas7520.remindclockhud.util.RemindClockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT, modid = RemindClockHUD.MODID)
public class RemindTimerEvent {



    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.Key event) {
        if(RemindClockUtil.guiBind.isDown()) {
            Minecraft.getInstance().setScreen(new MenuScreen());
            //RemindTimerUtil.getReminds().add(new Remind("Test", 9801, false, false));
        }

        if(RemindClockUtil.addLapsBind.isDown()) {
            for (Remind remind : RemindClockUtil.getReminds()) {
                remind.setPaused(!remind.isPaused());
            }
        }

        if(RemindClockUtil.switchChronometerBind.isDown()) {
            Chronometer chronometer = RemindClockHUD.getChronometer();

            if(!chronometer.isEnable()) return;

            if(!chronometer.isStarted()) {
                chronometer.setStarted(true);
            } else {
                chronometer.setPaused(!chronometer.isPaused());
            }
        }

        if(RemindClockUtil.resetChronometerBind.isDown()) {
            Chronometer chronometer = RemindClockHUD.getChronometer();

            if(!chronometer.isEnable() || !chronometer.isStarted() || !chronometer.isPaused()) return;

            chronometer.reset();
        }
    }

    private static int ticksElasped;

    @SubscribeEvent
    public static void tickEvent(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;

        if(ticksElasped % 20 == 0) {
            for (Remind remind : RemindClockUtil.getReminds()) {
                if (remind.isPaused() || remind.isRinging()) continue;

                if (remind.getTime() <= 0) {
                    remind.setRinging(true);
                    // make ringing, or show message, icon, ...
                    continue;
                }

                remind.setTime(remind.getTime() - 1);
            }
            ticksElasped = 0;
        }

        ticksElasped++;


        if (Minecraft.getInstance().player != null) {

            if(RemindClockHUD.getClock().isEnable()) {
                if (RemindClockHUD.getClock().getRgbModeText() != HUDMode.STATIC) {
                    RemindClockUtil.waveCounterClockText += (RemindClockHUD.getClock().getRgbSpeedText() - 1) / (100 - 1f) * (10 - 1) + 1;
                }

                if (RemindClockHUD.getClock().isDrawBackground() && RemindClockHUD.getClock().getRgbModeBackground() != HUDMode.STATIC) {
                    RemindClockUtil.waveCounterClockBackground += (RemindClockHUD.getClock().getRgbSpeedBackground() - 1) / (100 - 1f) * (20 - 1) + 1;
                }
            }

            if(RemindClockHUD.getChronometer().isEnable()) {

                if (RemindClockHUD.getChronometer().getRgbModeText() != HUDMode.STATIC) {
                    RemindClockUtil.waveCounterChronometerText += (RemindClockHUD.getChronometer().getRgbSpeedText() - 1) / (100 - 1f) * (10 - 1) + 1;
                }

                if (RemindClockHUD.getChronometer().isDrawBackground() && RemindClockHUD.getChronometer().getRgbModeBackground() != HUDMode.STATIC) {
                    RemindClockUtil.waveCounterChronometerBackground += (RemindClockHUD.getChronometer().getRgbSpeedBackground() - 1) / (100 - 1f) * (10 - 1) + 1;
                }
            }
        }
    }

    @SubscribeEvent
    public static void hudEvent(RenderGuiOverlayEvent.Post event) {


        if(event.getOverlay() != VanillaGuiOverlay.AIR_LEVEL.type()) return;

//        for (Remind remind : RemindTimerUtil.getReminds()) {
//            if (remind.isPaused() || remind.isRinging()) continue;
//
//            event.getGuiGraphics().drawString(Minecraft.getInstance().font, remind.getTimeFormatted(), 0, 0, Color.CYAN.getRGB());
//        }
//
//
//        //RemindTimerUtil.circleDoubleProgress(event.getGuievent.getGuiGraphics()().guiWidth() / 2, event.getGuievent.getGuiGraphics()().guiHeight() / 2, 10F, 6F, 180, 180F, 64, new Color(91, 13, 13, 200));


        Clock clock = RemindClockHUD.getClock();

        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();

        Font font = Minecraft.getInstance().font;

        float x = (float) (clock.getPosX() / 100.0 * event.getWindow().getGuiScaledWidth());
        float y = (float) (clock.getPosY() / 100.0 * event.getWindow().getGuiScaledHeight());

        if(clock.isEnable()) {
            RemindClockUtil.renderClock(clock, event.getGuiGraphics(), font, x, y, width, height);
        }

        Chronometer chronometer = RemindClockHUD.getChronometer();

        if(chronometer.isEnable() && (chronometer.isStarted() || chronometer.isIdleRender())) {

            String chronometerFormatted = chronometer.getFormat().formatTime(chronometer.getStartTime());


            if (chronometer.isPaused()) chronometerFormatted = chronometer.getPauseTimeCache();


            if (!chronometer.isStarted() && chronometer.isIdleRender())
                chronometerFormatted = chronometer.getFormat().formatTime(System.currentTimeMillis());

            x = (float) (chronometer.getPosX() / 100.0 * event.getWindow().getGuiScaledWidth());
            y = (float) (chronometer.getPosY() / 100.0 * event.getWindow().getGuiScaledHeight());

            RemindClockUtil.drawChronometer(chronometer, chronometerFormatted, event.getGuiGraphics(), font, x, y, width, height);
        }
    }
}
