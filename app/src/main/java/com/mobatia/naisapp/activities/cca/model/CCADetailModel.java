package com.mobatia.naisapp.activities.cca.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class CCADetailModel implements Serializable {

    String day;
    String choice1;
    String choice2;
    ArrayList<CCAchoiceModel> ccaChoiceModel;
    String choice1Id;
    String choice2Id;
    String cca_item_start_timechoice1;
    String cca_item_end_timechoice1;
    String cca_item_start_timechoice2;
    String cca_item_end_timechoice2;
    public ArrayList<CCAchoiceModel> getCcaChoiceModel2() {
        return ccaChoiceModel2;
    }

    public void setCcaChoiceModel2(ArrayList<CCAchoiceModel> ccaChoiceModel2) {
        this.ccaChoiceModel2 = ccaChoiceModel2;
    }

    ArrayList<CCAchoiceModel> ccaChoiceModel2;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<CCAchoiceModel> getCcaChoiceModel() {
        return ccaChoiceModel;
    }

    public void setCcaChoiceModel(ArrayList<CCAchoiceModel> ccaChoiceModel) {
        this.ccaChoiceModel = ccaChoiceModel;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice1Id() {
        return choice1Id;
    }

    public void setChoice1Id(String choice1Id) {
        this.choice1Id = choice1Id;
    }

    public String getChoice2Id() {
        return choice2Id;
    }

    public void setChoice2Id(String choice2Id) {
        this.choice2Id = choice2Id;
    }

    public String getCca_item_start_timechoice1() {
        return cca_item_start_timechoice1;
    }

    public void setCca_item_start_timechoice1(String cca_item_start_timechoice1) {
        this.cca_item_start_timechoice1 = cca_item_start_timechoice1;
    }

    public String getCca_item_end_timechoice1() {
        return cca_item_end_timechoice1;
    }

    public void setCca_item_end_timechoice1(String cca_item_end_timechoice1) {
        this.cca_item_end_timechoice1 = cca_item_end_timechoice1;
    }

    public String getCca_item_start_timechoice2() {
        return cca_item_start_timechoice2;
    }

    public void setCca_item_start_timechoice2(String cca_item_start_timechoice2) {
        this.cca_item_start_timechoice2 = cca_item_start_timechoice2;
    }

    public String getCca_item_end_timechoice2() {
        return cca_item_end_timechoice2;
    }

    public void setCca_item_end_timechoice2(String cca_item_end_timechoice2) {
        this.cca_item_end_timechoice2 = cca_item_end_timechoice2;
    }
}
