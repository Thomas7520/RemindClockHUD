package com.thomas7520.remindclockhud.object;

public class Remind {

    private String name;
    private long time;

    private boolean ringing;

    private boolean paused;


    public Remind(String name, long time, boolean ringing, boolean paused) {
        this.name = name;
        this.time = time;
        this.ringing = ringing;
        this.paused = paused;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRinging() {
        return ringing;
    }

    public void setRinging(boolean ringing) {
        this.ringing = ringing;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public String getTimeFormatted() {
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;
        long seconds = time % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
