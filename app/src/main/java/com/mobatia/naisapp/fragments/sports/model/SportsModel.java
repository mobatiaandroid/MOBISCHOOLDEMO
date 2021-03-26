package com.mobatia.naisapp.fragments.sports.model;

import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 28/3/17.
 */
public class SportsModel implements Serializable{
    String sports_id;
    String studentIds;
    String sports_name;
    String sports_start_date;
    String sports_end_date;
    String sports_description;
    String sports_lat;
    String sports_lng;
    String sports_venue;
    String sports_phone_number;
    String sports_email;
    String going_status;
    /************** created by aparna phase 3*/
    String meeting_point;
    String meeting_time;
    String fixture_start;
    String fixture_end;
    String school_arrival;
    String isAway;
    String sports_calendar_badge;
    String sports_fixture_badge;
    String sports_calendar_edited_badge;
    String sports_fixture_edited_badge;
    String status;
    /***********************************/
    String partipipate_status;
    ArrayList<StudentModel> sportsModelParticipantsArrayList;
    ArrayList<BusModel> sportsModelBusRoutesArrayList;
    ArrayList<StudentModel> studentModelSportsList;

    public ArrayList<SportsModel> getmSportsModelHouseArrayList() {
        return mSportsModelHouseArrayList;
    }

    public void setmSportsModelHouseArrayList(ArrayList<SportsModel> mSportsModelHouseArrayList) {
        this.mSportsModelHouseArrayList = mSportsModelHouseArrayList;
    }

    ArrayList<SportsModel> mSportsModelHouseArrayList;


    public String getSports_id() {
        return sports_id;
    }

    public void setSports_id(String sports_id) {
        this.sports_id = sports_id;
    }

    public String getSports_name() {
        return sports_name;
    }

    public void setSports_name(String sports_name) {
        this.sports_name = sports_name;
    }

    public String getSports_start_date() {
        return sports_start_date;
    }

    public void setSports_start_date(String sports_start_date) {
        this.sports_start_date = sports_start_date;
    }

    public String getSports_end_date() {
        return sports_end_date;
    }

    public void setSports_end_date(String sports_end_date) {
        this.sports_end_date = sports_end_date;
    }

    public String getSports_description() {
        return sports_description;
    }

    public void setSports_description(String sports_description) {
        this.sports_description = sports_description;
    }

    public String getSports_lat() {
        return sports_lat;
    }

    public void setSports_lat(String sports_lat) {
        this.sports_lat = sports_lat;
    }

    public String getSports_lng() {
        return sports_lng;
    }

    public void setSports_lng(String sports_lng) {
        this.sports_lng = sports_lng;
    }

    public String getSports_venue() {
        return sports_venue;
    }

    public void setSports_venue(String sports_venue) {
        this.sports_venue = sports_venue;
    }

    public String getSports_phone_number() {
        return sports_phone_number;
    }

    public void setSports_phone_number(String sports_phone_number) {
        this.sports_phone_number = sports_phone_number;
    }

    public String getSports_email() {
        return sports_email;
    }

    public void setSports_email(String sports_email) {
        this.sports_email = sports_email;
    }

    public ArrayList<StudentModel> getSportsModelParticipantsArrayList() {
        return sportsModelParticipantsArrayList;
    }

    public void setSportsModelParticipantsArrayList(ArrayList<StudentModel> sportsModelParticipantsArrayList) {
        this.sportsModelParticipantsArrayList = sportsModelParticipantsArrayList;
    }

    public ArrayList<BusModel> getSportsModelBusRoutesArrayList() {
        return sportsModelBusRoutesArrayList;
    }

    public void setSportsModelBusRoutesArrayList(ArrayList<BusModel> sportsModelBusRoutesArrayList) {
        this.sportsModelBusRoutesArrayList = sportsModelBusRoutesArrayList;
    }

    public String getGoing_status() {
        return going_status;
    }

    public void setGoing_status(String going_status) {
        this.going_status = going_status;
    }

    public String getPartipipate_status() {
        return partipipate_status;
    }

    public void setPartipipate_status(String partipipate_status) {
        this.partipipate_status = partipipate_status;
    }

    /************ created by aparana***********/
    public String getMeeting_point() {
        return meeting_point;
    }

    public void setMeeting_point(String meeting_point) {
        this.meeting_point = meeting_point;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public void setMeeting_time(String meeting_time) {
        this.meeting_time = meeting_time;
    }

    public String getFixture_start() {
        return fixture_start;
    }

    public void setFixture_start(String fixture_start) {
        this.fixture_start = fixture_start;
    }

    public String getFixture_end() {
        return fixture_end;
    }

    public void setFixture_end(String fixture_end) {
        this.fixture_end = fixture_end;
    }

    public String getSchool_arrival() {
        return school_arrival;
    }

    public void setSchool_arrival(String school_arrival) {
        this.school_arrival = school_arrival;
    }

    public String getIsAway() {
        return isAway;
    }

    public void setIsAway(String isAway) {
        this.isAway = isAway;
    }

    public String getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(String studentIds) {
        this.studentIds = studentIds;
    }

    public String getSports_calendar_badge() {
        return sports_calendar_badge;
    }

    public void setSports_calendar_badge(String sports_calendar_badge) {
        this.sports_calendar_badge = sports_calendar_badge;
    }

    public String getSports_fixture_badge() {
        return sports_fixture_badge;
    }

    public void setSports_fixture_badge(String sports_fixture_badge) {
        this.sports_fixture_badge = sports_fixture_badge;
    }

    public String getSports_calendar_edited_badge() {
        return sports_calendar_edited_badge;
    }

    public void setSports_calendar_edited_badge(String sports_calendar_edited_badge) {
        this.sports_calendar_edited_badge = sports_calendar_edited_badge;
    }

    public String getSports_fixture_edited_badge() {
        return sports_fixture_edited_badge;
    }

    public void setSports_fixture_edited_badge(String sports_fixture_edited_badge) {
        this.sports_fixture_edited_badge = sports_fixture_edited_badge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<StudentModel> getStudentModelSportsList() {
        return studentModelSportsList;
    }

    public void setStudentModelSportsList(ArrayList<StudentModel> studentModelSportsList) {
        this.studentModelSportsList = studentModelSportsList;
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
