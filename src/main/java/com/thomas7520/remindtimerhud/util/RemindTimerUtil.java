package com.thomas7520.remindtimerhud.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.thomas7520.remindtimerhud.RemindTimerHUD;
import com.thomas7520.remindtimerhud.object.Alarm;
import com.thomas7520.remindtimerhud.object.Chronometer;
import com.thomas7520.remindtimerhud.object.Clock;
import com.thomas7520.remindtimerhud.object.Remind;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RemindTimerUtil {

    public final static int RECT_HEIGHT = 12;
    public final static int TEXT_X = 2;
    public final static int TEXT_Y = 2;
    public static float waveCounterClockBackground;
    public static float waveCounterClockText;
    public static float waveCounterChronometerBackground;
    public static float waveCounterChronometerText;
    public static KeyMapping guiBind;
    public static KeyMapping switchChronometerBind;
    public static KeyMapping addLapsBind;
    public static KeyMapping resetChronometerBind;
    public static KeyMapping stopAlarm;

    private static final List<Remind> reminds = new ArrayList<>();
    private static final HashMap<UUID, Alarm> alarms = new HashMap<>();
    private static final HashMap<UUID, Alarm> serverAlarms = new HashMap<>();


    public static List<Remind> getReminds() {
        return reminds;
    }

    public static HashMap<UUID, Alarm> getAlarms() {
        return alarms;
    }

    public static HashMap<UUID, Alarm> getServerAlarms() {
        return serverAlarms;
    }


    public static void circleDoubleProgress(
            int x, int y,
            float radius, float radiusDouble,
            float progress, float initialDegree, int segment, Color color) {

        float a = 360F / segment;
        float b;

        double degree, sin, cos;
        double osin = 0;
        double ocos = 0;

        Tesselator tessellator = Tesselator.getInstance();
        tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        for (int i = 0; i <= segment; ++i) {

            b = a * i;
            if (b < progress)
                degree = (initialDegree + b) * Math.PI / 180D;
            else
                degree = (initialDegree + progress) * Math.PI / 180D;

            sin = Math.sin(-degree);
            cos = Math.cos(-degree);


            tessellator.getBuilder()
                    .vertex((float) (x + (osin * radius)), (float) (y + (ocos * radius)), 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();

            tessellator.getBuilder()
                    .vertex((float) ((float)x + (osin * radiusDouble)), (float) ((float)y + (ocos * radiusDouble)), 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();

            tessellator.getBuilder()
                    .vertex((float) ((float)x + (sin * radiusDouble)), (float) ((float)y + (cos * radiusDouble)), 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();

            tessellator.getBuilder()
                    .vertex((float) ((float)x + (sin * radius)), (float) ((float)y + (cos * radius)), 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();


            osin = sin;
            ocos = cos;

            if (b > progress) break;
        }
        tessellator.end();
        RenderSystem.disableBlend();
    }

    public static void renderClock(Clock clock, GuiGraphics guiGraphics, Font font, double x, double y, int width, int height) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        String dateFormatted = clock.getDateFormatted();

        int rectWidth = font.width(dateFormatted) + 3;

        x = Math.max(0, x);
        x = Math.min(width - rectWidth, x);

        y = Math.max(y, 0);
        y = Math.min(y, height - RECT_HEIGHT);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);

        if (clock.isDrawBackground()) {
            if (clock.getRgbModeBackground() == HUDMode.WAVE) {

                for (int i = 0; i < rectWidth; i++) {
                    float hueStart = 1.0F - ((i - waveCounterClockBackground) / 360f);

                    float hueEnd = 1.0F - ((i + 1 - waveCounterClockBackground) / 360f);

                    if (clock.isBackgroundRightToLeftDirection()) {
                        hueStart = (i + waveCounterClockBackground) / 360f;
                        hueEnd = (i + 4 + waveCounterClockBackground) / 360f;
                    }

                    int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                    int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);

                    colorStart = (colorStart & 0x00FFFFFF) | (clock.getAlphaBackground() << 24);
                    colorEnd = (colorEnd & 0x00FFFFFF) | (clock.getAlphaBackground() << 24);

                    drawGradientRect(x + i, y, x + i + 1, y + RECT_HEIGHT, 0, colorStart, colorEnd, colorStart, colorEnd);
                }
            } else if (clock.getRgbModeBackground() == HUDMode.CYCLE) {

                float hueStart = 1.0F - (waveCounterClockBackground / 360f); // Inversion de la couleur

                if (clock.isBackgroundRightToLeftDirection()) {
                    hueStart = waveCounterClockBackground / 360f; // Inversion de la couleur
                }

                float hueEnd = hueStart;

                int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);


                colorStart = (colorStart & 0x00FFFFFF) | (clock.getAlphaBackground() << 24);
                colorEnd = (colorEnd & 0x00FFFFFF) | (clock.getAlphaBackground() << 24);

                drawGradientRect(x, y, x + rectWidth, y + RECT_HEIGHT, 0, colorStart, colorStart, colorEnd, colorEnd);
            } else {
                int colorBackground = (clock.getAlphaBackground() << 24 | clock.getRedBackground() << 16 | clock.getGreenBackground() << 8 | clock.getBlueBackground());

                guiGraphics.fill(0, -2, rectWidth, 8 + 4, colorBackground);
            }
        }


        if (clock.getRgbModeText() == HUDMode.WAVE) {
            int textCharX = TEXT_X;

            for (int i = 0; i < dateFormatted.length(); i++) {
                char c = dateFormatted.charAt(i);

                int color = getFadeClockColor(dateFormatted, i);

                guiGraphics.drawString(font, String.valueOf(c), textCharX, TEXT_Y, color, false);
                textCharX += font.width(String.valueOf(c));
            }
        } else if (clock.getRgbModeText() == HUDMode.CYCLE) {

            float hueStart = 1.0F - (waveCounterClockText / 255);

            if (clock.isTextRightToLeftDirection()) {
                hueStart = waveCounterClockText / 255;
            }

            int color = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);

            color = (color & 0x00FFFFFF) | (clock.getAlphaText() << 24);

            guiGraphics.drawString(font, dateFormatted, TEXT_X, TEXT_Y, color, false);

        } else {
            int colorText = (clock.getAlphaText() << 24 | clock.getRedText() << 16 | clock.getGreenText() << 8 | clock.getBlueText());

            guiGraphics.drawString(font, dateFormatted, TEXT_X, TEXT_Y, colorText, false);
        }

        guiGraphics.setColor(1,1,1,1);
        guiGraphics.pose().popPose();
        RenderSystem.disableBlend();

    }

    // TODO When chronometer increase size, it breaks its original position. But if it's in a corner, it's good. So ?
    public static void drawChronometer(Chronometer chronometer, String chronometerFormatted, GuiGraphics guiGraphics, Font font, double x, double y, int width, int height) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        int rectWidth = font.width(chronometerFormatted) + 3;

        x = Math.max(0, x);
        x = Math.min(width - rectWidth, x);

        y = Math.max(y, 0);
        y = Math.min(y, height - RECT_HEIGHT);


        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x,y, 0);


        if(chronometer.isDrawBackground()) {
            if (chronometer.getRgbModeBackground() == HUDMode.WAVE) {
                for (int i = 0; i < rectWidth; i++) {
                    float hueStart = 1.0F - ((i - waveCounterChronometerBackground) / 360f);

                    float hueEnd = 1.0F - ((i + 1 - waveCounterChronometerBackground) / 360f);

                    if (chronometer.isBackgroundRightToLeftDirection()) {
                        hueStart = (i + waveCounterChronometerBackground) / 360f;
                        hueEnd = (i + 4 + waveCounterChronometerBackground) / 360f;

                    }

                    int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                    int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);

                    colorStart = (colorStart & 0x00FFFFFF) | (chronometer.getAlphaBackground() << 24);

                    colorEnd = (colorEnd & 0x00FFFFFF) | (chronometer.getAlphaBackground() << 24);

                    drawGradientRect(x + i, y, x + i + 1, y + RECT_HEIGHT, 0, colorStart, colorEnd, colorStart, colorEnd);
                }
            } else if (chronometer.getRgbModeBackground() == HUDMode.CYCLE) {

                float hueStart = 1.0F - (waveCounterChronometerBackground / 360f); // Inversion de la couleur

                if (chronometer.isBackgroundRightToLeftDirection()) {
                    hueStart = waveCounterChronometerBackground / 360f; // Inversion de la couleur
                }

                float hueEnd = hueStart; // Utilisez la même couleur pour le coin opposé

                int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);


                colorStart = (colorStart & 0x00FFFFFF) | (chronometer.getAlphaBackground() << 24);

                colorEnd = (colorEnd & 0x00FFFFFF) | (chronometer.getAlphaBackground() << 24);

                drawGradientRect(x, y, x+rectWidth, y+ RECT_HEIGHT, 0, colorStart, colorStart, colorEnd, colorEnd);
            } else {
                int colorBackground = (chronometer.getAlphaBackground() << 24 | chronometer.getRedBackground() << 16 | chronometer.getGreenBackground() << 8 | chronometer.getBlueBackground());

                guiGraphics.fill(0, -2, rectWidth, 8 + 4, colorBackground);
            }
        }


        if(chronometer.getRgbModeText() == HUDMode.WAVE) {
            int textCharX = TEXT_X;

            for (int i = 0; i < chronometerFormatted.length(); i++) {
                char c = chronometerFormatted.charAt(i);

                int color = getFadeChronometerColor(chronometerFormatted, i);

                guiGraphics.drawString(font, String.valueOf(c), textCharX, TEXT_Y, color, false);
                textCharX += font.width(String.valueOf(c));
            }
        } else if (chronometer.getRgbModeText() == HUDMode.CYCLE) {

            float hueStart = 1.0F - (waveCounterChronometerText / 255); // Inversion de la couleur

            if (chronometer.isTextRightToLeftDirection()) {
                hueStart = waveCounterChronometerText / 255; // Inversion de la couleur
            }


            int color = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);

            color = (color & 0x00FFFFFF) | (chronometer.getAlphaText() << 24);

            guiGraphics.drawString(font, chronometerFormatted, TEXT_X, TEXT_Y, color, false);

        } else {
            int colorText = (chronometer.getAlphaText() << 24 | chronometer.getRedText() << 16 | chronometer.getGreenText() << 8 | chronometer.getBlueText());

            guiGraphics.drawString(font, chronometerFormatted, TEXT_X, TEXT_Y, colorText, false);
        }

        guiGraphics.setColor(1,1,1,1);
        guiGraphics.pose().popPose();
        RenderSystem.disableBlend();
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int z, int coltl, int coltr, int colbl,
                                        int colbr) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();

        Tesselator tesselator = Tesselator.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);


        buffer.vertex(right, top, z).color((coltr & 0x00ff0000) >> 16, (coltr & 0x0000ff00) >> 8,
                (coltr & 0x000000ff), (coltr & 0xff000000) >>> 24).endVertex();
        buffer.vertex(left, top, z).color((coltl & 0x00ff0000) >> 16, (coltl & 0x0000ff00) >> 8, (coltl & 0x000000ff),
                (coltl & 0xff000000) >>> 24).endVertex();
        buffer.vertex(left, bottom, z).color((colbl & 0x00ff0000) >> 16, (colbl & 0x0000ff00) >> 8,
                (colbl & 0x000000ff), (colbl & 0xff000000) >>> 24).endVertex();
        buffer.vertex(right, bottom, z).color((colbr & 0x00ff0000) >> 16, (colbr & 0x0000ff00) >> 8,
                (colbr & 0x000000ff), (colbr & 0xff000000) >>> 24).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();
    }

    private static int getFadeClockColor(String dateFormatted, int i) {
        float hue = 1.0F - ((dateFormatted.length() - i + waveCounterClockText) * 2 / 360f);

        if (RemindTimerHUD.getClock().isTextRightToLeftDirection())
            hue = (dateFormatted.length() + i + waveCounterClockText) * 2 / 360f;

        float saturation = 1.0F;
        float brightness = 1.0F;

        int color = Color.HSBtoRGB(hue, saturation, brightness);

        color = (color & 0x00FFFFFF) | (RemindTimerHUD.getClock().getAlphaText() << 24);
        return color;
    }

    private static int getFadeChronometerColor(String chornometerFormatted, int i) {
        float hue = 1.0F - ((chornometerFormatted.length() - i + waveCounterChronometerText) * 2 / 360f);

        if (RemindTimerHUD.getClock().isTextRightToLeftDirection())
            hue = (chornometerFormatted.length() + i + waveCounterChronometerText) * 2 / 360f;

        float saturation = 1.0F;
        float brightness = 1.0F;

        int color = Color.HSBtoRGB(hue, saturation, brightness);

        color = (color & 0x00FFFFFF) | (RemindTimerHUD.getChronometer().getAlphaText() << 24);
        return color;
    }
}
