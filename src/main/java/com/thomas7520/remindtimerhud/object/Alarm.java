package com.thomas7520.remindtimerhud.object;

public class Alarm {

    private String name;
    private boolean repeat;

    private boolean ringing;

    private int[] days;

    private int hour;

    private int minutes;

    
    public Alarm(String name, boolean repeat, boolean ringing, int[] days, int hour, int minutes) {
        this.name = name;
        this.repeat = repeat;
        this.ringing = ringing;
        this.days = days;
        this.hour = hour;
        this.minutes = minutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isRinging() {
        return ringing;
    }

    public void setRinging(boolean ringing) {
        this.ringing = ringing;
    }

    public int[] getDays() {
        return days;
    }

    public void setDays(int[] days) {
        this.days = days;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
