package com.mobatia.naisapp.fragments.canteen.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Canteen")
public class Canteen implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "users_id")
    private String users_id;
    @ColumnInfo(name = "student_id")
    private String student_id;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "keycode")
    private String keycode;
    @ColumnInfo(name = "payment_date")
    private String payment_date;
    @ColumnInfo(name = "amount")
    private String amount;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status)
    {
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

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
