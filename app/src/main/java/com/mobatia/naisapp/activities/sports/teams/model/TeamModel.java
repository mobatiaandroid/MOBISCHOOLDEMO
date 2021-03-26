package com.mobatia.naisapp.activities.sports.teams.model;

import java.io.Serializable;

public class TeamModel implements Serializable {

    String mId;

    public String getGoingStatus() {
        return goingStatus;
    }

    public void setGoingStatus(String goingStatus) {
        this.goingStatus = goingStatus;
    }

    String goingStatus;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }




    String mName;String mClass;
    String mSection;

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmHouse() {
        return mHouse;
    }

    public void setmHouse(String mHouse) {
        this.mHouse = mHouse;
    }

    public String getmSection() {
        return mSection;
    }

    public void setmSection(String mSection) {
        this.mSection = mSection;
    }

    public String getmClass() {
        return mClass;
    }

    public void setmClass(String mClass) {
        this.mClass = mClass;
    }

    String mPhoto;
    String mHouse;
    String progressReport;
    String alumini;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public String getProgressReport() {
        return progressReport;
    }

    public void setProgressReport(String progressReport) {
        this.progressReport = progressReport;
    }

    public String getAlumini() {
        return alumini;
    }

    public void setAlumini(String alumini) {
        this.alumini = alumini;
    }
}

