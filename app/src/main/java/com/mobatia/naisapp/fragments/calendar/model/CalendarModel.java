package com.mobatia.naisapp.fragments.calendar.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 15/3/17.
 */
public class CalendarModel implements Serializable {

    String dateNTime;
    String event;
    String fromTime;
    String startTime;
    String endTime;
    String monthDate;
    String isAllDay;
    Boolean eventAddToCalendar;

    public Boolean isEventAddToCalendar() {
        return eventAddToCalendar;
    }

    public void setEventAddToCalendar(boolean eventAddToCalendar) {
        this.eventAddToCalendar = eventAddToCalendar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public String getDayStringDate() {
        return dayStringDate;
    }

    public void setDayStringDate(String dayStringDate) {
        this.dayStringDate = dayStringDate;
    }

    public String getYearDate() {
        return yearDate;
    }

    public void setYearDate(String yearDate) {
        this.yearDate = yearDate;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    String yearDate;
    String dayStringDate;
    String dayDate;
    String vpml;

    public String getVpml() {
        return vpml;
    }

    public void setVpml(String vpml) {
        this.vpml = vpml;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    String toTime;
    String id;

    public ArrayList<CalendarModel> getEventModels() {
        return eventModels;
    }

    public void setEventModels(ArrayList<CalendarModel> eventModels) {
        this.eventModels = eventModels;
    }

    ArrayList<CalendarModel> eventModels;

    public String getDateNTime() {
        return dateNTime;
    }

    public void setDateNTime(String dateNTime) {
        this.dateNTime = dateNTime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsAllDay() {
        return isAllDay;
    }

    public void setIsAllDay(String isAllDay) {
        this.isAllDay = isAllDay;
    }
}
