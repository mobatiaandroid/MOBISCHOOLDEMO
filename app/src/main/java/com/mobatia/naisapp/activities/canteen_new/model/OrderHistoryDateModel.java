package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderHistoryDateModel implements Serializable {

    String id;
    String delivery_date;
    String total_amount;
    String status;
    String cancellation_time_exceed;
    ArrayList<OrderHistoryDetailModel>orderHistoryDetailArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancellation_time_exceed() {
        return cancellation_time_exceed;
    }

    public void setCancellation_time_exceed(String cancellation_time_exceed) {
        this.cancellation_time_exceed = cancellation_time_exceed;
    }

    public ArrayList<OrderHistoryDetailModel> getOrderHistoryDetailArrayList() {
        return orderHistoryDetailArrayList;
    }

    public void setOrderHistoryDetailArrayList(ArrayList<OrderHistoryDetailModel> orderHistoryDetailArrayList) {
        this.orderHistoryDetailArrayList = orderHistoryDetailArrayList;
    }
}
