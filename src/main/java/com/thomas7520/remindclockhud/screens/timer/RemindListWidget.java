package com.thomas7520.remindclockhud.screens.timer;

import com.thomas7520.remindclockhud.object.Remind;
import com.thomas7520.remindclockhud.util.RemindClockUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class RemindListWidget extends ObjectSelectionList<RemindListWidget.RemindEntry> {

    public static final int BACKGROUND_COLOR = new Color(0, 0, 0, 95).getRGB();
    private final int listWidth;
    private TimerScreen parent;

    public RemindListWidget(TimerScreen parent, int listWidth, int top, int bottom) {
        super(parent.getMinecraft(), listWidth - 10, listWidth, top, bottom, parent.getMinecraft().font.lineHeight * 2 + 8);
        this.parent = parent;
        this.listWidth = listWidth;
        this.setRenderBackground(false);
        this.setRenderTopAndBottom(false);
        this.refreshList();
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getRight() - 5;
    }

    @Override
    protected void updateScrollingState(double pMouseX, double pMouseY, int pButton) {
        super.updateScrollingState(pMouseX, pMouseY, pButton);
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();

        parent.buildRemindList(this::addEntry, RemindEntry::new);
        setScrollAmount(getScrollAmount());
    }

    @Override
    protected void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fill(RenderType.guiOverlay(), this.x0, this.y0, this.x1, this.y1, 0, BACKGROUND_COLOR);
    }

    public class RemindEntry extends ObjectSelectionList.Entry<RemindEntry> {
        private final Remind remind;

        private final Button removeButton;

        RemindEntry(Remind remind) {
            this.remind = remind;

            this.removeButton = Button.builder(Component.literal("X"), pButton -> {
                        RemindClockUtil.getReminds().remove(remind);
                        refreshList();
            }).size(20,20)
                    .build();
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", remind.getName());
        }

        @Override
        public void render(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            guiGraphics.drawString(parent.getMinecraft().font, remind.getName(), left + 10, top + 7, 0xFFFFFF, false);

            this.removeButton.setX(left + entryWidth - 40);
            this.removeButton.setY(top);
            this.removeButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

            return removeButton.mouseClicked(pMouseX, pMouseY, pButton) || super.mouseClicked(pMouseX, pMouseY, pButton);
        }
    }
}