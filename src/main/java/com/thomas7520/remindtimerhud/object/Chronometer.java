package com.thomas7520.remindtimerhud.object;


import com.thomas7520.remindtimerhud.util.ChronometerFormat;
import com.thomas7520.remindtimerhud.util.HUDMode;

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
    private boolean stop;
    private final List<Long> laps = new ArrayList<>();

    public Chronometer(ChronometerFormat format, boolean drawBackground, HUDMode rgbModeText, HUDMode rgbModeBackground, int redText, int greenText, int blueText, int alphaText, int rgbSpeedText, int redBackground, int greenBackground, int blueBackground, int alphaBackground, int rgbSpeedBackground, boolean textRightToLeftDirection, boolean backgroundRightToLeftDirection, double posX, double posY, long startTime, boolean stop) {
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
        this.posX = posX;
        this.posY = posY;
        this.startTime = startTime;
        this.stop = stop;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        if(!this.stop && stop) {
            pauseTimeCache = format.formatTime(System.currentTimeMillis() - startTime);
            pauseTime = System.currentTimeMillis();
        }

        if(this.stop && !stop) {
            pauseTimeCache = "";
            setStartTime(startTime + (System.currentTimeMillis() - pauseTime));
            pauseTime = 0;
        }
        this.stop = stop;
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

    public void reset() {
        setStop(false);
        setStartTime(0);
        laps.clear();
    }

    @Override
    public String toString() {
        return "Chronometer{" +
                "formatText='" + format + '\'' +
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
                ", start=" + startTime +
                ", stop=" + stop +
                ", laps=" + laps +
                '}';
    }
}
