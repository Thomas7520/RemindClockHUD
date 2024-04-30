package com.thomas7520.remindclockhud.screens;

import com.thomas7520.remindclockhud.RemindClockHUD;
import com.thomas7520.remindclockhud.screens.buttons.CustomButton;
import com.thomas7520.remindclockhud.screens.chronometer.ChronometerScreen;
import com.thomas7520.remindclockhud.screens.clock.ClockScreen;
import com.thomas7520.remindclockhud.screens.timer.TimerScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.function.Supplier;

public class MenuScreen extends Screen {


    private int guiLeft;
    private int guiTop;

    public MenuScreen() {
        super(Component.translatable("remindclockhud.title"));
    }


    @Override
    protected void init() {
        this.guiLeft = (this.width) / 2;
        this.guiTop = (this.height) / 2;

        addRenderableWidget(new CustomButton(guiLeft - 170 , guiTop - 25, 50, 50, Component.empty(), p_93751_ -> {
            minecraft.setScreen(new ClockScreen(this, RemindClockHUD.getClock()));
        }, Supplier::get){

            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.render(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.blit(new ResourceLocation(RemindClockHUD.MODID, "textures/clock.png"), getX() + 5, getY()+ 5, 0, 0F, 0F, 40, 40, 40, 40);

                Component clockTitle = Component.translatable("config.clock");

                guiGraphics.drawString(font, clockTitle.getString(), getX() + getWidth() / 2f - (font.width(clockTitle.getString()) / 2f), getY() + 55, Color.WHITE.getRGB(), false);
            }
        });

        addRenderableWidget(new CustomButton(guiLeft - 170 + 95, guiTop - 25, 50, 50, Component.empty(), p_93751_ -> {
            minecraft.setScreen(new ChronometerScreen(this, RemindClockHUD.getChronometer()));
        }, Supplier::get){

            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.render(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.blit(new ResourceLocation(RemindClockHUD.MODID, "textures/chronometer.png"), getX(), getY() - 2, 0, 0F, 0F, 50, 50, 50, 50);

                Component chronometerTitle = Component.translatable("config.chronometer");

                guiGraphics.drawString(font, chronometerTitle.getString(), getX() + getWidth() / 2f - (font.width(chronometerTitle.getString()) / 2f), getY() + 55, Color.WHITE.getRGB(), false);
            }
        });

        Button remindButton = addRenderableWidget(new CustomButton(guiLeft - 170 + 95 * 2, guiTop - 25, 50, 50, Component.empty(), p_93751_ -> {
            minecraft.setScreen(new TimerScreen(this));
        }, Supplier::get){

            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {


                super.render(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.blit(new ResourceLocation(RemindClockHUD.MODID, "textures/remind.png"), getX() + 5, getY() + 2, 0, 0F, 0F, 40, 40, 40, 40);

                Component remindTitle = Component.translatable("config.remind");

                guiGraphics.drawString(font, remindTitle.getString(), getX() + getWidth() / 2f - (font.width(remindTitle.getString()) / 2f), getY() + 55, Color.WHITE.getRGB(), false);

            }
        });

        Component discordLink = Component.translatable("config.needhelp");

        addRenderableWidget(Button.builder(discordLink, p_93751_ -> this.minecraft.setScreen(new ConfirmLinkScreen((p_169337_) -> {
            if (p_169337_) {
                Util.getPlatform().openUri("https://discord.gg/xTqj3ZSeH4");
            }

            this.minecraft.setScreen(this);
        }, "https://discord.gg/xTqj3ZSeH4", true))).bounds(guiLeft - 100, height - 40, 200, 20)
                .build());

        Button alarmButton = addRenderableWidget(new CustomButton(guiLeft - 170 + 95 * 3, guiTop - 25, 50, 50, Component.empty(), p_93751_ -> {
        }, Supplier::get){

            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

                super.render(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.blit(new ResourceLocation(RemindClockHUD.MODID, "textures/alarm.png"), getX() + 5, getY()+ 5, 0, 0F, 0F, 40, 40, 40, 40);

                Component alarmTitle = Component.translatable("config.alarm");

                guiGraphics.drawString(font, alarmTitle.getString(), getX() + getWidth() / 2f - (font.width(alarmTitle.getString()) / 2f), getY() + 55, Color.WHITE.getRGB(), false);

            }
        });

        alarmButton.active = false;
        alarmButton.setTooltip(Tooltip.create(Component.literal("Now available yet. Follow progress on discord").withStyle(ChatFormatting.RED)));
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float p_282465_) {


        renderBackground(graphics);



        super.render(graphics, x, y, p_282465_);

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);

    }
}
