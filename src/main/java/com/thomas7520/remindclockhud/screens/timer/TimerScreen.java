package com.thomas7520.remindclockhud.screens.timer;

import com.thomas7520.remindclockhud.util.RemindClockUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class TimerScreen extends Screen {

    private final Screen lastScreen;

    private EditBox[] editBoxes = new EditBox[3];
    public TimerScreen(Screen lastScreen) {
        super(Component.translatable("timer.title"));

        this.lastScreen = lastScreen;
    }

    @Override
    public void tick() {
        for (EditBox editBox : editBoxes) {
            editBox.tick();
        }
        super.tick();
    }

    @Override
    protected void init() {

        this.addRenderableWidget(Button.builder(Component.literal("timer.add"), p_93751_ -> {

        })
                .bounds(this.width / 2 - 155, this.height - 25, 150, 20).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, p_93751_ -> this.minecraft.setScreen(this.lastScreen))
                .bounds(this.width / 2 - 155 + 160, this.height - 25, 150, 20).build());


        this.addRenderableWidget(Button.builder(Component.literal("timer.launch"), p_93751_ -> {

                })
                .tooltip(Tooltip.create(Component.empty()))
                .bounds(this.width / 4 - 40, this.height / 2 + 65, 80, 20).build());

        this.addRenderableWidget(Button.builder(Component.empty(), p_93751_ -> {

                })
                .tooltip(Tooltip.create(Component.translatable("timer.start_pause")))
                .bounds(this.width / 4 + 10, this.height / 2 + 65, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.empty(), p_93751_ -> {

                })
                .tooltip(Tooltip.create(Component.translatable("timer.start_pause")))
                .bounds(this.width / 4 - 30, this.height / 2 + 65, 20, 20).build());


        for(int i = 0; i <3 ; i++) {
            int pX = this.width / 4 - 10 + 35 * (i - 1);
            int pY = this.height / 2 - 25 - 10;

            addRenderableWidget(editBoxes[i] = new EditBox(font, pX, pY, 20, 20, Component.empty()));

            editBoxes[i].setMaxLength(2);
            editBoxes[i].setFilter(s -> s.matches("^[0-9]*"));
            editBoxes[i].setCanLoseFocus(true);

            int maxValue = i == 0 ? 99 : 59;

            int finalI = i;
            this.addRenderableWidget(Button.builder(Component.literal("+"), p_93751_ -> {
                        if(editBoxes[finalI].getValue().isEmpty() || Integer.parseInt(editBoxes[finalI].getValue()) == maxValue) {
                            editBoxes[finalI].setValue("1");
                        } else {
                            editBoxes[finalI].setValue(String.valueOf(Integer.parseInt(editBoxes[finalI].getValue()) + 1));
                        }
                    })
                    .bounds(pX, pY - 25, 20, 20).build());

            this.addRenderableWidget(Button.builder(Component.literal("-"), p_93751_ -> {
                        if(editBoxes[finalI].getValue().isEmpty() || Integer.parseInt(editBoxes[finalI].getValue()) == 0) {
                            editBoxes[finalI].setValue(String.valueOf(maxValue));
                        } else {
                            editBoxes[finalI].setValue(String.valueOf(Integer.parseInt(editBoxes[finalI].getValue()) - 1));
                        }
                    })
                    .bounds(pX, pY + 25, 20, 20).build());
        }
        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderDirtBackground(pGuiGraphics);

        pGuiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 3, 16777215);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        Color color = new Color(143, 20, 215);
        Color color2 = new Color(200, 182, 224);

        RemindClockUtil.circleDoubleProgress(width / 4d, height / 2d - 25, 78, 77, 360, 0, 100, color2.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());
        RemindClockUtil.circleDoubleProgress(width / 4d, height / 2d - 25, 80, 75, 180, 180, 100, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());


        RemindClockUtil.circleProgress(width / 4d - 17.5, height /2d - 25 - 5, 1.8f, 360, 0, 30, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());
        RemindClockUtil.circleProgress(width / 4d - 17.5, height /2d - 25 + 5, 1.8f, 360, 0, 30, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());

        RemindClockUtil.circleProgress(width / 4d + 17.5, height /2d - 25 - 5, 1.8f, 360, 0, 30, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());
        RemindClockUtil.circleProgress(width / 4d + 17.5, height /2d - 25 + 5, 1.8f, 360, 0, 30, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());
    }

    @Override
    public void onClose() {
        minecraft.setScreen(lastScreen);
    }
}
