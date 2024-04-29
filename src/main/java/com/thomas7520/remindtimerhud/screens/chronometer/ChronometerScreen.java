package com.thomas7520.remindtimerhud.screens.chronometer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thomas7520.remindtimerhud.RemindTimerHUD;
import com.thomas7520.remindtimerhud.object.Chronometer;
import com.thomas7520.remindtimerhud.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.util.ArrayList;
import java.util.List;

public class ChronometerScreen extends Screen {


    private final Chronometer chronometer;

    private final String[] stateValues = {"text.red", "text.green", "text.blue", "text.alpha"};
    private ForgeSlider sliderRedText, sliderGreenText, sliderBlueText, sliderAlphaText, sliderRGBText;
    private ForgeSlider sliderRedBackground, sliderGreenBackground, sliderBlueBackground, sliderAlphaBackground, sliderRGBBackground;
    private final Screen lastScreen;
    private Button buttonWaveDirection;
    private Button buttonWaveDirectionBackground;
    private ButtonDropDown predefineFormatsButton;

    public ChronometerScreen(Screen lastScreen, Chronometer Chronometer) {
        super(Component.translatable("chronometer.title"));
        this.lastScreen = lastScreen;
        this.chronometer = Chronometer;
    }



    @Override
    protected void init() {
        List<ButtonDropDown.Entry> formatEntries = new ArrayList<>();

        for (ChronometerFormat value : ChronometerFormat.values()) {
            formatEntries.add(new ButtonDropDown.Entry(value.name(), pEntry -> {
                chronometer.setFormat(ChronometerFormat.valueOf(pEntry.getName()));
                predefineFormatsButton.setMessage(Component.translatable("chronometer.format", value.name()));
            }));
        }

        predefineFormatsButton = ButtonDropDown.builder(Component.translatable("chronometer.format", chronometer.getFormat().name()))
                .bounds(0,0, 154, 20)
                .addEntries(formatEntries)
                .build();


        Button displayMode = Button.builder(Component.translatable("text.display_position"), pButton -> {
                    minecraft.setScreen(new ChronometerPositionScreen(this));
                }).bounds(0,0, 154, 20)
                .build();

        Button rgbTextMode = Button.builder(Component.translatable("text.text_mode", chronometer.getRgbModeText().name()), pButton -> {
                    chronometer.setRgbModeText(getNextMode(chronometer.getRgbModeText()));
                    sliderRGBText.visible = chronometer.getRgbModeText() == HUDMode.WAVE || chronometer.getRgbModeText() == HUDMode.CYCLE;
                    buttonWaveDirection.visible = chronometer.getRgbModeText() == HUDMode.WAVE;
                    pButton.setMessage(Component.translatable("text.text_mode", chronometer.getRgbModeText().name()));
                }).bounds(0,0, 154, 20)
                .build();

        Button rgbBackgroundMode = Button.builder(Component.translatable("text.background_mode", chronometer.getRgbModeBackground().name()), pButton -> {
                    chronometer.setRgbModeBackground(getNextMode(chronometer.getRgbModeBackground()));
                    sliderRGBBackground.visible = chronometer.getRgbModeBackground() == HUDMode.WAVE || chronometer.getRgbModeBackground() == HUDMode.CYCLE;
                    buttonWaveDirectionBackground.visible = chronometer.getRgbModeBackground() == HUDMode.WAVE;
                    pButton.setMessage(Component.translatable("text.background_mode", chronometer.getRgbModeBackground().name()));
                }).bounds(0,0, 154, 20)
                .build();

        buttonWaveDirection = Button.builder(Component.translatable(chronometer.isTextRightToLeftDirection() ? "text.direction_lr" : "text.direction_rl"), pButton -> {
                    chronometer.setTextRightToLeftDirection(!chronometer.isTextRightToLeftDirection());
                    pButton.setMessage(Component.translatable(chronometer.isTextRightToLeftDirection() ? "text.direction_lr" : "text.direction_rl"));
        }).bounds(0,0, 100, 20)
                .build();

        buttonWaveDirection.visible = chronometer.getRgbModeText() == HUDMode.WAVE;

        buttonWaveDirectionBackground = Button.builder(Component.translatable(chronometer.isTextRightToLeftDirection() ? "text.direction_lr" : "text.direction_rl"), pButton -> {
                    chronometer.setBackgroundRightToLeftDirection(!chronometer.isBackgroundRightToLeftDirection());
                    pButton.setMessage(Component.translatable(chronometer.isBackgroundRightToLeftDirection() ? "text.direction_lr" : "text.direction_rl"));
                }).bounds(0,0, 100, 20)
                .build();

        buttonWaveDirectionBackground.visible = chronometer.getRgbModeBackground() == HUDMode.WAVE;


        Button buttonBackgroundState = Button.builder(Component.translatable(chronometer.isDrawBackground() ? "text.disable_background" : "text.enable_background"), pButton -> {
                    chronometer.setDrawBackground(!chronometer.isDrawBackground());
                    pButton.setMessage(Component.translatable(chronometer.isDrawBackground() ? "text.disable_background" : "text.enable_background"));
                }).bounds(0, 0, 154, 20)
                .build();

        Button buttonIdleState = Button.builder(Component.translatable(chronometer.isIdleRender() ? "chronometer.idle_off" : "chronometer.idle_on"), pButton -> {
                    chronometer.setIdleRender(!chronometer.isIdleRender());
                    pButton.setMessage(Component.translatable(chronometer.isIdleRender() ? "chronometer.idle_off" : "chronometer.idle_on"));
                }).bounds(0, 0, 154, 20)
                .build();


        int i = 1;
        sliderRedText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, chronometer.getRedText(), 1, 1, true) {


            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindTimerUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xff00ffff, col1 | 0x00ff0000, col1 & 0xff00ffff,
                        col1 | 0x00ff0000);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                chronometer.setRedText(getValueInt());
                super.applyValue();
            }

        };

        i++;

        sliderGreenText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, chronometer.getGreenText(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindTimerUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffff00ff, col1 | 0x0000ff00, col1 & 0xffff00ff,
                        col1 | 0x0000ff00);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                chronometer.setGreenText(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderBlueText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, chronometer.getBlueText(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindTimerUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffffff00, col1 | 0x000000ff, col1 & 0xffffff00,
                        col1 | 0x000000ff);


                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                chronometer.setBlueText(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderAlphaText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.literal("%")
                , 0, 100, (100 * (chronometer.getAlphaText()-25)) / (255.0 - 25), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                RenderSystem.setShaderColor(1,1,1,1);
                guiGraphics.blit(new ResourceLocation(RemindTimerHUD.MODID, "textures/transparency.png"), getX() + 1, getY() + 1, 0, 0F, 0F, getWidth() - 2, getHeight() - 2,  10,10);

                RemindTimerUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,rgb |  (0x46 << 24), rgb | (0xFF << 24), rgb | (0x46 << 24), rgb | (0xFF << 24));
                RenderSystem.setShaderColor(1,1,1,1);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            public int getValueInt() {
                return (int) (((float) super.getValueInt() / 100) * (255 - 25) + 25);
            }

            @Override
            protected void applyValue() {
                chronometer.setAlphaText(getValueInt());
                super.applyValue();
            }
        };

        sliderRGBText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable("text.speed").getString() + " : "), Component.literal("%")
                , 1, 100, chronometer.getRgbSpeedText(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
            }


            @Override
            protected void applyValue() {
                chronometer.setRgbSpeedText(getValueInt());
                super.applyValue();
            }
        };

        sliderRGBText.visible = chronometer.getRgbModeText() == HUDMode.WAVE || chronometer.getRgbModeText() == HUDMode.CYCLE;

        i = 1;
        sliderRedBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, chronometer.getRedBackground(), 1, 1, true) {


            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindTimerUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xff00ffff, col1 | 0x00ff0000, col1 & 0xff00ffff,
                        col1 | 0x00ff0000);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                chronometer.setRedBackground(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderGreenBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, chronometer.getGreenBackground(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindTimerUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffff00ff, col1 | 0x0000ff00, col1 & 0xffff00ff,
                        col1 | 0x0000ff00);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                chronometer.setGreenBackground(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderBlueBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, chronometer.getBlueBackground(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindTimerUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffffff00, col1 | 0x000000ff, col1 & 0xffffff00,
                        col1 | 0x000000ff);


                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                chronometer.setBlueBackground(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderAlphaBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.literal("%")
                , 0, 100, (100 * chronometer.getAlphaBackground()) / 255.0, 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                RenderSystem.setShaderColor(1,1,1,1);
                guiGraphics.blit(new ResourceLocation(RemindTimerHUD.MODID, "textures/transparency.png"), getX() + 1, getY() + 1, 0, 0F, 0F, getWidth() - 2, getHeight() - 2,  10,10);

                RemindTimerUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,rgb |  (0x46 << 24), rgb | (0xFF << 24), rgb | (0x46 << 24), rgb | (0xFF << 24));
                RenderSystem.setShaderColor(1,1,1,1);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            public int getValueInt() {
                return (int) (((float) super.getValueInt() / 100) * (255));
            }

            @Override
            protected void applyValue() {
                chronometer.setAlphaBackground(getValueInt());
                super.applyValue();
            }
        };

        sliderRGBBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable("text.speed").getString() + " : "), Component.literal("%")
                , 1, 100, chronometer.getRgbSpeedBackground(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
            }



            @Override
            protected void applyValue() {
                chronometer.setRgbSpeedBackground(getValueInt());
                super.applyValue();
            }
        };

        sliderRGBBackground.visible = chronometer.getRgbModeBackground() == HUDMode.WAVE || chronometer.getRgbModeBackground() == HUDMode.CYCLE;


        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_280842_) -> {
            saveConfig();
            this.minecraft.setScreen(this.lastScreen);
        }).bounds(this.width / 2 - 100, this.height - 27, 200, 20).build());


        net.minecraft.client.gui.layouts.GridLayout gridlayout = new net.minecraft.client.gui.layouts.GridLayout();
        gridlayout.defaultCellSetting().padding(4, 4, 4, 0);

        GridLayout.RowHelper gridlayout$rowhelper = gridlayout.createRowHelper(3);

        gridlayout$rowhelper.addChild(predefineFormatsButton);
        gridlayout$rowhelper.addChild(sliderRedText);
        gridlayout$rowhelper.addChild(sliderRedBackground);

        gridlayout$rowhelper.addChild(displayMode);
        gridlayout$rowhelper.addChild(sliderGreenText);
        gridlayout$rowhelper.addChild(sliderGreenBackground);

        gridlayout$rowhelper.addChild(rgbTextMode);
        gridlayout$rowhelper.addChild(sliderBlueText);
        gridlayout$rowhelper.addChild(sliderBlueBackground);

        gridlayout$rowhelper.addChild(rgbBackgroundMode);
        gridlayout$rowhelper.addChild(sliderAlphaText);
        gridlayout$rowhelper.addChild(sliderAlphaBackground);

        gridlayout$rowhelper.addChild(buttonBackgroundState);
        gridlayout$rowhelper.addChild(sliderRGBText);
        gridlayout$rowhelper.addChild(sliderRGBBackground);

        gridlayout$rowhelper.addChild(buttonIdleState);
        gridlayout.addChild(buttonWaveDirection, 5, 1, gridlayout.defaultCellSetting());
        gridlayout.addChild(buttonWaveDirectionBackground, 5, 2, gridlayout.defaultCellSetting());

        gridlayout.arrangeElements();

        FrameLayout.alignInRectangle(gridlayout, 0, 0, this.width, this.height, 0.5F, 0.5F);

        gridlayout.visitWidgets(this::addRenderableWidget);
        super.init();

    }





    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float p_282465_) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, p_282465_);

        String chronometerFormatted = chronometer.getFormat().formatTime(System.currentTimeMillis());


        graphics.drawCenteredString(this.font, this.title, this.width / 2, 3, 16777215);

        int x = width / 2 - font.width(chronometerFormatted) / 2;
        int y = predefineFormatsButton.getY() - 20;

        RemindTimerUtil.drawChronometer(chronometer, chronometerFormatted, graphics, font, x, y, width, height);
    }

    private HUDMode getNextMode(HUDMode currentOption) {
        HUDMode[] values = HUDMode.values();
        int currentIndex = currentOption.ordinal();
        int nextIndex = (currentIndex + 1) % values.length;
        return values[nextIndex];
    }

    @Override
    public void onClose() {
        saveConfig();
        super.onClose();
    }

    private void saveConfig() {
        RemindTimerConfig.Client.Chronometer configChronometer = RemindTimerConfig.CLIENT.chronometer;

        configChronometer.format.set(chronometer.getFormat());
        configChronometer.drawBackground.set(chronometer.isDrawBackground());

        configChronometer.rgbModeText.set(chronometer.getRgbModeText());
        configChronometer.rgbModeBackground.set(chronometer.getRgbModeBackground());

        configChronometer.redText.set(chronometer.getRedText());
        configChronometer.greenText.set(chronometer.getGreenText());
        configChronometer.blueText.set(chronometer.getBlueText());
        configChronometer.alphaText.set(chronometer.getAlphaText());
        configChronometer.rgbSpeedText.set(chronometer.getRgbSpeedText());

        configChronometer.redBackground.set(chronometer.getRedBackground());
        configChronometer.greenBackground.set(chronometer.getGreenBackground());
        configChronometer.blueBackground.set(chronometer.getBlueBackground());
        configChronometer.alphaBackground.set(chronometer.getAlphaBackground());
        configChronometer.rgbSpeedBackground.set(chronometer.getRgbSpeedBackground());

        configChronometer.textRightToLeftDirection.set(chronometer.isTextRightToLeftDirection());
        configChronometer.backgroundRightToLeftDirection.set(chronometer.isBackgroundRightToLeftDirection());
    }
}
