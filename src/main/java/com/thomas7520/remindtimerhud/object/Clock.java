package com.thomas7520.remindtimerhud.object;

import com.thomas7520.remindtimerhud.util.HUDMode;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Clock {

    private String formatText;
    private boolean drawText, drawBackground;
    private boolean use12HourFormat;
    private HUDMode rgbModeText, rgbModeBackground;
    private int redText, greenText, blueText, alphaText, rgbSpeedText;
    private int redBackground, greenBackground, blueBackground, alphaBackground, rgbSpeedBackground;
    private boolean textRightToLeftDirection, backgroundRightToLeftDirection;

    private double posX;
    private double posY;

    public Clock(String formatText, boolean drawText, boolean drawBackground, boolean use12HourFormat, HUDMode rgbModeText, HUDMode rgbModeBackground, int redText, int greenText, int blueText, int alphaText, int rgbSpeedText, int redBackground, int greenBackground, int blueBackground, int alphaBackground, int rgbSpeedBackground, boolean textRightToLeftDirection, boolean backgroundRightToLeftDirection, double posX, double posY) {
        this.formatText = formatText;
        this.drawText = drawText;
        this.drawBackground = drawBackground;
        this.use12HourFormat = use12HourFormat;
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
    }



    public String getFormatText() {
        return formatText;
    }

    public void setFormatText(String formatText) {
        this.formatText = formatText;
    }

    public boolean isDrawText() {
        return drawText;
    }

    public void setDrawText(boolean drawText) {
        this.drawText = drawText;
    }

    public boolean isDrawBackground() {
        return drawBackground;
    }

    public void setDrawBackground(boolean drawBackground) {
        this.drawBackground = drawBackground;
    }

    public boolean isUse12HourFormat() {
        return use12HourFormat;
    }

    public void setUse12HourFormat(boolean use12HourFormat) {
        this.use12HourFormat = use12HourFormat;
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

    @Override
    public String toString() {
        return "Clock{" +
                "formatText='" + formatText + '\'' +
                ", drawText=" + drawText +
                ", drawBackground=" + drawBackground +
                ", use12HourFormat=" + use12HourFormat +
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
                '}';
    }

    public String getDateFormatted() {

        String date;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter;

        if (use12HourFormat) {
            timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        } else {
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        }

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault()); // Format pour le nom du jour

        DateTimeFormatter dayShortFormatter = DateTimeFormatter.ofPattern("E", Locale.getDefault()); // Format pour l'abréviation du jour

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault()); // Format pour le mois
        DateTimeFormatter shortMonthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault()); // Format pour le mois

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        date = formatText
                .replace("%hh", timeFormatter.format(now).substring(0, 2))
                .replace("%mm", timeFormatter.format(now).substring(3, 5))
                .replace("%ss", timeFormatter.format(now).substring(6, 8))
                .replace("%dd", dateFormatter.format(now).substring(0, 2))
                .replace("%day", StringUtils.capitalize(dayFormatter.format(now)))
                .replace("%sday", StringUtils.capitalize(dayShortFormatter.format(now)))
                .replace("%month", StringUtils.capitalize(monthFormatter.format(now)))
                .replace("%smonth", StringUtils.capitalize(shortMonthFormatter.format(now)))
                .replace("%MM", dateFormatter.format(now).substring(3, 5))
                .replace("%yyyy", String.valueOf(now.getYear()))
                .replace("%yy", String.valueOf(now.getYear() % 100));

        if(use12HourFormat) date = date.replace("%state", timeFormatter.format(now).substring(9, 11));

        return date;
    }
}
