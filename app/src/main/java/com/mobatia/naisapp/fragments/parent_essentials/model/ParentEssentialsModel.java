package com.mobatia.naisapp.fragments.parent_essentials.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class ParentEssentialsModel implements Serializable{
    String newsLetterCatId;
    String newsLetterCatName;
    ArrayList<ParentEssentialsModel> newsLetterModelArrayList;
    String title;
    String newLetterSubId;
    String filename;
    String submenu;
    String bannerImage;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    String description;
    String contactEmail;

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

    public ArrayList<ParentEssentialsModel> getNewsLetterModelArrayList() {
        return newsLetterModelArrayList;
    }

    public void setNewsLetterModelArrayList(ArrayList<ParentEssentialsModel> newsLetterModelArrayList) {
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
