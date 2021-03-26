package com.mobatia.naisapp.activities.sports.filter;

import java.io.Serializable;

/**
 * Created by mobatia on 13/07/18.
 */

public class FilterModel implements Serializable {

    String studentId;
    String activityTypeText;
    Boolean isChecked;
    Boolean isCheckedStudent;
    Boolean isCheckedTeam;
    Boolean isCheckedDay;
    Boolean isDateSet;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    String mName;
    String mClass;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public Boolean getCheckedStudent() {
        return isCheckedStudent;
    }

    public void setCheckedStudent(Boolean checkedStudent) {
        isCheckedStudent = checkedStudent;
    }

    public Boolean getCheckedTeam() {
        return isCheckedTeam;
    }

    public void setCheckedTeam(Boolean checkedTeam) {
        isCheckedTeam = checkedTeam;
    }

    public Boolean getCheckedDay() {
        return isCheckedDay;
    }

    public void setCheckedDay(Boolean checkedDay) {
        isCheckedDay = checkedDay;
    }

    public Boolean getDateSet() {
        return isDateSet;
    }

    public void setDateSet(Boolean dateSet) {
        isDateSet = dateSet;
    }
}