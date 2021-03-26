package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderHistoryDetailModel implements Serializable {

    String id;
    String item_id;
    String quantity;
    String item_status;
    String item_total;
    String price;
    String available_quantity;
    String item_description;
    String item_image;
    String item_name;
    ArrayList<String> imageBanner;
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getItem_total() {
        return item_total;
    }

    public void setItem_total(String item_total) {
        this.item_total = item_total;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(String available_quantity) {
        this.available_quantity = available_quantity;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public ArrayList<String> getImageBanner() {
        return imageBanner;
    }

    public void setImageBanner(ArrayList<String> imageBanner) {
        this.imageBanner = imageBanner;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }
}
