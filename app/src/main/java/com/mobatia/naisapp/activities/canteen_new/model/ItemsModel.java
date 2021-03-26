package com.mobatia.naisapp.activities.canteen_new.model;

import java.util.ArrayList;

public class ItemsModel {
    String delivery_date;
    ArrayList<OrdersModel> items;

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public ArrayList<OrdersModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrdersModel> items) {
        this.items = items;
    }
}
