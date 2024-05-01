package com.thomas7520.remindclockhud.screens.timer;

import com.thomas7520.remindclockhud.RemindClockHUD;
import com.thomas7520.remindclockhud.object.Timer;
import com.thomas7520.remindclockhud.util.ChronometerFormat;
import com.thomas7520.remindclockhud.util.RemindClockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerScreen extends Screen {

    private final Screen lastScreen;
    private final Timer timer;

    private EditBox[] editBoxes = new EditBox[3];
    private Button[] addButtons = new Button[3];
    private Button[] minusButtons = new Button[3];
    private Button buttonStop;
    private Button buttonPause;
    private Button buttonLaunch;
    private float percentage;

    public TimerScreen(Screen lastScreen) {
        super(Component.translatable("timer.title"));

        this.lastScreen = lastScreen;
        this.timer = RemindClockHUD.getTimer();
        timer.setEnable(true);
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
                .bounds(this.width / 2 - 155 + 160, this.height - 25, 150, 20)
                .build());



        for(int i = 0; i < 3; i++) {
            int pX = this.width / 4 - 10 + 35 * (i - 1);
            int pY = this.height / 2 - 25 - 10;

            addRenderableWidget(editBoxes[i] = new EditBox(font, pX, pY, 20, 20, Component.empty()));

            int finalI = i;

            editBoxes[i].setMaxLength(2);
            editBoxes[i].setFilter(s -> s.matches(finalI == 0 ? "^([0-9]?[0-9])?$" : "^([0-5]?[0-9])?$"));
            editBoxes[i].setCanLoseFocus(true);
            editBoxes[i].setHint(Component.literal("00"));
            editBoxes[i].setVisible(!timer.isStarted());

            int maxValue = i == 0 ? 99 : 59;

            addButtons[i] = this.addRenderableWidget(Button.builder(Component.literal("+"), p_93751_ -> {
                        if(editBoxes[finalI].getValue().isEmpty()) {
                            editBoxes[finalI].setValue("1");
                        } else if(Integer.parseInt(editBoxes[finalI].getValue()) == maxValue) {
                            editBoxes[finalI].setValue("");
                        } else {
                            editBoxes[finalI].setValue(String.valueOf(Integer.parseInt(editBoxes[finalI].getValue()) + 1));
                        }
                    })
                    .bounds(pX, pY - 25, 20, 20)
                    .build());

            addButtons[i].visible = !timer.isStarted();

            minusButtons[i] = this.addRenderableWidget(Button.builder(Component.literal("-"), p_93751_ -> {
                        if(editBoxes[finalI].getValue().isEmpty()) {
                            editBoxes[finalI].setValue(String.valueOf(maxValue));
                        } else if(Integer.parseInt(editBoxes[finalI].getValue()) == 1){
                            editBoxes[finalI].setValue("");
                        } else {
                            editBoxes[finalI].setValue(String.valueOf(Integer.parseInt(editBoxes[finalI].getValue()) - 1));
                        }
                    })
                    .bounds(pX, pY + 25, 20, 20)
                    .build());
            minusButtons[i].visible = !timer.isStarted();
        }


        buttonLaunch = this.addRenderableWidget(Button.builder(Component.literal("timer.launch"), p_93751_ -> {
                    int hours = editBoxes[0].getValue().isEmpty() ? 0 : Integer.parseInt(editBoxes[0].getValue());
                    int minutes = editBoxes[1].getValue().isEmpty() ? 0 : Integer.parseInt(editBoxes[1].getValue());
                    int seconds = editBoxes[2].getValue().isEmpty() ? 0 : Integer.parseInt(editBoxes[2].getValue());

                    timer.setEndTime(System.currentTimeMillis() + hours * 3600 * 1000L + minutes * 60 * 1000L + seconds * 1000L);
                    timer.setStartTime(System.currentTimeMillis());
                    timer.setStarted(true);

                    buttonStop.visible = true;
                    buttonPause.visible = true;
                    p_93751_.visible = false;

                    for (int i = 0; i < 3; i++) {
                        editBoxes[i].setVisible(false);
                        minusButtons[i].visible = false;
                        addButtons[i].visible = false;
                    }
                })
                .tooltip(Tooltip.create(Component.empty()))
                .bounds(this.width / 4 - 40, this.height / 2 + 65, 80, 20)
                .build());

        buttonLaunch.visible = !timer.isStarted();

        buttonStop = this.addRenderableWidget(Button.builder(Component.empty(), p_93751_ -> {
                    timer.setStarted(false);

                    p_93751_.visible = false;
                    buttonPause.visible = false;
                    buttonLaunch.visible = true;

                    for (int i = 0; i < 3; i++) {
                        editBoxes[i].setVisible(true);
                        minusButtons[i].visible = true;
                        addButtons[i].visible = true;
                    }
                })
                .tooltip(Tooltip.create(Component.translatable("timer.stop")))
                .bounds(this.width / 4 + 10, this.height / 2 + 65, 20, 20)
                .build());

        buttonStop.visible = timer.isStarted();

        buttonPause = this.addRenderableWidget(Button.builder(Component.empty(), p_93751_ -> {
                    System.out.println("tt" + timer.isPaused());
                    timer.setPaused(!timer.isPaused());
                })
                .tooltip(Tooltip.create(Component.translatable("timer.start_pause")))
                .bounds(this.width / 4 - 30, this.height / 2 + 65, 20, 20)
                .build());

        buttonPause.visible = timer.isStarted();

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderDirtBackground(pGuiGraphics);

        pGuiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 3, 16777215);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        Color color = new Color(143, 20, 215);
        Color color2 = new Color(200, 182, 224);

        if(timer.isStarted() && !timer.isPaused()) {

            long currentTime = System.currentTimeMillis();

            long startTime = timer.getStartTime();
            long endTime = timer.getEndTime();

            long totalDuration = endTime - startTime;
            long elapsedDuration = currentTime - startTime;

            percentage = (((float) (totalDuration - elapsedDuration) / totalDuration) * 360);
        }

        RemindClockUtil.circleDoubleProgress(width / 4d, height / 2d - 25, 78, 77, 360, 0, 100, color2.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());
        RemindClockUtil.circleDoubleProgress(width / 4d, height / 2d - 25, 80, 75, timer.isStarted() ? percentage : 360, 180, 100, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());


        if(!timer.isStarted()) {
            RemindClockUtil.circleProgress(width / 4d - 17.5, height / 2d - 25 - 5, 1.8f, 360, 0, 30, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());
            RemindClockUtil.circleProgress(width / 4d - 17.5, height / 2d - 25 + 5, 1.8f, 360, 0, 30, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());

            RemindClockUtil.circleProgress(width / 4d + 17.5, height / 2d - 25 - 5, 1.8f, 360, 0, 30, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());
            RemindClockUtil.circleProgress(width / 4d + 17.5, height / 2d - 25 + 5, 1.8f, 360, 0, 30, color.getRGB(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), RenderType.gui());
        } else if(System.currentTimeMillis() < timer.getEndTime() || timer.isPaused()) {
            String timeLeft = ChronometerFormat.HH_MN_SS.formatTime(timer.getEndTime());

            if(timer.isPaused()) {
                timeLeft = timer.getPauseTimeCache();
            }

            double timerX = width / 4d - font.width(timeLeft) * 2 / 2d;
            double timerY = height / 2d - 30;

            pGuiGraphics.pose().translate(timerX, timerY, 0);

            pGuiGraphics.pose().scale(2,2,2);

            pGuiGraphics.drawString(font, Component.literal(timeLeft), 0,0, Color.WHITE.getRGB(), false);
            pGuiGraphics.pose().scale(0.5f,0.5f,0.5f);
            pGuiGraphics.pose().translate(-timerX, -timerY, 0);

            Date endDate = new Date(timer.getEndTime());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            String endString = sdf.format(endDate);

            double dateEndX = width / 4d - font.width(endString) / 2d;
            double dateEndY = height / 2d - 15;

            pGuiGraphics.pose().translate(dateEndX, dateEndY, 0);
            pGuiGraphics.drawString(font, Component.literal(endString), 0,40, timer.isPaused() ? Color.GRAY.getRGB() : Color.WHITE.getRGB(), false);
            pGuiGraphics.pose().translate(-dateEndX, -dateEndY, 0);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        for (EditBox editBox : editBoxes) {
            if(!editBox.isMouseOver(pMouseX, pMouseY) && editBox.isFocused()) {
                editBox.setFocused(false);
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(lastScreen);
    }
}
