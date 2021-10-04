package com.mobatia.naisapp.fragments.home.module;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyQuestionsModel implements Serializable {

    String id;
    String question;
    String answer_type;
    String answer;
    ArrayList<SurveyAnswersModel> surveyAnswersrrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<SurveyAnswersModel> getSurveyAnswersrrayList() {
        return surveyAnswersrrayList;
    }

    public void setSurveyAnswersrrayList(ArrayList<SurveyAnswersModel> surveyAnswersrrayList) {
        this.surveyAnswersrrayList = surveyAnswersrrayList;
    }

    public String getAnswer_type() {
        return answer_type;
    }

    public void setAnswer_type(String answer_type) {
        this.answer_type = answer_type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
