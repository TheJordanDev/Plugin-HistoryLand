package fr.thejordan.historyland.object.common;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class Time {

    @Getter
    public Integer year = 0;
    @Getter
    public Integer mounth = 0;
    @Getter
    public Integer day = 0;
    @Getter
    public Integer hour = 0;
    @Getter
    public Integer minutes = 0;
    @Getter
    public Integer seconds = 0;

    public Time() {
    }

    public Time setYear(Integer num) {
        year = num;
        return this;
    }

    public Time setMounth(Integer num) {
        mounth = num;
        return this;
    }

    public Time setDay(Integer num) {
        day = num;
        return this;
    }

    public Time setHour(Integer num) {
        hour = num;
        return this;
    }

    public Time setMinutes(Integer num) {
        minutes = num;
        return this;
    }

    public Time setSeconds(Integer num) {
        seconds = num;
        return this;
    }

    public static Time getNow() {
        Time time = new Time();
        LocalDateTime ltime = LocalDateTime.now();
        ltime.atZone(ZoneId.of("Europe/Paris"));
        time.setDay(ltime.getDayOfMonth());
        time.setMounth(ltime.getMonthValue());
        time.setYear(ltime.getYear());
        time.setHour(ltime.getHour());
        time.setMinutes(ltime.getMinute());
        time.setSeconds(ltime.getSecond());
        return time;
    }

    public long toTicks() {
        long ret = 18000;
        ret += (getHour()) * 1000;
        ret += (getMinutes() / 60.0) * 1000;
        ret %= 24000;
        return ret;
    }

    public static long convertTimeToTicks(Time time) {
        long ret = 18000;
        ret += (time.getHour()) * 1000;
        ret += (time.getMinutes() / 60.0) * 1000;
        ret %= 24000;
        return ret;
    }

    public static Time convertTickToTime(long tick) {
        long seconds = tick / 20L;
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
        Time time = new Time();
        time.setHour(timeOfDay.getHour());
        time.setMinutes(timeOfDay.getMinute());
        time.setSeconds(timeOfDay.getSecond());
        return time;
    }

    public static String repairTime(Integer value) {
        if (value < 10) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }

    public static Time localDateTimeToTime(LocalTime timeOfDay) {
        Time time = new Time();
        time.setHour(timeOfDay.getHour());
        time.setMinutes(timeOfDay.getMinute());
        time.setSeconds(timeOfDay.getSecond());
        return time;
    }

    public void addASecond() {
        if (seconds + 1 > 60) {
            seconds = 0;
            if (minutes + 1 > 60) {
                minutes = 0;
                if (hour + 1 > 23) {
                    hour = 0;
                } else {
                    hour++;
                }
            } else {
                minutes++;
            }
        } else {
            seconds++;
        }
    }

    public String dateString() {
        return repairTime(day) + "/" + repairTime(mounth) + "/" + repairTime(year) + " "
                + repairTime(hour) + ":" + repairTime(minutes) + ":" + repairTime(seconds);
    }

    public String dayString() {
        return repairTime(day) + "/" + repairTime(mounth) + "/" + repairTime(year);
    }

    public String timeString() {
        return repairTime(hour) + ":" + repairTime(minutes) + ":" + repairTime(seconds);
    }
}
