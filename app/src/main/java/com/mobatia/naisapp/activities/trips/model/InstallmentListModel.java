package com.mobatia.naisapp.activities.trips.model;

import java.io.Serializable;
import java.util.ArrayList;

public class InstallmentListModel implements Serializable {


    String installment_id;
    String invoiceNote;
    String paid_amount;
    String inst_amount;
    String paid_status;
    String last_payment_date;
    String paid_by;
    String payment_type;
    String paid_date;
    String order_id;
    String inst_date_status;
    String payment_option;
    String enable;
    int enablePosition;
    boolean isPaid;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getPayment_option() {
        return payment_option;
    }

    public void setPayment_option(String payment_option) {
        this.payment_option = payment_option;
    }

    public int getEnablePosition() {
        return enablePosition;
    }

    public void setEnablePosition(int enablePosition) {
        this.enablePosition = enablePosition;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getInst_date_status() {
        return inst_date_status;
    }

    public void setInst_date_status(String inst_date_status) {
        this.inst_date_status = inst_date_status;
    }

    ArrayList<InstallmentListModel> InstallmentArray;

    public ArrayList<InstallmentListModel> getInstallmentArray() {
        return InstallmentArray;
    }

    public void setInstallmentArray(ArrayList<InstallmentListModel> installmentArray) {
        InstallmentArray = installmentArray;
    }

    public ArrayList<InstallmentListModel> getFullpaymentArray() {
        return FullpaymentArray;
    }

    public void setFullpaymentArray(ArrayList<InstallmentListModel> fullpaymentArray) {
        FullpaymentArray = fullpaymentArray;
    }

    ArrayList<InstallmentListModel> FullpaymentArray;





    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    String payment_status;

    public String getInstallment_id() {
        return installment_id;
    }

    public void setInstallment_id(String installment_id) {
        this.installment_id = installment_id;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getInst_amount() {
        return inst_amount;
    }

    public void setInst_amount(String inst_amount) {
        this.inst_amount = inst_amount;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public void setPaid_status(String paid_status) {
        this.paid_status = paid_status;
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
