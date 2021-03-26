package com.mobatia.naisapp.fragments.about_us.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class AboutusModel implements Serializable{
    String Id;
    String Url;
    String webUrl;
    String description;
    String email;
    String title;
    String TabType;
    ArrayList<AboutusModel> aboutusModelArrayList;
    ArrayList<AboutusModel> mFacilitylListArray;
    String itemDesc;
    String itemImageUrl;
    String itemPdfUrl;
    String itemTitle;


    public String getTabType() {
        return TabType;
    }

    public void setTabType(String tabType) {
        TabType = tabType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<AboutusModel> getAboutusModelArrayList() {
        return aboutusModelArrayList;
    }

    public void setAboutusModelArrayList(ArrayList<AboutusModel> aboutusModelArrayList) {
        this.aboutusModelArrayList = aboutusModelArrayList;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getImageUrl() {
        return itemImageUrl;
    }

    public void setImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public ArrayList<AboutusModel> getmFacilitylListArray() {
        return mFacilitylListArray;
    }

    public void setmFacilitylListArray(ArrayList<AboutusModel> mFacilitylListArray) {
        this.mFacilitylListArray = mFacilitylListArray;
    }

    public String getItemPdfUrl() {
        return itemPdfUrl;
    }

    public void setItemPdfUrl(String itemPdfUrl) {
        this.itemPdfUrl = itemPdfUrl;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
