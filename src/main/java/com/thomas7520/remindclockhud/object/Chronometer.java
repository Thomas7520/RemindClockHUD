package com.thomas7520.remindclockhud.object;


import com.thomas7520.remindclockhud.util.ChronometerFormat;
import com.thomas7520.remindclockhud.util.HUDMode;

import java.util.ArrayList;
import java.util.List;

public class Chronometer {

    private ChronometerFormat format;
    private String pauseTimeCache;
    private boolean drawBackground;

    private HUDMode rgbModeText, rgbModeBackground;
    private int redText, greenText, blueText, alphaText, rgbSpeedText;
    private int redBackground, greenBackground, blueBackground, alphaBackground, rgbSpeedBackground;
    private boolean textRightToLeftDirection, backgroundRightToLeftDirection;
    private double posX, posY;
    private long startTime, pauseTime;
    private boolean enable, paused, started, idleRender;
    private final List<Long> laps = new ArrayList<>();

    public Chronometer(boolean enable, ChronometerFormat format, boolean drawBackground, HUDMode rgbModeText, HUDMode rgbModeBackground, int redText, int greenText, int blueText, int alphaText, int rgbSpeedText, int redBackground, int greenBackground, int blueBackground, int alphaBackground, int rgbSpeedBackground, boolean textRightToLeftDirection, boolean backgroundRightToLeftDirection, boolean idleRender, double posX, double posY) {
        this.enable = enable;
        this.format = format;
        this.drawBackground = drawBackground;
        this.rgbModeText = rgbModeText;
        this.rgbModeBackground = rgbModeBackground;
        this.redText = redText;
        this.greenText = greenText;
        this.blueText = blueText;
        this.alphaText = alphaText;
        this.rgbSpeedText = rgbSpeedText;
        this.redBackground = redBackground;
        this.greenBackground = greenBackground;
        this.blueBackground = blueBackground;
        this.alphaBackground = alphaBackground;
        this.rgbSpeedBackground = rgbSpeedBackground;
        this.textRightToLeftDirection = textRightToLeftDirection;
        this.backgroundRightToLeftDirection = backgroundRightToLeftDirection;
        this.idleRender = idleRender;
        this.posX = posX;
        this.posY = posY;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setStarted(boolean started) {
        if(!this.enable) return;

        this.started = started;
        setStartTime(System.currentTimeMillis());
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean stop) {
        if(!this.started || !this.enable) return;

        if(!this.paused && stop) {
            pauseTimeCache = format.formatTime(startTime);
            pauseTime = System.currentTimeMillis();
        }

        if(this.paused && !stop) {
            pauseTimeCache = "";
            setStartTime(startTime + (System.currentTimeMillis() - pauseTime));
            pauseTime = 0;
        }
        this.paused = stop;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void addLap(long time) {
        laps.add(time);
    }

    public List<Long> getLaps() {
        return laps;
    }

    public ChronometerFormat getFormat() {
        return format;
    }

    public void setFormat(ChronometerFormat format) {
        this.format = format;
    }

    public boolean isDrawBackground() {
        return drawBackground;
    }

    public void setDrawBackground(boolean drawBackground) {
        this.drawBackground = drawBackground;
    }

    public HUDMode getRgbModeText() {
        return rgbModeText;
    }

    public void setRgbModeText(HUDMode rgbModeText) {
        this.rgbModeText = rgbModeText;
    }

    public HUDMode getRgbModeBackground() {
        return rgbModeBackground;
    }

    public void setRgbModeBackground(HUDMode rgbModeBackground) {
        this.rgbModeBackground = rgbModeBackground;
    }

    public int getRedText() {
        return redText;
    }

    public void setRedText(int redText) {
        this.redText = redText;
    }

    public int getGreenText() {
        return greenText;
    }

    public void setGreenText(int greenText) {
        this.greenText = greenText;
    }

    public int getBlueText() {
        return blueText;
    }

    public void setBlueText(int blueText) {
        this.blueText = blueText;
    }

    public int getAlphaText() {
        return alphaText;
    }

    public void setAlphaText(int alphaText) {
        this.alphaText = alphaText;
    }

    public int getRgbSpeedText() {
        return rgbSpeedText;
    }

    public void setRgbSpeedText(int rgbSpeedText) {
        this.rgbSpeedText = rgbSpeedText;
    }

    public int getRedBackground() {
        return redBackground;
    }

    public void setRedBackground(int redBackground) {
        this.redBackground = redBackground;
    }

    public int getGreenBackground() {
        return greenBackground;
    }

    public void setGreenBackground(int greenBackground) {
        this.greenBackground = greenBackground;
    }

    public int getBlueBackground() {
        return blueBackground;
    }

    public void setBlueBackground(int blueBackground) {
        this.blueBackground = blueBackground;
    }

    public int getAlphaBackground() {
        return alphaBackground;
    }

    public void setAlphaBackground(int alphaBackground) {
        this.alphaBackground = alphaBackground;
    }

    public int getRgbSpeedBackground() {
        return rgbSpeedBackground;
    }

    public void setRgbSpeedBackground(int rgbSpeedBackground) {
        this.rgbSpeedBackground = rgbSpeedBackground;
    }

    public boolean isTextRightToLeftDirection() {
        return textRightToLeftDirection;
    }

    public void setTextRightToLeftDirection(boolean textRightToLeftDirection) {
        this.textRightToLeftDirection = textRightToLeftDirection;
    }

    public boolean isBackgroundRightToLeftDirection() {
        return backgroundRightToLeftDirection;
    }

    public void setBackgroundRightToLeftDirection(boolean backgroundRightToLeftDirection) {
        this.backgroundRightToLeftDirection = backgroundRightToLeftDirection;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public String getPauseTimeCache() {
        return pauseTimeCache;
    }

    public boolean isIdleRender() {
        return idleRender;
    }

    public void setIdleRender(boolean idleRender) {
        this.idleRender = idleRender;
    }

    public void reset() {
        this.paused = false;
        this.started = false;
        this.startTime = 0;

        laps.clear();
    }

    @Override
    public String toString() {
        return "Chronometer{" +
                "format=" + format +
                ", pauseTimeCache='" + pauseTimeCache + '\'' +
                ", drawBackground=" + drawBackground +
                ", rgbModeText=" + rgbModeText +
                ", rgbModeBackground=" + rgbModeBackground +
                ", redText=" + redText +
                ", greenText=" + greenText +
                ", blueText=" + blueText +
                ", alphaText=" + alphaText +
                ", rgbSpeedText=" + rgbSpeedText +
                ", redBackground=" + redBackground +
                ", greenBackground=" + greenBackground +
                ", blueBackground=" + blueBackground +
                ", alphaBackground=" + alphaBackground +
                ", rgbSpeedBackground=" + rgbSpeedBackground +
                ", textRightToLeftDirection=" + textRightToLeftDirection +
                ", backgroundRightToLeftDirection=" + backgroundRightToLeftDirection +
                ", posX=" + posX +
                ", posY=" + posY +
                ", startTime=" + startTime +
                ", pauseTime=" + pauseTime +
                ", enable=" + enable +
                ", paused=" + paused +
                ", started=" + started +
                ", idleRender=" + idleRender +
                ", laps=" + laps +
                '}';
    }
}
