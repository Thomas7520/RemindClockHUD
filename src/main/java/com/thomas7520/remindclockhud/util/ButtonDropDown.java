package com.thomas7520.remindclockhud.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

// ButtonDropDown.java made by Thomas7520. Be careful, this one doesn't have scrollbar.
public class ButtonDropDown extends AbstractButton {
    protected static final ButtonDropDown.CreateNarration DEFAULT_NARRATION = Supplier::get;
    protected final ButtonDropDown.CreateNarration createNarration;
    private final List<Entry> entries;
    protected boolean focused;

    public static ButtonDropDown.Builder builder(Component pMessage) {
        return new ButtonDropDown.Builder(pMessage);
    }

    protected ButtonDropDown(int pX, int pY, int pWidth, int pHeight, Component pMessage, List<Entry> entries, ButtonDropDown.CreateNarration pCreateNarration) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.createNarration = pCreateNarration;
        this.entries = entries;
    }

    protected ButtonDropDown(ButtonDropDown.Builder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.entries, builder.createNarration);
        setTooltip(builder.tooltip); // Forge: Make use of the Builder tooltip
    }

    protected MutableComponent createNarrationMessage() {
        return this.createNarration.createNarrationMessage(super::createNarrationMessage);
    }

    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        if(isHovered) {
            focused = !focused;
            playDownSound(Minecraft.getInstance().getSoundManager());
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }

        boolean hovered = pMouseX >= getX() && pMouseX <= (getX() + getWidth()) && pMouseY >= getY() + getHeight() && pMouseY <= (getY() + getHeight() + 20 * entries.size());

        if(!hovered) {
            focused = false;
        } else if (focused) {
            int index = (int) Math.floor((pMouseY-getY()-getHeight()) / 20);
            entries.get(index).onPress();
            playDownSound(Minecraft.getInstance().getSoundManager());
            focused = !focused;
            return true;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void onPress() {

    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0,0, 10); // To be in foreground
        if(focused) {
            for (int i = 0; i < entries.size(); i++) {
                Entry entry = entries.get(i);

                pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
                RenderSystem.enableBlend();
                RenderSystem.enableDepthTest();

                boolean entryHovered = pMouseX >= getX() && pMouseX <= (getX() + getWidth()) && pMouseY >= getY() + getHeight() + 20 * i && pMouseY <= getY() + getHeight() + 20 * (i+1);

                pGuiGraphics.fill(getX(), getY() + getHeight() + 20 * i, getX() + getWidth(), getY() + getHeight() + 20 * (i+1), entryHovered ? Color.WHITE.getRGB() : -6250336);

                pGuiGraphics.fill(getX()+1, getY() + getHeight() + 20 * i+1, getX() + getWidth()-1, getY() + getHeight() + 20 * (i+1)-1, -16777216);
                renderScrollingString(pGuiGraphics, minecraft.font, Component.literal(entry.getName()), getX() + 2, getY() + getHeight() + 10 + 20 * i - 5, getX()+getWidth() - 2, getY() + getHeight()  + 10 + 20 * i + 3, Color.WHITE.getRGB());
            }
        }
        pGuiGraphics.pose().popPose();

        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return 46 - 20 + i * 20;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final Component message;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private ButtonDropDown.CreateNarration createNarration = ButtonDropDown.DEFAULT_NARRATION;

        private List<Entry> entries;

        public Builder(Component pMessage) {
            this.message = pMessage;
        }

        public ButtonDropDown.Builder pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public ButtonDropDown.Builder width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public ButtonDropDown.Builder size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public ButtonDropDown.Builder bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public ButtonDropDown.Builder tooltip(@Nullable Tooltip pTooltip) {
            this.tooltip = pTooltip;
            return this;
        }

        public ButtonDropDown.Builder createNarration(ButtonDropDown.CreateNarration pCreateNarration) {
            this.createNarration = pCreateNarration;
            return this;
        }

        public ButtonDropDown.Builder addEntries(List<Entry> entries) {
            this.entries = entries;
            return this;
        }

        public ButtonDropDown build() {
            return build(ButtonDropDown::new);
        }

        public ButtonDropDown build(java.util.function.Function<ButtonDropDown.Builder, ButtonDropDown> builder) {
            return builder.apply(this);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> pMessageSupplier);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnEntryPress {
        void onPress(Entry pEntry);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Entry {
        private final OnEntryPress onPress;
        private final String name;

        public Entry(String name, OnEntryPress entryPress) {
            this.name = name;
            this.onPress = entryPress;
        }
        public String getName() {
            return name;
        }
        public void onPress() {
            onPress.onPress(this);
        }
    }
}