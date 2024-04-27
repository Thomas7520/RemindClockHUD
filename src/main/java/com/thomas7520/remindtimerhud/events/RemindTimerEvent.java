package com.thomas7520.remindtimerhud.events;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.thomas7520.remindtimerhud.RemindTimerHUD;
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
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

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
            waveCounterText += (RemindTimerHUD.getClock().getRgbSpeedText() - 1) / (100 - 1) * (10 - 1) + 1;
            waveCounterBackground += (RemindTimerHUD.getClock().getRgbSpeedBackground() - 1) / (100 - 1) * (20 - 1) + 1;
        }
    }
    private static int waveCounterBackground;
    private static int waveCounterText;
    @SubscribeEvent
    public static void hudEvent(RenderGuiOverlayEvent.Post event) {


        if(event.getOverlay() != VanillaGuiOverlay.AIR_LEVEL.type()) return;

        for (Remind remind : RemindTimerUtil.getReminds()) {
            if (remind.isPaused() || remind.isRinging()) continue;

            event.getGuiGraphics().drawString(Minecraft.getInstance().font, remind.getTimeFormatted(), 0, 0, Color.CYAN.getRGB());
        }


        //RemindTimerUtil.circleDoubleProgress(event.getGuievent.getGuiGraphics()().guiWidth() / 2, event.getGuievent.getGuiGraphics()().guiHeight() / 2, 10F, 6F, 180, 180F, 64, new Color(91, 13, 13, 200));


        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        Clock clock = RemindTimerHUD.getClock();

        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();

        Font font = Minecraft.getInstance().font;
        String dateFormatted = clock.getDateFormatted();

        float x = (float) (clock.getPosX() / 100.0 * event.getWindow().getGuiScaledWidth());
        float y = (float) (clock.getPosY() / 100.0 * event.getWindow().getGuiScaledHeight());

        int rectWidth = font.width(dateFormatted) + 3;
        int rectHeight = 12;

        x = Math.max(0, x);
        x = Math.min(width - rectWidth, x);

        y = Math.max(y, 2);
        y = Math.min(y, height - rectHeight);

        int textX = 2;
        int textY = 2;

        event.getGuiGraphics().pose().pushPose();
        event.getGuiGraphics().pose().translate(x,y, 0);


        if(clock.isDrawBackground()) {
            if (clock.getRgbModeBackground() == HUDMode.WAVE) {
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
                    drawGradientRect(x+i, y, i + 1, y+rectHeight, 0, colorStart, colorEnd, colorStart, colorEnd);
                }
            } else if (clock.getRgbModeBackground() == HUDMode.CYCLE) {

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
                drawGradientRect(x, y, x+rectWidth, y+rectHeight, 0, colorStart, colorStart, colorEnd, colorEnd);
            } else {
                int colorBackground = (clock.getAlphaBackground() << 24 | clock.getRedBackground() << 16 | clock.getGreenBackground() << 8 | clock.getBlueBackground());

                event.getGuiGraphics().fill(0, -2, rectWidth, 8 + 4, colorBackground);
            }

        }


        if(clock.getRgbModeText() == HUDMode.WAVE) {
            int textCharX = textX;

            for (int i = 0; i < dateFormatted.length(); i++) {
                char c = dateFormatted.charAt(i);

                int color = getColor(dateFormatted, i);

                event.getGuiGraphics().drawString(font, String.valueOf(c), textCharX, textY, color, false);
                textCharX += font.width(String.valueOf(c));
            }
        } else if (clock.getRgbModeText() == HUDMode.CYCLE) {

            float hueStart = 1.0F - ((float) (waveCounterText) / 255); // Inversion de la couleur

            if (clock.isTextRightToLeftDirection()) {
                hueStart = (float) ( waveCounterText) / 255; // Inversion de la couleur
            }


            int color = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);

            color = (color & 0x00FFFFFF) | (clock.getAlphaText() << 24);

            event.getGuiGraphics().drawString(font, dateFormatted, textX, textY, color, false);

        } else {
            int colorText = (clock.getAlphaText() << 24 | clock.getRedText() << 16 | clock.getGreenText() << 8 | clock.getBlueText());

            event.getGuiGraphics().drawString(font, dateFormatted, textX, textY, colorText, false);
        }

        event.getGuiGraphics().pose().popPose();

        RenderSystem.disableBlend();
    }

    private static void drawGradientRect(double left, double top, double right, double bottom, int z, int coltl, int coltr, int colbl,
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

    private static int getColor(String dateFormatted, int i) {
        float hue = 1.0F - ((float) (dateFormatted.length() - i + waveCounterText) * 2 / 360f); // Inversion de la couleur

        if (RemindTimerHUD.getClock().isTextRightToLeftDirection())
            hue = (float) (dateFormatted.length() + i + waveCounterText) * 2 / 360f; // Inversion de la couleur

        float saturation = 1.0F;
        float brightness = 1.0F;

        int color = Color.HSBtoRGB(hue, saturation, brightness);

        color = (color & 0x00FFFFFF) | (RemindTimerHUD.getClock().getAlphaText() << 24);
        return color;
    }
}
