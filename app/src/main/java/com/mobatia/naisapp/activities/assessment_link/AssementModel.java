package com.mobatia.naisapp.activities.assessment_link;

import com.mobatia.naisapp.activities.cca.model.CCADetailModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class AssementModel implements Serializable {
String assessmentDate;
String description;
String title;
String type;
ArrayList<AssementModel> mAssementModelArrayList;

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public ArrayList<AssementModel> getmAssementModelArrayList() {
        return mAssementModelArrayList;
    }

    public void setmAssementModelArrayList(ArrayList<AssementModel> mAssementModelArrayList) {
        this.mAssementModelArrayList = mAssementModelArrayList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
