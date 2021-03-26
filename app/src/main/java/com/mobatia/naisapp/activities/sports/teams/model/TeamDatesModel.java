package com.mobatia.naisapp.activities.sports.teams.model;

import java.io.Serializable;

public class TeamDatesModel implements Serializable {
    String day;
    String date;
    String time;
    String venue;
    String yearDate;
    String dayStringDate;
    String dayDate;
    String id;
    String monthDate;
    String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getYearDate() {
        return yearDate;
    }

    public void setYearDate(String yearDate) {
        this.yearDate = yearDate;
    }

    public String getDayStringDate() {
        return dayStringDate;
    }

    public void setDayStringDate(String dayStringDate) {
        this.dayStringDate = dayStringDate;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    String start_time;
    String end_time;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
