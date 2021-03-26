package com.mobatia.naisapp.activities.trips.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "Trip")
public class Trip implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "users_id")
    private String users_id;
    @ColumnInfo(name = "trip_id")
    private String trip_id;
    @ColumnInfo(name = "student_id")
    private String student_id;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "order_id")
    private String order_id;
    @ColumnInfo(name = "payment_date")
    private String payment_date;
    @ColumnInfo(name = "installment_amount")
    private String installment_amount;
    @ColumnInfo(name = "amount")
    private String amount;
    @ColumnInfo(name = "payment_option")
    private String payment_option;
    @ColumnInfo(name = "installment_id")
    private String installment_id;
    @ColumnInfo(name = "category_id")
    private String category_id;
    @ColumnInfo(name = "installment")
    private String installment;
    @ColumnInfo(name = "payment_detail_id")
    private String payment_detail_id;

    public String getPayment_detail_id() {
        return payment_detail_id;
    }

    public void setPayment_detail_id(String payment_detail_id) {
        this.payment_detail_id = payment_detail_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getUsers_id() {
        return users_id;
    }

    public void setUsers_id(String user_id) {
        this.users_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(String installment_amount) {
        this.installment_amount = installment_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment_option() {
        return payment_option;
    }

    public void setPayment_option(String payment_option) {
        this.payment_option = payment_option;
    }

    public String getInstallment_id() {
        return installment_id;
    }

    public void setInstallment_id(String installment_id) {
        this.installment_id = installment_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }
}
