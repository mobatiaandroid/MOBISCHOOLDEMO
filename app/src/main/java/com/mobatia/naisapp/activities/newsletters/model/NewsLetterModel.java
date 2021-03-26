package com.mobatia.naisapp.activities.newsletters.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class NewsLetterModel implements Serializable{
    String newsLetterCatId;
    String newsLetterCatName;
    ArrayList<NewsLetterModel> newsLetterModelArrayList;
    String title;
    String newLetterSubId;
    String filename;
    String submenu;

    public String getNewsLetterCatId() {
        return newsLetterCatId;
    }

    public void setNewsLetterCatId(String newsLetterCatId) {
        this.newsLetterCatId = newsLetterCatId;
    }

    public String getNewsLetterCatName() {
        return newsLetterCatName;
    }

    public void setNewsLetterCatName(String newsLetterCatName) {
        this.newsLetterCatName = newsLetterCatName;
    }

    public ArrayList<NewsLetterModel> getNewsLetterModelArrayList() {
        return newsLetterModelArrayList;
    }

    public void setNewsLetterModelArrayList(ArrayList<NewsLetterModel> newsLetterModelArrayList) {
        this.newsLetterModelArrayList = newsLetterModelArrayList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewLetterSubId() {
        return newLetterSubId;
    }

    public void setNewLetterSubId(String newLetterSubId) {
        this.newLetterSubId = newLetterSubId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSubmenu() {
        return submenu;
    }

    public void setSubmenu(String submenu) {
        this.submenu = submenu;
    }
}
