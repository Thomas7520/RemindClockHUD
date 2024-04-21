package com.thomas7520.remindtimerhud.screens.clock;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.thomas7520.remindtimerhud.RemindTimerHUD;
import com.thomas7520.remindtimerhud.object.Clock;
import com.thomas7520.remindtimerhud.screens.buttons.CustomButton;
import com.thomas7520.remindtimerhud.screens.buttons.InformationButton;
import com.thomas7520.remindtimerhud.util.HUDMode;
import com.thomas7520.remindtimerhud.util.HUDPosition;
import com.thomas7520.remindtimerhud.util.RemindTimerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class ClockScreen extends Screen {


    private final Clock clock;
    // todo à côté du textbox pour l'heure, mettre un mini bouton avec un "?" et quand tu vises,
    //  todo hover avec tous les trucs possibles (mm, ss, hh, etc.. et laissez la possibilité de AM & PM ?)
    private int guiLeft;
    private int guiTop;

    private Button buttonWidth;
    private Button buttonHeight;

    private EditBox dateBox;

    private HUDPosition.EnumWidth widthPosition = HUDPosition.EnumWidth.PERCENT_50;

    private HUDPosition.EnumHeight heightPosition = HUDPosition.EnumHeight.PERCENT_50;

    private String dateFormatted;

    private final String[] stateValues = {I18n.get("text.red "), I18n.get("text.green "), I18n.get("text.blue "), I18n.get("text.alpha ")};
    private ForgeSlider sliderRedText, sliderGreenText, sliderBlueText, sliderAlphaText, sliderRGBText;
    private ForgeSlider sliderRedBackground, sliderGreenBackground, sliderBlueBackground, sliderAlphaBackground, sliderRGBBackground;
    private final Screen lastScreen;
    private CustomButton formatHourButton;

    private int waveCounterText;
    private int waveCounterBackground;

    private CustomButton buttonWaveDirection, buttonWaveDirectionBackground;
    private CustomButton buttonBackgroundState;

    public ClockScreen(Screen lastScreen, Clock clock) {
        super(Component.translatable("remindtimerhud.clockscreen.title"));
        this.lastScreen = lastScreen;
        this.clock = clock;
    }


    @Override
    protected void init() {
        this.guiLeft = (this.width / 2);
        this.guiTop = (this.height / 2);



        /*addRenderableWidget(*/buttonWidth = new CustomButton(guiLeft - 50 , guiTop - 10, 100, 20, Component.literal(widthPosition.name()), p_93751_ -> {
            widthPosition = getNextWidthOption(widthPosition);
            buttonWidth.setMessage(Component.literal(widthPosition.name()));

        }, Supplier::get)/*)*/;

        /*addRenderableWidget(*/buttonHeight = new CustomButton(guiLeft - 50 , guiTop - 30, 100, 20, Component.literal(heightPosition.name()), p_93751_ -> {
            heightPosition = getNextHeightOption(heightPosition);
            buttonHeight.setMessage(Component.literal(heightPosition.name()));
        }, Supplier::get)/*)*/;

        /*this.addRenderableWidget(*/dateBox = new EditBox(font, guiLeft / 4, guiTop - 80 + 30 + 24, 151, 20, Component.nullToEmpty(null)){
            @Override
            public boolean charTyped(char p_94122_, int p_94123_) {
                waveCounterText =0;
                return super.charTyped(p_94122_, p_94123_);
            }
        }/*)*/;
        dateBox.setCanLoseFocus(true);
        dateBox.setMaxLength(100);
        dateBox.setTooltip(Tooltip.create(Component.translatable("Date Format")));
        dateBox.setValue(clock.getFormatText());

//        dateBox.setValue("%day %dd %smonth %yyyy");

        formatHourButton = new CustomButton(guiLeft / 4 - 2, guiTop - 80 + 30 + 24 + 27, 154, 20, Component.translatable(clock.isUse12HourFormat() ? "12 hours format" : "24 hours format"), p_93751_ -> {
            clock.setUse12HourFormat(!clock.isUse12HourFormat());
            formatHourButton.setMessage(Component.translatable(clock.isUse12HourFormat() ? "12 hours format" : "24 hours format"));
        }, Supplier::get);

        CustomButton displayMode = new CustomButton(guiLeft - 200, guiTop - 80 + 30 + 24 * 2 + 27, 154, 20, Component.translatable("Change display position"), p_93751_ -> {
            minecraft.setScreen(new PositionScreen(Component.literal("Position")));
        }, Supplier::get);

        CustomButton rgbTextMode = new CustomButton(guiLeft / 4 - 2, guiTop - 80 + 30 + 24 * 3 + 27, 154, 20, Component.translatable("RGB Text Mode " + clock.getRgbModeText().name()), p_93751_ -> {
            clock.setRgbModeText(getNextMode(clock.getRgbModeText()));
            sliderRGBText.visible = clock.getRgbModeText() == HUDMode.RGB_WAVE || clock.getRgbModeText() == HUDMode.RGB_CYCLE;
            buttonWaveDirection.visible = clock.getRgbModeText() == HUDMode.RGB_WAVE;
            p_93751_.setMessage(Component.literal("RGB Mode" + clock.getRgbModeText().name()));
        }, Supplier::get);

        CustomButton rgbBackgroundMode = new CustomButton(guiLeft / 4 - 2, guiTop - 80 + 30 + 24 * 3 + 27, 154, 20, Component.translatable("RGB Background Mode " + clock.getRgbModeBackground()), p_93751_ -> {
            clock.setRgbModeBackground(getNextMode(clock.getRgbModeBackground()));
            sliderRGBBackground.visible = clock.getRgbModeBackground() == HUDMode.RGB_WAVE || clock.getRgbModeBackground() == HUDMode.RGB_CYCLE;
            buttonWaveDirectionBackground.visible = clock.getRgbModeBackground() == HUDMode.RGB_WAVE;
            p_93751_.setMessage(Component.literal("RGB Mode" + clock.getRgbModeBackground()));
        }, Supplier::get);

        buttonWaveDirection = new CustomButton(guiLeft / 4 - 2, guiTop - 80 + 30 + 24 * 4 + 27, 100, 20, Component.translatable("Direction " + (clock.isTextRightToLeftDirection() ? "RIGHT TO LEFT" : "LEFT TO RIGHT")), p_93751_ -> {
            clock.setTextRightToLeftDirection(!clock.isTextRightToLeftDirection());
            p_93751_.setMessage(Component.literal("Direction " + (clock.isTextRightToLeftDirection() ? "RIGHT TO LEFT" : "LEFT TO RIGHT")));
        }, Supplier::get);

        buttonWaveDirection.visible = clock.getRgbModeText() == HUDMode.RGB_WAVE;

        buttonWaveDirectionBackground = new CustomButton(guiLeft / 4 - 2, guiTop - 80 + 30 + 24 * 4 + 27, 100, 20, Component.translatable("Direction BG " + (clock.isBackgroundRightToLeftDirection() ? "RIGHT TO LEFT" : "LEFT TO RIGHT")), p_93751_ -> {
            clock.setBackgroundRightToLeftDirection(!clock.isBackgroundRightToLeftDirection());
            p_93751_.setMessage(Component.literal("Direction BG" + (clock.isBackgroundRightToLeftDirection() ? "RIGHT TO LEFT" : "LEFT TO RIGHT")));
        }, Supplier::get);

        buttonWaveDirectionBackground.visible = clock.getRgbModeBackground() == HUDMode.RGB_WAVE;



        buttonBackgroundState = new CustomButton(guiLeft / 4 - 2, guiTop - 80 + 30 + 24 * 4 + 27, 154, 20, Component.translatable("Draw background " + (clock.isDrawBackground() ? "ON" : "OFF")), p_93751_ -> {
            clock.setDrawBackground(!clock.isDrawBackground());
            p_93751_.setMessage(Component.literal("draw bG" + (clock.isDrawBackground() ? "YES" : "NO")));
        }, Supplier::get);





        List<String> tooltipLines = new ArrayList<>();
        
        tooltipLines.add("Liste available of formats :");
        
        tooltipLines.add("%hh");
        tooltipLines.add("%mm");
        tooltipLines.add("%ss");
        tooltipLines.add("%dd");
        tooltipLines.add("%day");
        tooltipLines.add("%sday");
        tooltipLines.add("%month");
        tooltipLines.add("%smonth");
        tooltipLines.add("%MM");
        tooltipLines.add("%yyyy");
        tooltipLines.add("%yy");

        StringBuilder tooltip = new StringBuilder();

        for (String s : tooltipLines) {
            tooltip.append(s).append("\n");
        }



        int i = 1;
        sliderRedText = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable(stateValues[i-1]), Component.empty()
        , 0, 255, clock.getRedText(), 1, 1, true) {


            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xff00ffff, col1 | 0x00ff0000, col1 & 0xff00ffff,
                        col1 | 0x00ff0000);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                clock.setRedText(getValueInt());
                super.applyValue();
            }

        };

        i++;

        sliderGreenText = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable(stateValues[i-1]), Component.empty()
                , 0, 255, clock.getGreenText(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffff00ff, col1 | 0x0000ff00, col1 & 0xffff00ff,
                        col1 | 0x0000ff00);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                clock.setGreenText(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderBlueText = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable(stateValues[i-1]), Component.empty()
                , 0, 255, clock.getBlueText(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffffff00, col1 | 0x000000ff, col1 & 0xffffff00,
                        col1 | 0x000000ff);


                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                clock.setBlueText(getValueInt());
                super.applyValue();
            }
        };

        i++;




        sliderAlphaText = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable(stateValues[i-1]), Component.literal("%")
                , 0, 100, (100 * (clock.getAlphaText()-25)) / (255.0 - 25), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                RenderSystem.setShaderColor(1,1,1,1);
                guiGraphics.blit(new ResourceLocation(RemindTimerHUD.MODID, "textures/transparency.png"), getX() + 1, getY() + 1, 0, 0F, 0F, getWidth() - 2, getHeight() - 2,  10,10);

                drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,rgb |  (0x46 << 24), rgb | (0xFF << 24), rgb | (0x46 << 24), rgb | (0xFF << 24));
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
                System.out.println(getValueInt());
                clock.setAlphaText(getValueInt());
                super.applyValue();
            }
        };


        i++;

        sliderRGBText = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable("RGB Mode Speed"), Component.literal("%")
                , 1, 100, clock.getRgbSpeedText(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
            }


            @Override
            protected void applyValue() {
                clock.setRgbSpeedText(getValueInt());
                super.applyValue();
            }
        };

        sliderRGBText.visible = clock.getRgbModeText() == HUDMode.RGB_WAVE || clock.getRgbModeText() == HUDMode.RGB_CYCLE;

        i = 1;
        sliderRedBackground = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable(stateValues[i-1]), Component.empty()
                , 0, 255, clock.getRedBackground(), 1, 1, true) {


            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xff00ffff, col1 | 0x00ff0000, col1 & 0xff00ffff,
                        col1 | 0x00ff0000);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                clock.setRedBackground(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderGreenBackground = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable(stateValues[i-1]), Component.empty()
                , 0, 255, clock.getGreenBackground(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffff00ff, col1 | 0x0000ff00, col1 & 0xffff00ff,
                        col1 | 0x0000ff00);

                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                clock.setGreenBackground(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderBlueBackground = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable(stateValues[i-1]), Component.empty()
                , 0, 255, clock.getBlueBackground(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffffff00, col1 | 0x000000ff, col1 & 0xffffff00,
                        col1 | 0x000000ff);


                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

                renderScrollingString(guiGraphics, mc.font, 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
            }

            @Override
            protected void applyValue() {
                clock.setBlueBackground(getValueInt());
                super.applyValue();
            }
        };

        i++;



        sliderAlphaBackground = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable(stateValues[i-1]), Component.literal("%")
                , 0, 100, (100 * clock.getAlphaBackground()) / 255.0, 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                RenderSystem.setShaderColor(1,1,1,1);
                guiGraphics.blit(new ResourceLocation(RemindTimerHUD.MODID, "textures/transparency.png"), getX() + 1, getY() + 1, 0, 0F, 0F, getWidth() - 2, getHeight() - 2,  10,10);

                drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,rgb |  (0x46 << 24), rgb | (0xFF << 24), rgb | (0x46 << 24), rgb | (0xFF << 24));
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
                clock.setAlphaBackground(getValueInt());
                super.applyValue();
            }
        };

        i++;

        sliderRGBBackground = new ForgeSlider((int) ((width / 2) * 1.5f - 135 / 2), guiTop - 80 + 30 + i * 20 + i * 4 , 100, 20, Component.translatable("RGB Mode Speed"), Component.literal("%")
                , 1, 100, clock.getRgbSpeedBackground(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
            }



            @Override
            protected void applyValue() {
                clock.setRgbSpeedBackground(getValueInt());
                super.applyValue();
            }
        };

        sliderRGBBackground.visible = clock.getRgbModeBackground() == HUDMode.RGB_WAVE || clock.getRgbModeBackground() == HUDMode.RGB_CYCLE;


        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_280842_) -> {
            saveConfig();
            this.minecraft.setScreen(this.lastScreen);
        }).bounds(this.width / 2 - 100, this.height - 27, 200, 20).build());


        net.minecraft.client.gui.layouts.GridLayout gridlayout = new net.minecraft.client.gui.layouts.GridLayout();
        gridlayout.defaultCellSetting().padding(4, 4, 4, 0);

        GridLayout.RowHelper gridlayout$rowhelper = gridlayout.createRowHelper(3);

        gridlayout$rowhelper.addChild(dateBox);
        gridlayout$rowhelper.addChild(sliderRedText);
        gridlayout$rowhelper.addChild(sliderRedBackground);

        gridlayout$rowhelper.addChild(formatHourButton);
        gridlayout$rowhelper.addChild(sliderGreenText);
        gridlayout$rowhelper.addChild(sliderGreenBackground);

        gridlayout$rowhelper.addChild(displayMode);
        gridlayout$rowhelper.addChild(sliderBlueText);
        gridlayout$rowhelper.addChild(sliderBlueBackground);
        gridlayout$rowhelper.addChild(rgbTextMode);


        gridlayout$rowhelper.addChild(sliderAlphaText);
        gridlayout$rowhelper.addChild(sliderAlphaBackground);

        gridlayout$rowhelper.addChild(rgbBackgroundMode);

        gridlayout$rowhelper.addChild(sliderRGBText);
        gridlayout$rowhelper.addChild(sliderRGBBackground);

        gridlayout.addChild(buttonBackgroundState, 5, 0, gridlayout.defaultCellSetting());
        gridlayout.addChild(buttonWaveDirection, 5, 1, gridlayout.defaultCellSetting());
        gridlayout.addChild(buttonWaveDirectionBackground, 5, 2, gridlayout.defaultCellSetting());

        gridlayout.arrangeElements();



        FrameLayout.alignInRectangle(gridlayout, 0, 0, this.width, this.height, 0.5F, 0.5F);

        InformationButton informationButton = new InformationButton(dateBox.getX() - 25, dateBox.getY(), 20,20, Component.translatable(""), p_93751_ -> {

        }, Supplier::get);

        dateBox.setX(dateBox.getX()+1);

        informationButton.setTooltip(Tooltip.create(Component.literal(tooltip.toString())));
        addRenderableWidget(informationButton);

        gridlayout.visitWidgets(this::addRenderableWidget);
        super.init();

    }


    @Override
    public void tick() {
        dateBox.tick();
        waveCounterText+=(sliderRGBText.getValue() - 1) / (100 - 1) * (10 - 1) + 1;
        waveCounterBackground+=(sliderRGBBackground.getValue() - 1) / (100 - 1) * (20 - 1) + 1;
        super.tick();
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float p_282465_) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, p_282465_);

        dateFormatted = clock.getDateFormatted();

        dateBox.render(graphics, mouseX, mouseY, p_282465_);

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 3, 16777215);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        int x = width / 2 - font.width(dateFormatted) / 2;
        int y = dateBox.getY() - 20;

        double rectX = width / 2f - font.width(dateFormatted) / 2f - 2;
        double rectY = y - 2;
        double rectWidth = font.width(dateFormatted) + 3;
        double rectHeight = 12;


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

                    colorStart = (colorStart & 0x00FFFFFF) | (sliderAlphaBackground.getValueInt() << 24);

                    colorEnd = (colorEnd & 0x00FFFFFF) | (sliderAlphaBackground.getValueInt() << 24);

                    // Dessiner une colonne du rectangle avec le dégradé de couleur
                    drawGradientRect((rectX + i), rectY, rectX + i + 1, rectY + rectHeight, 0, colorStart, colorEnd, colorStart, colorEnd);
                }
            } else if (clock.getRgbModeBackground() == HUDMode.RGB_CYCLE) {

                float hueStart = 1.0F - ((float) (waveCounterBackground) / 360f); // Inversion de la couleur

                if (clock.isBackgroundRightToLeftDirection()) {
                    hueStart = (float) (waveCounterBackground) / 360f; // Inversion de la couleur
                }

                float hueEnd = hueStart; // Utilisez la même couleur pour le coin opposé

                int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
                int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);


                colorStart = (colorStart & 0x00FFFFFF) | (sliderAlphaBackground.getValueInt() << 24);

                colorEnd = (colorEnd & 0x00FFFFFF) | (sliderAlphaBackground.getValueInt() << 24);
                // Dessiner une colonne du rectangle avec le dégradé de couleur
                drawGradientRect(rectX, rectY, rectX + rectWidth, rectY + rectHeight, 0, colorStart, colorStart, colorEnd, colorEnd);
            } else {
                int colorBackground = (sliderAlphaBackground.getValueInt() << 24 | sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                graphics.fill(width / 2 - font.width(dateFormatted) / 2 - 2, y - 2, width / 2 + font.width(dateFormatted) / 2 + 2, y + 8 + 2, colorBackground);
            }

        }


        if(clock.getRgbModeText() == HUDMode.RGB_WAVE) {
            for (int i = 0; i < dateFormatted.length(); i++) {
                char c = dateFormatted.charAt(i);

                float hue = 1.0F - ((float) (dateFormatted.length() - i + waveCounterText) * 2 / 360f); // Inversion de la couleur

                if (clock.isTextRightToLeftDirection())
                    hue = (float) (dateFormatted.length() + i + waveCounterText) * 2 / 360f; // Inversion de la couleur

                float saturation = 1.0F;
                float brightness = 1.0F;

                int color = Color.HSBtoRGB(hue, saturation, brightness);

                color = (color & 0x00FFFFFF) | (sliderAlphaText.getValueInt() << 24);

                graphics.drawString(font, String.valueOf(c), x, y, color, false);
                x += minecraft.font.width(String.valueOf(c));
            }
        } else if (clock.getRgbModeText() == HUDMode.RGB_CYCLE) {

        float hueStart = 1.0F - ((float) (waveCounterText) / 255); // Inversion de la couleur

            if (clock.isTextRightToLeftDirection()) {
                hueStart = (float) ( waveCounterText) / 255; // Inversion de la couleur
            }


            int color = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);

            color = (color & 0x00FFFFFF) | (sliderAlphaText.getValueInt() << 24);

            graphics.drawString(font, dateFormatted, x, y, color, false);

        } else {
            int colorText = (sliderAlphaText.getValueInt() << 24 | sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

            graphics.drawString(font, dateFormatted, x, y, colorText, false);
        }
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        if(!dateBox.isMouseOver(p_94695_, p_94696_) && dateBox.isFocused()) {
            dateBox.setFocused(false);
        }
        return super.mouseClicked(p_94695_, p_94696_, p_94697_);
    }


    @Override
    public boolean keyPressed(int p_96552_, int p_96553_, int p_96554_) {
        if(!dateBox.isFocused()) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), minecraft.options.keyUp.getKey().getValue()) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_UP)) {
                heightPosition = getPreviousHeightOption(heightPosition);
                buttonHeight.setMessage(Component.literal(heightPosition.name()));
                return false;
            }

            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), minecraft.options.keyDown.getKey().getValue()) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_DOWN)) {
                heightPosition = getNextHeightOption(heightPosition);
                buttonHeight.setMessage(Component.literal(heightPosition.name()));
                return false;
            }

            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), minecraft.options.keyLeft.getKey().getValue()) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LEFT)) {
                widthPosition = getPreviousWidthOption(widthPosition);
                buttonWidth.setMessage(Component.literal(widthPosition.name()));
                return false;
            }

            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), minecraft.options.keyRight.getKey().getValue()) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_RIGHT)) {
                widthPosition = getNextWidthOption(widthPosition);
                buttonWidth.setMessage(Component.literal(widthPosition.name()));
                return false;
            }
        }
        return super.keyPressed(p_96552_, p_96553_, p_96554_);
    }

    @Override
    public void onClose() {
        saveConfig();
        super.onClose();
    }

    private void saveConfig() {

        System.out.println(clock);
        RemindTimerConfig.Client config = RemindTimerConfig.CLIENT;

        config.formatText.set(clock.getFormatText());

        config.drawText.set(clock.isDrawText());
        config.drawBackground.set(clock.isDrawText());

        config.use12HourFormat.set(clock.isUse12HourFormat());

        config.rgbModeText.set(clock.getRgbModeText());
        config.rgbModeBackground.set(clock.getRgbModeBackground());

        config.redText.set(clock.getRedText());
        config.greenText.set(clock.getGreenText());
        config.blueText.set(clock.getBlueText());
        config.alphaText.set(clock.getAlphaText());
        config.rgbSpeedText.set(clock.getRgbSpeedText());

        config.redBackground.set(clock.getRedBackground());
        config.greenBackground.set(clock.getGreenBackground());
        config.blueBackground.set(clock.getBlueBackground());
        config.alphaBackground.set(clock.getAlphaBackground());
        config.rgbSpeedBackground.set(clock.getRgbSpeedBackground());

        config.textRightToLeftDirection.set(clock.isTextRightToLeftDirection());
        config.backgroundRightToLeftDirection.set(clock.isBackgroundRightToLeftDirection());

        config.textWidth.set(clock.getTextWidth());
        config.textHeight.set(clock.getTextHeight());

        config.backgroundWidth.set(clock.getBackgroundWidth());
        config.backgroundHeight.set(clock.getBackgroundHeight());
    }

    public int getWidthPosition() {
        switch (widthPosition) {

            case PERCENT_0 -> {
                return 2;
            }

            case PERCENT_25 -> {
                return width / 4 - font.width(dateFormatted) / 2;
            }

            case PERCENT_50 -> {
                return width / 2 - font.width(dateFormatted) / 2;
            }

            case PERCENT_75 -> {
                return (int) ((width / 2) * 1.5f - font.width(dateFormatted) / 2);
            }

            case PERCENT_100 -> {
                return width - font.width(dateFormatted) - 2;
            }
        }
        return 0;
    }

    public int getHeightPosition() {
        switch (heightPosition) {
            case PERCENT_0 -> {
                return 2;
            }

            case PERCENT_25 -> {
                return height / 4;
            }

            case PERCENT_50 -> {
                return height / 2;
            }

            case PERCENT_75 -> {
                return (int) ((height / 2) * 1.5f);
            }

            case PERCENT_100 -> {
                return height - 10;
            }
        }
        return 0;
    }

    private HUDPosition.EnumHeight getNextHeightOption(HUDPosition.EnumHeight currentOption) {
        HUDPosition.EnumHeight[] values = HUDPosition.EnumHeight.values();
        int currentIndex = currentOption.ordinal();
        int nextIndex = (currentIndex + 1) % values.length;
        return values[nextIndex];
    }

    private HUDPosition.EnumHeight getPreviousHeightOption(HUDPosition.EnumHeight currentOption) {
        HUDPosition.EnumHeight[] values = HUDPosition.EnumHeight.values();
        int currentIndex = currentOption.ordinal();
        int nextIndex = (currentIndex - 1 + values.length) % values.length;
        return values[nextIndex];
    }

    private HUDPosition.EnumWidth getNextWidthOption(HUDPosition.EnumWidth currentOption) {
        HUDPosition.EnumWidth[] values = HUDPosition.EnumWidth.values();
        int currentIndex = currentOption.ordinal();
        int nextIndex = (currentIndex + 1) % values.length;
        return values[nextIndex];
    }

    private HUDPosition.EnumWidth getPreviousWidthOption(HUDPosition.EnumWidth currentOption) {
        HUDPosition.EnumWidth[] values = HUDPosition.EnumWidth.values();
        int currentIndex = currentOption.ordinal();
        int nextIndex = (currentIndex - 1 + values.length) % values.length;
        return values[nextIndex];
    }

    private HUDMode getNextMode(HUDMode currentOption) {
        HUDMode[] values = HUDMode.values();
        int currentIndex = currentOption.ordinal();
        int nextIndex = (currentIndex + 1) % values.length;
        return values[nextIndex];
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

    public static Component genericValueLabel(Component p_231922_, Component p_231923_) {
        return Component.translatable("options.generic_value", p_231922_, p_231923_);
    }

}
