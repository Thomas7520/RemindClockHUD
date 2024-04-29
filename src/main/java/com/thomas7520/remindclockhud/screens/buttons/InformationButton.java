package com.thomas7520.remindclockhud.screens.buttons;

import com.thomas7520.remindclockhud.RemindClockHUD;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class InformationButton extends Button {

    public InformationButton(int p_259075_, int p_259271_, int p_260232_, int p_260028_, Component p_259351_, OnPress p_260152_, CreateNarration p_259552_) {
        super(p_259075_, p_259271_, p_260232_, p_260028_, p_259351_, p_260152_, p_259552_);
    }

    @Override
    public void render(GuiGraphics p_282421_, int p_93658_, int p_93659_, float p_93660_) {
        super.render(p_282421_, p_93658_, p_93659_, p_93660_);

        p_282421_.blit(new ResourceLocation(RemindClockHUD.MODID, "textures/interrogation.png"), getX() + 2, getY() + 1, 0, 0F, 0F, getWidth() - 4,  getHeight() - 4,  getWidth() - 4, getHeight() - 4);
    }
}
