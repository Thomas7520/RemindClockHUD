package com.thomas7520.remindclockhud.screens;



import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class GuiColorPicker extends Screen {

    private static int WIDTH = 300;
    private static int HEIGHT = 200;
    private static int RECT_SIZE = 150;
    private static int RECT_MARGIN = 20;
    private static int HUE_BAR_HEIGHT = 20;

    private float hue = 0.0f; // Hue value between 0 and 1

    protected GuiColorPicker(Component pTitle) {
        super(pTitle);
        RECT_SIZE = 50;

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        int centerX = pGuiGraphics.guiWidth() / 2;
        int centerY = pGuiGraphics.guiHeight() / 2;

        // Draw color rectangle
        //pGuiGraphics.fill(centerX - RECT_SIZE / 2, centerY - RECT_SIZE / 2, centerX + RECT_SIZE / 2, centerY + RECT_SIZE / 2, Color.HSBtoRGB(hue, 1.0f, 1.0f));

        int colorTopLeft = Color.HSBtoRGB(hue, 0f, 1);
        int colorTopRight = Color.HSBtoRGB(hue, 1.0f, 1);

        int colorBottomLeft = Color.HSBtoRGB(hue, 1, 0);
        int colorBottomRight = Color.HSBtoRGB(hue, 1, 0);

        drawGradientRect(centerX - RECT_SIZE / 2, centerY - RECT_SIZE, centerX + RECT_SIZE / 2, centerY + RECT_SIZE, 0, colorTopLeft, colorTopRight, colorBottomLeft, colorBottomRight);
        // Draw hue selection bar
        for (int x = 0; x < WIDTH; x++) {
            float hueValue = (float) x / (float) WIDTH;
            pGuiGraphics.fill(centerX - RECT_SIZE / 2 + x, centerY + RECT_SIZE / 2 + RECT_MARGIN, centerX - RECT_SIZE / 2 + x + 1, centerY + RECT_SIZE / 2 + RECT_MARGIN + HUE_BAR_HEIGHT, Color.HSBtoRGB(hueValue, 1.0f, 1.0f));
        }
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {

        int centerX = width / 2;
        int centerY = height / 2;

        // Check if mouse is within the color rectangle
        if (mouseX >= centerX - WIDTH && mouseX <= centerX + WIDTH &&
                mouseY >= centerY - RECT_SIZE / 2 && mouseY <= centerY + RECT_SIZE / 2) {
            // Calculate the hue value based on the mouse position
            hue = (float) GuiColorPicker.clamp((mouseX - (centerX - RECT_SIZE / 2)) / (float) RECT_SIZE, 0.0f, 1.0f);
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(max, value));
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
