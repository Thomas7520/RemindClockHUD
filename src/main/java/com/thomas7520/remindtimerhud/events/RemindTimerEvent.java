package com.thomas7520.remindtimerhud.events;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.thomas7520.remindtimerhud.RemindTimerHUD;
import com.thomas7520.remindtimerhud.object.Chronometer;
import com.thomas7520.remindtimerhud.object.Clock;
import com.thomas7520.remindtimerhud.object.Remind;
import com.thomas7520.remindtimerhud.screens.MenuScreen;
import com.thomas7520.remindtimerhud.util.HUDMode;
import com.thomas7520.remindtimerhud.util.RemindTimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT, modid = RemindTimerHUD.MODID)
public class RemindTimerEvent {



    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.Key event) {
        if(RemindTimerUtil.guiBind.isDown()) {
            Minecraft.getInstance().setScreen(new MenuScreen());
            //RemindTimerUtil.getReminds().add(new Remind("Test", 9801, false, false));
        }

        if(RemindTimerUtil.addLapsBind.isDown()) {
            for (Remind remind : RemindTimerUtil.getReminds()) {
                remind.setPaused(!remind.isPaused());
            }
        }

        if(RemindTimerUtil.switchChronometerBind.isDown()) {
            Chronometer chronometer = RemindTimerHUD.getChronometer();

            if(!chronometer.isEnable()) return;

            if(!chronometer.isStarted()) {
                chronometer.setStarted(true);
            } else {
                chronometer.setPaused(!chronometer.isPaused());
            }
        }

        if(RemindTimerUtil.resetChronometerBind.isDown()) {
            Chronometer chronometer = RemindTimerHUD.getChronometer();

            if(!chronometer.isEnable() || !chronometer.isStarted() || !chronometer.isPaused()) return;

            chronometer.reset();
        }
    }

    private static int ticksElasped;

    @SubscribeEvent
    public static void tickEvent(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;

        if(ticksElasped % 20 == 0) {
            for (Remind remind : RemindTimerUtil.getReminds()) {
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

            if(RemindTimerHUD.getClock().isEnable()) {
                if (RemindTimerHUD.getClock().getRgbModeText() != HUDMode.STATIC) {
                    RemindTimerUtil.waveCounterClockText += (RemindTimerHUD.getClock().getRgbSpeedText() - 1) / (100 - 1f) * (10 - 1) + 1;
                }

                if (RemindTimerHUD.getClock().isDrawBackground() && RemindTimerHUD.getClock().getRgbModeBackground() != HUDMode.STATIC) {
                    RemindTimerUtil.waveCounterClockBackground += (RemindTimerHUD.getClock().getRgbSpeedBackground() - 1) / (100 - 1f) * (20 - 1) + 1;
                }
            }

            if(RemindTimerHUD.getChronometer().isEnable()) {

                if (RemindTimerHUD.getChronometer().getRgbModeText() != HUDMode.STATIC) {
                    RemindTimerUtil.waveCounterChronometerText += (RemindTimerHUD.getChronometer().getRgbSpeedText() - 1) / (100 - 1f) * (10 - 1) + 1;
                }

                if (RemindTimerHUD.getChronometer().isDrawBackground() && RemindTimerHUD.getChronometer().getRgbModeBackground() != HUDMode.STATIC) {
                    RemindTimerUtil.waveCounterChronometerBackground += (RemindTimerHUD.getChronometer().getRgbSpeedBackground() - 1) / (100 - 1f) * (10 - 1) + 1;
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


        Clock clock = RemindTimerHUD.getClock();

        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();

        Font font = Minecraft.getInstance().font;

        float x = (float) (clock.getPosX() / 100.0 * event.getWindow().getGuiScaledWidth());
        float y = (float) (clock.getPosY() / 100.0 * event.getWindow().getGuiScaledHeight());

        if(clock.isEnable()) {
            RemindTimerUtil.renderClock(clock, event.getGuiGraphics(), font, x, y, width, height);
        }

        Chronometer chronometer = RemindTimerHUD.getChronometer();

        if(chronometer.isEnable() && (chronometer.isStarted() || chronometer.isIdleRender())) {

            String chronometerFormatted = chronometer.getFormat().formatTime(chronometer.getStartTime());


            if (chronometer.isPaused()) chronometerFormatted = chronometer.getPauseTimeCache();


            if (!chronometer.isStarted() && chronometer.isIdleRender())
                chronometerFormatted = chronometer.getFormat().formatTime(System.currentTimeMillis());

            x = (float) (chronometer.getPosX() / 100.0 * event.getWindow().getGuiScaledWidth());
            y = (float) (chronometer.getPosY() / 100.0 * event.getWindow().getGuiScaledHeight());

            RemindTimerUtil.drawChronometer(chronometer, chronometerFormatted, event.getGuiGraphics(), font, x, y, width, height);
        }
    }
}
