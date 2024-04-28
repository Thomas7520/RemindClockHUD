package com.thomas7520.remindtimerhud.screens.chronometer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.thomas7520.remindtimerhud.RemindTimerHUD;
import com.thomas7520.remindtimerhud.object.Chronometer;
import com.thomas7520.remindtimerhud.util.HUDMode;
import com.thomas7520.remindtimerhud.util.RemindTimerConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ChronometerPositionScreen extends Screen {

    public Chronometer chronometer;
    private int waveCounterBackground;
    private int waveCounterText;

    private boolean clicked;
    private double percentageX;
    private double percentageY;
    private double x;
    private double y;

    public ChronometerPositionScreen() {
        super(Component.empty());
        chronometer = RemindTimerHUD.getChronometer();
    }


    @Override
    public void tick() {
        waveCounterText += (chronometer.getRgbSpeedText() - 1) / (100 - 1) * (10 - 1) + 1;
        waveCounterBackground += (chronometer.getRgbSpeedBackground() - 1) / (100 - 1) * (20 - 1) + 1;
        super.tick();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pMouseX >= x && pMouseX < x + font.width(chronometer.getFormat().formatTime(0)) + 3
                && pMouseY >= y && pMouseY < y + 12) {
            if (!clicked) {
                clicked = true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (clicked) {
            clicked = false;
            RemindTimerConfig.Client.Chronometer configChronometer = RemindTimerConfig.CLIENT.chronometer;
            configChronometer.posX.set(chronometer.getPosX());
            configChronometer.posY.set(chronometer.getPosY());
        }

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float p_282465_) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, p_282465_);

        if (clicked) {
            percentageX = (double) mouseX / minecraft.getWindow().getGuiScaledWidth() * 100;
            percentageY = (double) mouseY / minecraft.getWindow().getGuiScaledHeight() * 100;

            percentageX = Math.min(100, Math.max(0, percentageX));
            percentageY = Math.min(100, Math.max(0, percentageY));

            chronometer.setPosX(percentageX);
            chronometer.setPosY(percentageY);
        }

        for (int i = 1; i < 4; i++) {
            graphics.fill((width / 4) * (i), 0, (width / 4) * (i) + 1, height, Color.RED.getRGB());

            graphics.fill(0, (height / 4) * (i), width, (height / 4) * (i) + 1, Color.RED.getRGB());
        }

        String dateFormatted = chronometer.getFormat().formatTime(System.currentTimeMillis());

        graphics.drawCenteredString(this.font, this.title, 2, 20, 16777215);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        x = (float) (chronometer.getPosX() / 100.0 * minecraft.getWindow().getGuiScaledWidth());
        y = (float) (chronometer.getPosY() / 100.0 * minecraft.getWindow().getGuiScaledHeight());

        int rectWidth = font.width(dateFormatted) + 3;
        int rectHeight = 12;

        x = Math.max(0, x);
        x = Math.min(width - rectWidth, x);

        y = Math.max(y, 2);
        y = Math.min(y, height - rectHeight);

        int textX = 2;
        int textY = 2;

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);


        if (chronometer.isDrawBackground()) {
            if (chronometer.getRgbModeBackground() == HUDMode.WAVE) {
                for (int i = 0; i < rectWidth; i++) {
                    float hueStart = 1.0F - ((i - waveCounterBackground) / 360f); // Inversion de la couleur

                    float hueEnd = 1.0F - ((i + 1 - waveCounterBackground) / 360f); // Inversion de la couleur

                    if (chronometer.isBackgroundRightToLeftDirection()) {
                        hueStart = (i + waveCounterBackground) / 360f; // Inversion de la couleur
                        hueEnd = (i + 4 + waveCounterBackground) / 360f; // Inversion de la couleur
                    }

                    int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                    int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);

                    colorStart = (colorStart & 0x00FFFFFF) | (chronometer.getAlphaBackground() << 24);

                    colorEnd = (colorEnd & 0x00FFFFFF) | (chronometer.getAlphaBackground() << 24);

                    // Dessiner une colonne du rectangle avec le dégradé de couleur
                    drawGradientRect(x + i, y, i + 1, y + rectHeight, 0, colorStart, colorEnd, colorStart, colorEnd);
                }
            } else if (chronometer.getRgbModeBackground() == HUDMode.CYCLE) {

                float hueStart = 1.0F - ((float) (waveCounterBackground) / 360f); // Inversion de la couleur

                if (chronometer.isBackgroundRightToLeftDirection()) {
                    hueStart = (float) (waveCounterBackground) / 360f; // Inversion de la couleur
                }

                float hueEnd = hueStart; // Utilisez la même couleur pour le coin opposé

                int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);


                colorStart = (colorStart & 0x00FFFFFF) | (chronometer.getAlphaBackground() << 24);

                colorEnd = (colorEnd & 0x00FFFFFF) | (chronometer.getAlphaBackground() << 24);
                // Dessiner une colonne du rectangle avec le dégradé de couleur
                drawGradientRect(x, y, x + rectWidth, y + rectHeight, 0, colorStart, colorStart, colorEnd, colorEnd);
            } else {
                int colorBackground = (chronometer.getAlphaBackground() << 24 | chronometer.getRedBackground() << 16 | chronometer.getGreenBackground() << 8 | chronometer.getBlueBackground());

                graphics.fill(0, -2, rectWidth, 8 + 4, colorBackground);
            }

        }


        if (chronometer.getRgbModeText() == HUDMode.WAVE) {
            int textCharX = textX;

            for (int i = 0; i < dateFormatted.length(); i++) {
                char c = dateFormatted.charAt(i);

                int color = getColor(dateFormatted, i);

                graphics.drawString(font, String.valueOf(c), textCharX, textY, color, false);
                textCharX += minecraft.font.width(String.valueOf(c));
            }
        } else if (chronometer.getRgbModeText() == HUDMode.CYCLE) {

            float hueStart = 1.0F - ((float) (waveCounterText) / 255); // Inversion de la couleur

            if (chronometer.isTextRightToLeftDirection()) {
                hueStart = (float) (waveCounterText) / 255; // Inversion de la couleur
            }


            int color = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);

            color = (color & 0x00FFFFFF) | (chronometer.getAlphaText() << 24);

            graphics.drawString(font, dateFormatted, textX, textY, color, false);

        } else {
            int colorText = (chronometer.getAlphaText() << 24 | chronometer.getRedText() << 16 | chronometer.getGreenText() << 8 | chronometer.getBlueText());

            graphics.drawString(font, dateFormatted, textX, textY, colorText, false);
        }

        graphics.pose().popPose();

    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {

        float xOffset = 0;
        float yOffset = 0;


        switch (pKeyCode) {
            case GLFW.GLFW_KEY_LEFT -> xOffset -= 0.5f;
            case GLFW.GLFW_KEY_RIGHT -> xOffset += 0.5f;
            case GLFW.GLFW_KEY_UP -> yOffset -= 0.5f;
            case GLFW.GLFW_KEY_DOWN -> yOffset += 0.5f;
        }

        x = (float) ((chronometer.getPosX() + xOffset) / 100.0 * minecraft.getWindow().getGuiScaledWidth());
        y = (float) ((chronometer.getPosY() + yOffset) / 100.0 * minecraft.getWindow().getGuiScaledHeight());

        int rectWidth = font.width(chronometer.getFormat().formatTime(0)) + 3;
        int rectHeight = 12;

        x = Math.max(0, x);
        x = Math.min(width - rectWidth, x);

        y = Math.max(y, 2);
        y = Math.min(y, height - rectHeight);

        percentageX = x / minecraft.getWindow().getGuiScaledWidth() * 100;
        percentageY = y / minecraft.getWindow().getGuiScaledHeight() * 100;

        percentageX = Math.min(100, Math.max(0, percentageX));
        percentageY = Math.min(100, Math.max(0, percentageY));

        chronometer.setPosX(percentageX);
        chronometer.setPosY(percentageY);

        RemindTimerConfig.Client.Chronometer configChronometer = RemindTimerConfig.CLIENT.chronometer;
        configChronometer.posX.set(chronometer.getPosX());
        configChronometer.posY.set(chronometer.getPosY());

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    private int getColor(String dateFormatted, int i) {
        float hue = 1.0F - ((float) (dateFormatted.length() - i + waveCounterText) * 2 / 360f); // Inversion de la couleur

        if (chronometer.isTextRightToLeftDirection())
            hue = (float) (dateFormatted.length() + i + waveCounterText) * 2 / 360f; // Inversion de la couleur

        float saturation = 1.0F;
        float brightness = 1.0F;

        int color = Color.HSBtoRGB(hue, saturation, brightness);

        color = (color & 0x00FFFFFF) | (chronometer.getAlphaText() << 24);
        return color;
    }

    private void drawGradientRect(double left, double top, double right, double bottom, int z, int coltl, int coltr, int colbl,
                                  int colbr) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

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
}