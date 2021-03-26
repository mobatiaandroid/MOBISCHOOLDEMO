package com.mobatia.naisapp.activities.parentsassociation.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class ParentAssociationEventsModel implements Serializable{
    String evenId;
    String eventName;
    String dayOfTheWeek ;
    String day ;
   boolean itemSelected;
    int position;
    String title;
    String pdfTitle;

    public String getPdfTitle() {
        return pdfTitle;
    }

    public void setPdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }

    public String getPdfId() {
        return pdfId;
    }

    public void setPdfId(String pdfId) {
        this.pdfId = pdfId;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    String pdfUrl;
    String pdfId;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public boolean isItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(boolean itemSelected) {
        this.itemSelected = itemSelected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    String itemName;
    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonthString() {
        return monthString;
    }

    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    public String getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(String monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    String monthString;
    String monthNumber;
    String year;
    public String getEvenId() {
        return evenId;
    }

    public void setEvenId(String evenId) {
        this.evenId = evenId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }



    public ArrayList<ParentAssociationEventItemsModel> getEventItemStatusList() {
        return eventItemStatusList;
    }

    public void setEventItemStatusList(ArrayList<ParentAssociationEventItemsModel> eventItemStatusList) {
        this.eventItemStatusList = eventItemStatusList;
    }

    String eventDate;
    ArrayList<ParentAssociationEventItemsModel> eventItemStatusList;

    public ArrayList<ParentAssociationEventsModel> getEventItemList() {
        return eventItemList;
    }

    public void setEventItemList(ArrayList<ParentAssociationEventsModel> eventItemList) {
        this.eventItemList = eventItemList;
    }

    ArrayList<ParentAssociationEventsModel> eventItemList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
