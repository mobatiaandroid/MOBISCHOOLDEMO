package com.mobatia.naisapp.activities.canteen_new.model;


import java.io.Serializable;

public class DateModel implements Serializable {
    String date;
    String day;
    String id;
    String month;
    String year;
    String numberDate;
    boolean isItemSelected;
    boolean isDateSelected;

    public boolean isItemSelected() {
        return isItemSelected;
    }

    public void setItemSelected(boolean itemSelected) {
        isItemSelected = itemSelected;
    }

    public boolean isDateSelected() {
        return isDateSelected;
    }

    public void setDateSelected(boolean dateSelected) {
        isDateSelected = dateSelected;
    }

    public String getNumberDate() {
        return numberDate;
    }

    public void setNumberDate(String numberDate) {
        this.numberDate = numberDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
