package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;

public class CanteenDetailShowModel implements Serializable {
    String id;
    String item_name;
    String available_date;
    String available_quantity;
    String unit;
    String bar_code;
    String profit_margin;
    String price;
    String barcode_qty;
    String item_category_id;
    String status;
    String item_image;
    boolean cartStatus;
    String itemQuantity;

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public boolean isCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(boolean cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getAvailable_date() {
        return available_date;
    }

    public void setAvailable_date(String available_date) {
        this.available_date = available_date;
    }

    public String getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(String available_quantity) {
        this.available_quantity = available_quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getProfit_margin() {
        return profit_margin;
    }

    public void setProfit_margin(String profit_margin) {
        this.profit_margin = profit_margin;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBarcode_qty() {
        return barcode_qty;
    }

    public void setBarcode_qty(String barcode_qty) {
        this.barcode_qty = barcode_qty;
    }

    public String getItem_category_id() {
        return item_category_id;
    }

    public void setItem_category_id(String item_category_id) {
        this.item_category_id = item_category_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }
}
