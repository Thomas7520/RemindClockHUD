package com.thomas7520.remindtimerhud.util;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.TimeZone;

public enum ChronometerFormat {

    HH_MM_SS("HH:mm:ss"),
    MM_SS("mm:ss"),
    SS_MS("ss.SSS"),
    DD_HH_MM_SS("dd:HH:mm:ss"),
    HH_MM_SS_MS("HH:mm:ss.SSS");

    private final String format;

    ChronometerFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public String formatTime(long milliseconds) {
        return DurationFormatUtils.formatDuration(milliseconds, format, true);
    }
}
