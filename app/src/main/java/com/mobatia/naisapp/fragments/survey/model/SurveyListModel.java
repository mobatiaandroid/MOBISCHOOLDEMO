package com.mobatia.naisapp.fragments.survey.model;

import java.io.Serializable;

public class SurveyListModel implements Serializable {

    String id;
    String survey_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurvey_name() {
        return survey_name;
    }

    public void setSurvey_name(String survey_name) {
        this.survey_name = survey_name;
    }
}
