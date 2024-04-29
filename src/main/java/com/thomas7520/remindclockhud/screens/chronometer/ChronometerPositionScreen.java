package com.thomas7520.remindclockhud.screens.chronometer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thomas7520.remindclockhud.RemindClockHUD;
import com.thomas7520.remindclockhud.object.Chronometer;
import com.thomas7520.remindclockhud.util.RemindClockConfig;
import com.thomas7520.remindclockhud.util.RemindClockUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ChronometerPositionScreen extends Screen {

    private final Screen lastScreen;
    public Chronometer chronometer;
    private boolean clicked;
    private double percentageX;
    private double percentageY;
    private double x;
    private double y;

    public ChronometerPositionScreen(Screen lastScreen) {
        super(Component.empty());

        this.lastScreen = lastScreen;
        this.chronometer = RemindClockHUD.getChronometer();
    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onClose() {
        minecraft.setScreen(lastScreen);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pMouseX >= x && pMouseX <= x + font.width(chronometer.getFormat().formatTime(System.currentTimeMillis())) + 3
                && pMouseY >= y && pMouseY <= y + 12) {
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
            RemindClockConfig.Client.Chronometer configChronometer = RemindClockConfig.CLIENT.chronometer;
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

        RemindClockUtil.drawChronometer(chronometer, dateFormatted, graphics, font, x, y, width, height);

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

        int rectWidth = font.width(chronometer.getFormat().formatTime(System.currentTimeMillis())) + 3;
        int rectHeight = 12;

        x = Math.max(0, x);
        x = Math.min(width - rectWidth, x);

        y = Math.max(y, 0);
        y = Math.min(y, height - rectHeight);

        percentageX = x / minecraft.getWindow().getGuiScaledWidth() * 100;
        percentageY = y / minecraft.getWindow().getGuiScaledHeight() * 100;

        percentageX = Math.min(100, Math.max(0, percentageX));
        percentageY = Math.min(100, Math.max(0, percentageY));

        chronometer.setPosX(percentageX);
        chronometer.setPosY(percentageY);

        RemindClockConfig.Client.Chronometer configChronometer = RemindClockConfig.CLIENT.chronometer;
        configChronometer.posX.set(chronometer.getPosX());
        configChronometer.posY.set(chronometer.getPosY());

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}