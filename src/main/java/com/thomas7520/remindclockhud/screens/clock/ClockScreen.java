package com.thomas7520.remindclockhud.screens.clock;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thomas7520.remindclockhud.RemindClockHUD;
import com.thomas7520.remindclockhud.object.Clock;
import com.thomas7520.remindclockhud.screens.buttons.InformationButton;
import com.thomas7520.remindclockhud.util.HUDMode;
import com.thomas7520.remindclockhud.util.RemindClockConfig;
import com.thomas7520.remindclockhud.util.RemindClockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
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
import java.util.function.Supplier;

public class ClockScreen extends Screen {


    private final Clock clock;
    private EditBox dateBox;
    private String dateFormatted;
    private final String[] stateValues = {"text.red", "text.green", "text.blue", "text.alpha"};
    private ForgeSlider sliderRedText, sliderGreenText, sliderBlueText, sliderAlphaText, sliderRGBText;
    private ForgeSlider sliderRedBackground, sliderGreenBackground, sliderBlueBackground, sliderAlphaBackground, sliderRGBBackground;
    private final Screen lastScreen;
    private Button formatHourButton;
    private Button buttonWaveDirection;
    private Button buttonWaveDirectionBackground;

    public ClockScreen(Screen lastScreen, Clock clock) {
        super(Component.translatable("clock.title"));
        this.lastScreen = lastScreen;
        this.clock = clock;
    }


    @Override
    protected void init() {

        dateBox = new EditBox(font, 0,0, 151, 20, Component.nullToEmpty(null)){

        };
        dateBox.setCanLoseFocus(true);
        dateBox.setMaxLength(100);
        dateBox.setTooltip(Tooltip.create(Component.translatable("clock.date_format")));
        dateBox.setValue(clock.getFormatText());
        dateBox.setResponder(newDate -> {
            clock.setFormatText(newDate);
            RemindClockConfig.CLIENT.clock.formatText.set(newDate);
        });

        formatHourButton = Button.builder(Component.translatable(clock.isUse12HourFormat() ? "clock.formatters12" : "clock.formatters24"), pButton -> {
                    clock.setUse12HourFormat(!clock.isUse12HourFormat());
                    pButton.setMessage(Component.translatable(clock.isUse12HourFormat() ? "clock.formatters12" : "clock.formatters24"));
        }).bounds(0,0, 154, 20)
                .build();

        Button displayMode = Button.builder(Component.translatable("text.display_position"), pButton -> {
                    minecraft.setScreen(new ClockPositionScreen(this));
                }).bounds(0,0, 154, 20)
                .build();

        Button rgbTextMode = Button.builder(Component.translatable("text.text_mode", clock.getRgbModeText().name()), pButton -> {
                    clock.setRgbModeText(getNextMode(clock.getRgbModeText()));
                    sliderRGBText.visible = clock.getRgbModeText() == HUDMode.WAVE || clock.getRgbModeText() == HUDMode.CYCLE;
                    buttonWaveDirection.visible = clock.getRgbModeText() == HUDMode.WAVE;
                    pButton.setMessage(Component.translatable("text.text_mode", clock.getRgbModeText().name()));
                }).bounds(0,0, 154, 20)
                .build();

        Button rgbBackgroundMode = Button.builder(Component.translatable("text.background_mode", clock.getRgbModeBackground().name()), pButton -> {
                    clock.setRgbModeBackground(getNextMode(clock.getRgbModeBackground()));
                    sliderRGBBackground.visible = clock.getRgbModeBackground() == HUDMode.WAVE || clock.getRgbModeBackground() == HUDMode.CYCLE;
                    buttonWaveDirectionBackground.visible = clock.getRgbModeBackground() == HUDMode.WAVE;
                    pButton.setMessage(Component.translatable("text.background_mode", clock.getRgbModeBackground().name()));
                }).bounds(0,0, 154, 20)
                .build();

        buttonWaveDirection = Button.builder(Component.translatable(clock.isTextRightToLeftDirection() ? "text.direction_lr" : "text.direction_rl"), pButton -> {
                    clock.setTextRightToLeftDirection(!clock.isTextRightToLeftDirection());
                    pButton.setMessage(Component.translatable(clock.isTextRightToLeftDirection() ? "text.direction_lr" : "text.direction_rl"));
                }).bounds(0,0, 100, 20)
                .build();

        buttonWaveDirection.visible = clock.getRgbModeText() == HUDMode.WAVE;

        buttonWaveDirectionBackground = Button.builder(Component.translatable(clock.isTextRightToLeftDirection() ? "text.direction_lr" : "text.direction_rl"), pButton -> {
                    clock.setBackgroundRightToLeftDirection(!clock.isBackgroundRightToLeftDirection());
                    pButton.setMessage(Component.translatable(clock.isBackgroundRightToLeftDirection() ? "text.direction_lr" : "text.direction_rl"));
                }).bounds(0,0, 100, 20)
                .build();

        buttonWaveDirectionBackground.visible = clock.getRgbModeBackground() == HUDMode.WAVE;


        Button buttonBackgroundState = Button.builder(Component.translatable(clock.isDrawBackground() ? "text.disable_background" : "text.enable_background"), pButton -> {
                    clock.setDrawBackground(!clock.isDrawBackground());
                    pButton.setMessage(Component.translatable(clock.isDrawBackground() ? "text.disable_background" : "text.enable_background"));
                }).bounds(0, 0, 154, 20)
                .build();



        List<String> tooltipLines = new ArrayList<>();

        tooltipLines.add(Component.translatable("clock.available_formats").getString());

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
        sliderRedText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, clock.getRedText(), 1, 1, true) {


            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindClockUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xff00ffff, col1 | 0x00ff0000, col1 & 0xff00ffff,
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

        sliderGreenText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, clock.getGreenText(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindClockUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffff00ff, col1 | 0x0000ff00, col1 & 0xffff00ff,
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

        sliderBlueText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, clock.getBlueText(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindClockUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffffff00, col1 | 0x000000ff, col1 & 0xffffff00,
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




        sliderAlphaText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.literal("%")
                , 0, 100, (100 * (clock.getAlphaText()-25)) / (255.0 - 25), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedText.getValueInt() << 16 | sliderGreenText.getValueInt() << 8 | sliderBlueText.getValueInt());

                RenderSystem.setShaderColor(1,1,1,1);
                guiGraphics.blit(new ResourceLocation(RemindClockHUD.MODID, "textures/transparency.png"), getX() + 1, getY() + 1, 0, 0F, 0F, getWidth() - 2, getHeight() - 2,  10,10);

                RemindClockUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,rgb |  (0x46 << 24), rgb | (0xFF << 24), rgb | (0x46 << 24), rgb | (0xFF << 24));
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
                clock.setAlphaText(getValueInt());
                super.applyValue();
            }
        };


        i++;

        sliderRGBText = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable("text.speed").getString() + " : "), Component.literal("%")
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

        sliderRGBText.visible = clock.getRgbModeText() == HUDMode.WAVE || clock.getRgbModeText() == HUDMode.CYCLE;

        i = 1;
        sliderRedBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, clock.getRedBackground(), 1, 1, true) {


            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindClockUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xff00ffff, col1 | 0x00ff0000, col1 & 0xff00ffff,
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

        sliderGreenBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, clock.getGreenBackground(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindClockUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffff00ff, col1 | 0x0000ff00, col1 & 0xffff00ff,
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

        sliderBlueBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.empty()
                , 0, 255, clock.getBlueBackground(), 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                int col1 = rgb | 0xff000000;
                RemindClockUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,col1 & 0xffffff00, col1 | 0x000000ff, col1 & 0xffffff00,
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



        sliderAlphaBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable(stateValues[i-1]).getString() + " : "), Component.literal("%")
                , 0, 100, (100 * clock.getAlphaBackground()) / 255.0, 1, 1, true) {

            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                final Minecraft mc = Minecraft.getInstance();
                guiGraphics.blitWithBorder(SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);

                int rgb = (sliderRedBackground.getValueInt() << 16 | sliderGreenBackground.getValueInt() << 8 | sliderBlueBackground.getValueInt());

                RenderSystem.setShaderColor(1,1,1,1);
                guiGraphics.blit(new ResourceLocation(RemindClockHUD.MODID, "textures/transparency.png"), getX() + 1, getY() + 1, 0, 0F, 0F, getWidth() - 2, getHeight() - 2,  10,10);

                RemindClockUtil.drawGradientRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0,rgb |  (0x46 << 24), rgb | (0xFF << 24), rgb | (0x46 << 24), rgb | (0xFF << 24));
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

        sliderRGBBackground = new ForgeSlider(0,0, 100, 20, Component.literal(Component.translatable("text.speed").getString() + " : "), Component.literal("%")
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

        sliderRGBBackground.visible = clock.getRgbModeBackground() == HUDMode.WAVE || clock.getRgbModeBackground() == HUDMode.CYCLE;


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

        InformationButton informationButton = new InformationButton(dateBox.getX() - 25, dateBox.getY(), 20,20, Component.empty(), p_93751_ -> {

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
        super.tick();
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float p_282465_) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, p_282465_);

        dateFormatted = clock.getDateFormatted();

        dateBox.render(graphics, mouseX, mouseY, p_282465_);

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 3, 16777215);


        int x = width / 2 - font.width(dateFormatted) / 2;
        int y = dateBox.getY() - 20;

        RemindClockUtil.renderClock(clock, graphics, font, x, y, width, height);
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        if(!dateBox.isMouseOver(p_94695_, p_94696_) && dateBox.isFocused()) {
            dateBox.setFocused(false);
        }
        return super.mouseClicked(p_94695_, p_94696_, p_94697_);
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
        RemindClockConfig.Client.Clock configClock = RemindClockConfig.CLIENT.clock;

        configClock.formatText.set(clock.getFormatText());
        configClock.drawBackground.set(clock.isDrawBackground());
        configClock.use12HourFormat.set(clock.isUse12HourFormat());

        configClock.rgbModeText.set(clock.getRgbModeText());
        configClock.rgbModeBackground.set(clock.getRgbModeBackground());

        configClock.redText.set(clock.getRedText());
        configClock.greenText.set(clock.getGreenText());
        configClock.blueText.set(clock.getBlueText());
        configClock.alphaText.set(clock.getAlphaText());
        configClock.rgbSpeedText.set(clock.getRgbSpeedText());

        configClock.redBackground.set(clock.getRedBackground());
        configClock.greenBackground.set(clock.getGreenBackground());
        configClock.blueBackground.set(clock.getBlueBackground());
        configClock.alphaBackground.set(clock.getAlphaBackground());
        configClock.rgbSpeedBackground.set(clock.getRgbSpeedBackground());

        configClock.textRightToLeftDirection.set(clock.isTextRightToLeftDirection());
        configClock.backgroundRightToLeftDirection.set(clock.isBackgroundRightToLeftDirection());
    }
}
