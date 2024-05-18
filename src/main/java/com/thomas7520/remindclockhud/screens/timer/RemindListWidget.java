package com.thomas7520.remindclockhud.screens.timer;

import com.thomas7520.remindclockhud.object.Remind;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ModListWidget;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.lang.reflect.Field;

public class RemindListWidget extends ObjectSelectionList<RemindListWidget.RemindEntry> {
    private static String stripControlCodes(String value) {
        return net.minecraft.util.StringUtil.stripColor(value);
    }

    private final int listWidth;

    private TimerScreen parent;

    public RemindListWidget(TimerScreen parent, int listWidth, int top, int bottom) {
        super(parent.getMinecraft(), listWidth - 10, listWidth, top, bottom, parent.getMinecraft().font.lineHeight * 2 + 8);
        this.parent = parent;
        this.listWidth = listWidth;
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
    }

    @Override
    protected void renderBackground(GuiGraphics guiGraphics) {
        //this.parent.renderBackground(guiGraphics);
    }

    public class RemindEntry extends ObjectSelectionList.Entry<RemindEntry> {
        private final Remind remind;

        RemindEntry(Remind remind) {
            this.remind = remind;
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", remind.getName());
        }

        @Override
        public void render(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            guiGraphics.drawString(parent.getMinecraft().font, remind.getName(), left + 3, top + 2, 0xFFFFFF, false);
        }
    }
}