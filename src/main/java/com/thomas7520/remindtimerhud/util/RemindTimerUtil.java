package com.thomas7520.remindtimerhud.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.thomas7520.remindtimerhud.object.Alarm;
import com.thomas7520.remindtimerhud.object.Chronometer;
import com.thomas7520.remindtimerhud.object.Remind;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RemindTimerUtil {
    public static KeyMapping guiBind;
    public static KeyMapping switchChronometerBind;
    public static KeyMapping addLapsBind;
    public static KeyMapping resetChronometerBind;
    public static KeyMapping stopAlarm;

    private static final Chronometer chronometer = new Chronometer(0, true);
    private static final List<Remind> reminds = new ArrayList<>();
    private static final HashMap<UUID, Alarm> alarms = new HashMap<>();
    private static final HashMap<UUID, Alarm> serverAlarms = new HashMap<>();

    public static Chronometer getChronometer() {
        return chronometer;
    }

    public static List<Remind> getReminds() {
        return reminds;
    }

    public static HashMap<UUID, Alarm> getAlarms() {
        return alarms;
    }

    public static HashMap<UUID, Alarm> getServerAlarms() {
        return serverAlarms;
    }


    public static void circleDoubleProgress(
            int x, int y,
            float radius, float radiusDouble,
            float progress, float initialDegree, int segment, Color color) {

        float a = 360F / segment;
        float b;

        double degree, sin, cos;
        double osin = 0;
        double ocos = 0;

        Tesselator tessellator = Tesselator.getInstance();
        tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        for (int i = 0; i <= segment; ++i) {

            b = a * i;
            if (b < progress)
                degree = (initialDegree + b) * Math.PI / 180D;
            else
                degree = (initialDegree + progress) * Math.PI / 180D;

            sin = Math.sin(-degree);
            cos = Math.cos(-degree);


            tessellator.getBuilder()
                    .vertex((float) (x + (osin * radius)), (float) (y + (ocos * radius)), 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();

            tessellator.getBuilder()
                    .vertex((float) ((float)x + (osin * radiusDouble)), (float) ((float)y + (ocos * radiusDouble)), 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();

            tessellator.getBuilder()
                    .vertex((float) ((float)x + (sin * radiusDouble)), (float) ((float)y + (cos * radiusDouble)), 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();

            tessellator.getBuilder()
                    .vertex((float) ((float)x + (sin * radius)), (float) ((float)y + (cos * radius)), 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();


            osin = sin;
            ocos = cos;

            if (b > progress) break;
        }
        tessellator.end();
        RenderSystem.disableBlend();
    }
}
