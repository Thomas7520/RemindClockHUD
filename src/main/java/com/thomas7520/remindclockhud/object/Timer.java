package com.thomas7520.remindclockhud.object;

import com.thomas7520.remindclockhud.util.ChronometerFormat;

public class Timer {

    private String pauseTimeCache;
    private boolean enable, paused, started;

    private long startTime, endTime, pauseTime;


    public String getPauseTimeCache() {
        return pauseTimeCache;
    }

    public void setPauseTimeCache(String pauseTimeCache) {
        this.pauseTimeCache = pauseTimeCache;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {

        if(!this.started || !this.enable) return;

        if(!this.paused && paused) {
            pauseTimeCache = ChronometerFormat.HH_MN_SS.formatTime(endTime);
            pauseTime = System.currentTimeMillis();
        }

        if(this.paused && !paused) {
            pauseTimeCache = "";
            long timeElapsed = System.currentTimeMillis() - pauseTime;
            setStartTime(startTime + timeElapsed);
            setEndTime(getEndTime() + timeElapsed);
            pauseTime = 0;
        }
        this.paused = paused;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
