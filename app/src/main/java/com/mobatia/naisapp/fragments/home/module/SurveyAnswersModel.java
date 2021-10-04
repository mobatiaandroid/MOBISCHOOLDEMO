package com.mobatia.naisapp.fragments.home.module;

import java.io.Serializable;

public class SurveyAnswersModel implements Serializable {

    String id;
    String answer;
    boolean isClicked;
    boolean isClicked0;
    boolean isClicked1;
    boolean isClicked2;
    boolean isclicked3;
    boolean isclicked4;

    public boolean isClicked0() {
        return isClicked0;
    }

    public void setClicked0(boolean clicked0) {
        isClicked0 = clicked0;
    }

    public boolean isClicked1() {
        return isClicked1;
    }

    public void setClicked1(boolean clicked1) {
        isClicked1 = clicked1;
    }

    public boolean isClicked2() {
        return isClicked2;
    }

    public void setClicked2(boolean clicked2) {
        isClicked2 = clicked2;
    }

    public boolean isIsclicked3() {
        return isclicked3;
    }

    public void setIsclicked3(boolean isclicked3) {
        this.isclicked3 = isclicked3;
    }

    public boolean isIsclicked4() {
        return isclicked4;
    }

    public void setIsclicked4(boolean isclicked4) {
        this.isclicked4 = isclicked4;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
