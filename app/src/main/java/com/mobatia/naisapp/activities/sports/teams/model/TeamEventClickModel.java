package com.mobatia.naisapp.activities.sports.teams.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamEventClickModel implements Serializable {
String day;
String date;
String time;
String venue;

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
    ArrayList<TeamDatesModel> teamDatesModel;

    public ArrayList<TeamDatesModel> getTeamDatesModel() {
        return teamDatesModel;
    }

    public void setTeamDatesModel(ArrayList<TeamDatesModel> teamDatesModel) {
        this.teamDatesModel = teamDatesModel;
    }
}
