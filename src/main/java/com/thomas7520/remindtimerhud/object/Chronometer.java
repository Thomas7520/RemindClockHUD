package com.thomas7520.remindtimerhud.object;


import java.util.ArrayList;
import java.util.List;

public class Chronometer {

    private long start;
    private boolean stop;
    private final List<Long> laps = new ArrayList<>();

    public Chronometer(long start, boolean stop) {
        this.start = start;
        this.stop = stop;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getStart() {
        return start;
    }

    public void addLap(long time) {
        laps.add(time);
    }

    public List<Long> getLaps() {
        return laps;
    }

    public void reset() {
        setStop(false);
        setStart(0);
        laps.clear();
    }
}
