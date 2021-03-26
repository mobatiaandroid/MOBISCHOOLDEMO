package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    String id;
    String categoryImage;
    String categoryName;
    boolean isCategorySelected;

    public boolean isCategorySelected() {
        return isCategorySelected;
    }

    public void setCategorySelected(boolean categorySelected) {
        isCategorySelected = categorySelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
