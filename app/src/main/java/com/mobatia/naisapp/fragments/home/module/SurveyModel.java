package com.mobatia.naisapp.fragments.home.module;

import com.mobatia.naisapp.activities.cca.model.CCAchoiceModel;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyModel implements Serializable {

    String id;
    String survey_name;
    String image;
    String title;
    String description;
    String created_at;
    String updated_at;
    String contact_email;

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    ArrayList<SurveyQuestionsModel> surveyQuestionsArrayList;

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

    public ArrayList<SurveyQuestionsModel> getSurveyQuestionsArrayList() {
        return surveyQuestionsArrayList;
    }

    public void setSurveyQuestionsArrayList(ArrayList<SurveyQuestionsModel> surveyQuestionsArrayList) {
        this.surveyQuestionsArrayList = surveyQuestionsArrayList;
    }
}
