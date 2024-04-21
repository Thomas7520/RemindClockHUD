package com.thomas7520.remindtimerhud.screens.clock;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.thomas7520.remindtimerhud.RemindTimerHUD;
import com.thomas7520.remindtimerhud.object.Clock;
import com.thomas7520.remindtimerhud.util.HUDMode;
import com.thomas7520.remindtimerhud.util.RemindTimerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class PositionScreen extends Screen {

    public Clock clock;
    private int waveCounterBackground;
    private int waveCounterText;

    private boolean clicked;
    private int percentageX;
    private int percentageY;
    private int x;
    private int y;

    public PositionScreen(Component pTitle) {
        super(pTitle);

        clock = RemindTimerHUD.getClock();

    }


    @Override
    public void tick() {
        waveCounterText+=(clock.getRgbSpeedText() - 1) / (100 - 1) * (10 - 1) + 1;
        waveCounterBackground+=(clock.getRgbSpeedBackground() - 1) / (100 - 1) * (20 - 1) + 1;
        super.tick();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(pMouseX >= x && pMouseX < x + font.width(clock.getFormatText()) + 3
                && pMouseY >= y && pMouseY < y + 12) {
            if (!clicked) {
                clicked = true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if(clicked) {
            clicked = false;
            RemindTimerConfig.Client config = RemindTimerConfig.CLIENT;
            config.posX.set(clock.getPosX());
            config.posY.set(clock.getPosY());
        }

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float p_282465_) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, p_282465_);

        if(clicked) {
            percentageX = (int) ((float) mouseX / minecraft.getWindow().getGuiScaledWidth() * 100);
            percentageY = (int) ((float) mouseY / minecraft.getWindow().getGuiScaledHeight() * 100);

            clock.setPosX(percentageX);
            clock.setPosY(percentageY);
        }


        String dateFormatted = clock.getDateFormatted();



        graphics.drawCenteredString(this.font, this.title, 2, 20, 16777215);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        // WORKING FOR EVERY SIZE, LETS GO !!

        x = (int) (clock.getPosX() / 100.0 * minecraft.getWindow().getGuiScaledWidth());
        y = (int) (clock.getPosY() / 100.0 * minecraft.getWindow().getGuiScaledHeight());

        double rectWidth = font.width(dateFormatted) + 3;
        double rectHeight = 12;

        x = Math.max(0, x);
        x = (int) Math.min(width - rectWidth, x);

        y = Math.max(y, 0);
        y = (int) Math.min(y, height - rectHeight);
        int textX = x + 2;
        int textY = y + 2;

        if(clock.isDrawBackground()) {
            if (clock.getRgbModeBackground() == HUDMode.RGB_WAVE) {
                for (int i = 0; i < rectWidth; i++) {
                    float hueStart = 1.0F - ((i - waveCounterBackground) / 360f); // Inversion de la couleur

                    float hueEnd = 1.0F - ((i + 1 - waveCounterBackground) / 360f); // Inversion de la couleur

                    if (clock.isBackgroundRightToLeftDirection()) {
                        hueStart = (i + waveCounterBackground) / 360f; // Inversion de la couleur
                        hueEnd = (i + 4 + waveCounterBackground) / 360f; // Inversion de la couleur

                    }

                    int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                    int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);

                    colorStart = (colorStart & 0x00FFFFFF) | (clock.getAlphaBackground() << 24);

                    colorEnd = (colorEnd & 0x00FFFFFF) | (clock.getAlphaBackground() << 24);

                    // Dessiner une colonne du rectangle avec le dégradé de couleur
                    drawGradientRect((x + i), y, x + i + 1, y + rectHeight, 0, colorStart, colorEnd, colorStart, colorEnd);
                }
            } else if (clock.getRgbModeBackground() == HUDMode.RGB_CYCLE) {

                float hueStart = 1.0F - ((float) (waveCounterBackground) / 360f); // Inversion de la couleur

                if (clock.isBackgroundRightToLeftDirection()) {
                    hueStart = (float) (waveCounterBackground) / 360f; // Inversion de la couleur
                }

                float hueEnd = hueStart; // Utilisez la même couleur pour le coin opposé

                int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);


                colorStart = (colorStart & 0x00FFFFFF) | (clock.getAlphaBackground() << 24);

                colorEnd = (colorEnd & 0x00FFFFFF) | (clock.getAlphaBackground() << 24);
                // Dessiner une colonne du rectangle avec le dégradé de couleur
                drawGradientRect(x, y, x + rectWidth, y + rectHeight, 0, colorStart, colorStart, colorEnd, colorEnd);
            } else {
                int colorBackground = (clock.getAlphaBackground() << 24 | clock.getRedBackground() << 16 | clock.getGreenBackground() << 8 | clock.getBlueBackground());

                graphics.fill(x, y - 2, (int) (x + rectWidth), y + 8 + 4, colorBackground);
            }

        }


        if(clock.getRgbModeText() == HUDMode.RGB_WAVE) {
            int textCharX = textX;

            for (int i = 0; i < dateFormatted.length(); i++) {
                char c = dateFormatted.charAt(i);

                int color = getColor(dateFormatted, i);

                graphics.drawString(font, String.valueOf(c), textCharX, textY, color, false);
                textCharX += minecraft.font.width(String.valueOf(c));
            }
        } else if (clock.getRgbModeText() == HUDMode.RGB_CYCLE) {

            float hueStart = 1.0F - ((float) (waveCounterText) / 255); // Inversion de la couleur

            if (clock.isTextRightToLeftDirection()) {
                hueStart = (float) ( waveCounterText) / 255; // Inversion de la couleur
            }


            int color = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);

            color = (color & 0x00FFFFFF) | (clock.getAlphaText() << 24);

            graphics.drawString(font, dateFormatted, textX, textY, color, false);

        } else {
            int colorText = (clock.getAlphaText() << 24 | clock.getRedText() << 16 | clock.getGreenText() << 8 | clock.getBlueText());

            graphics.drawString(font, dateFormatted, textX, textY, colorText, false);
        }
    }

    private int getColor(String dateFormatted, int i) {
        float hue = 1.0F - ((float) (dateFormatted.length() - i + waveCounterText) * 2 / 360f); // Inversion de la couleur

        if (clock.isTextRightToLeftDirection())
            hue = (float) (dateFormatted.length() + i + waveCounterText) * 2 / 360f; // Inversion de la couleur

        float saturation = 1.0F;
        float brightness = 1.0F;

        int color = Color.HSBtoRGB(hue, saturation, brightness);

        color = (color & 0x00FFFFFF) | (clock.getAlphaText() << 24);
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
