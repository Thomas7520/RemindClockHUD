package com.thomas7520.remindtimerhud.screens;

import com.thomas7520.remindtimerhud.RemindTimerHUD;
import com.thomas7520.remindtimerhud.screens.buttons.CustomButton;
import com.thomas7520.remindtimerhud.screens.clock.ClockScreen;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
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
        super(Component.translatable("remindtimerhud.menuscreen.title"));
    }


    @Override
    protected void init() {
        this.guiLeft = (this.width) / 2;
        this.guiTop = (this.height) / 2;

        addRenderableWidget(new CustomButton(guiLeft - 170 , guiTop - 25, 50, 50, Component.translatable(""), p_93751_ -> {


            minecraft.setScreen(new ClockScreen(this, RemindTimerHUD.getClock()));
        }, Supplier::get){

            @Override
            public void render(GuiGraphics p_282421_, int p_93658_, int p_93659_, float p_93660_) {
                super.render(p_282421_, p_93658_, p_93659_, p_93660_);
                p_282421_.blit(new ResourceLocation(RemindTimerHUD.MODID, "textures/clock.png"), getX() + 5, getY()+ 5, 0, 0F, 0F, 40, 40, 40, 40);

                p_282421_.drawString(font, "Heure", getX() + getWidth() / 2f - (font.width("Heure") / 2f), getY() + 55, Color.WHITE.getRGB(), false);
            }
        });

        addRenderableWidget(new CustomButton(guiLeft - 170 + 95, guiTop - 25, 50, 50, Component.translatable(""), p_93751_ -> {
        }, Supplier::get){

            @Override
            public void render(GuiGraphics p_282421_, int p_93658_, int p_93659_, float p_93660_) {

                super.render(p_282421_, p_93658_, p_93659_, p_93660_);
                p_282421_.blit(new ResourceLocation(RemindTimerHUD.MODID, "textures/alarm.png"), getX() + 5, getY()+ 5, 0, 0F, 0F, 40, 40, 40, 40);

                p_282421_.drawString(font, "Alarme", getX() + getWidth() / 2f - (font.width("Alarme") / 2f), getY() + 55, Color.WHITE.getRGB(), false);

            }
        });

        addRenderableWidget(new CustomButton(guiLeft - 170 + 95 * 2, guiTop - 25, 50, 50, Component.translatable(""), p_93751_ -> {
            Minecraft.getInstance().setScreen(new GuiColorPicker(Component.literal("ttt")));
            }, Supplier::get){

            @Override
            public void render(GuiGraphics p_282421_, int p_93658_, int p_93659_, float p_93660_) {


                super.render(p_282421_, p_93658_, p_93659_, p_93660_);
                p_282421_.blit(new ResourceLocation(RemindTimerHUD.MODID, "textures/chronometer.png"), getX(), getY() - 2, 0, 0F, 0F, 50, 50, 50, 50);

                p_282421_.drawString(font, "Chronomètre", getX() + getWidth() / 2f - (font.width("Chronomètre") / 2f), getY() + 55, Color.WHITE.getRGB(), false);

            }
        });

        addRenderableWidget(new CustomButton(guiLeft - 170 + 95 * 3, guiTop - 25, 50, 50, Component.translatable(""), p_93751_ -> {
        }, Supplier::get){

            @Override
            public void render(GuiGraphics p_282421_, int p_93658_, int p_93659_, float p_93660_) {


                super.render(p_282421_, p_93658_, p_93659_, p_93660_);
                p_282421_.blit(new ResourceLocation(RemindTimerHUD.MODID, "textures/remind.png"), getX() + 5, getY() + 2, 0, 0F, 0F, 40, 40, 40, 40);

                p_282421_.drawString(font, "Minuteur/Rappel", getX() + getWidth() / 2f - (font.width("Minuteur/Rappel") / 2f), getY() + 55, Color.WHITE.getRGB(), false);

            }
        });




        Component discordLink = Component.translatable("text.config.needhelp");

        addRenderableWidget(Button.builder(discordLink, p_93751_ -> this.minecraft.setScreen(new ConfirmLinkScreen((p_169337_) -> {
            if (p_169337_) {
                Util.getPlatform().openUri("https://discord.gg/xTqj3ZSeH4");
            }

            this.minecraft.setScreen(this);
        }, "https://discord.gg/xTqj3ZSeH4", true))).bounds(guiLeft - 100, height - 40, 200, 20)
                .build());

        super.init();
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float p_282465_) {


        renderBackground(graphics);



        super.render(graphics, x, y, p_282465_);

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);

    }
}
