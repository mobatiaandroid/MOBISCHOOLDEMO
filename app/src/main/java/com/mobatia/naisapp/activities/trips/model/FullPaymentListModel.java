package com.mobatia.naisapp.activities.trips.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FullPaymentListModel implements Serializable {

    String invoiceNote;
    String last_payment_date;
    String paid_amount;
    String paid_by;
    String payment_type;
    String paid_date;
    String order_id;

    ArrayList<FullPaymentListModel> InstallmentArray;

    public ArrayList<FullPaymentListModel> getInstallmentArray() {
        return InstallmentArray;
    }

    public void setInstallmentArray(ArrayList<FullPaymentListModel> installmentArray) {
        InstallmentArray = installmentArray;
    }

    public ArrayList<FullPaymentListModel> getFullpaymentArray() {
        return FullpaymentArray;
    }

    public void setFullpaymentArray(ArrayList<FullPaymentListModel> fullpaymentArray) {
        FullpaymentArray = fullpaymentArray;
    }

    ArrayList<FullPaymentListModel> FullpaymentArray;





    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    String payment_status;


    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }



    public String getPaid_by() {
        return paid_by;
    }

    public void setPaid_by(String paid_by) {
        this.paid_by = paid_by;
    }

    public String getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(String paid_date) {
        this.paid_date = paid_date;
    }



    public String getLast_payment_date() {
        return last_payment_date;
    }

    public void setLast_payment_date(String last_payment_date) {
        this.last_payment_date = last_payment_date;
    }




    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getInvoiceNote() {
        return invoiceNote;
    }

    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }


}
