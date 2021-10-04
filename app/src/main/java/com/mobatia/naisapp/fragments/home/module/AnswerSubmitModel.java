package com.mobatia.naisapp.fragments.home.module;

import java.io.Serializable;

public class AnswerSubmitModel implements Serializable {
    String question_id;
    String answer_id;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }
}
