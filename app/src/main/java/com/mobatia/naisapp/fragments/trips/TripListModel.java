package com.mobatia.naisapp.fragments.trips;

import com.mobatia.naisapp.activities.trips.model.FullPaymentListModel;
import com.mobatia.naisapp.activities.trips.model.InstallmentListModel;

import java.io.Serializable;
import java.util.ArrayList;

public class TripListModel implements Serializable {
    String id;
    String title;
    String description;
    String trip_date;
    String amount;
    String status;
    String last_payment_date;
    String completed_date;
    String last_payment_status;
    String trip_date_staus;
    String studentName;
    String order_id;
    String parentName;
    String paidby;
    String payment_type;
    String invoiceNote;
    String installment;
    String payment_option;
    String remaining_amount;
    String payment_status;
    String closing_date;
    String last_paid_date;
    String payment_date_status;
    String billingCode;
    boolean isEmiAvailable;
    boolean isfullPaid;
    ArrayList<InstallmentListModel> installmentArrayList;
    ArrayList<FullPaymentListModel> fullpaymentArrayList;
    public String getInstallment() {
        return installment;
    }

    public String getBillingCode() {
        return billingCode;
    }

    public void setBillingCode(String billingCode) {
        this.billingCode = billingCode;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getPayment_option() {
        return payment_option;
    }

    public void setPayment_option(String payment_option) {
        this.payment_option = payment_option;
    }

    public String getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(String remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getClosing_date() {
        return closing_date;
    }

    public void setClosing_date(String closing_date) {
        this.closing_date = closing_date;
    }

    public String getLast_paid_date() {
        return last_paid_date;
    }

    public void setLast_paid_date(String last_paid_date) {
        this.last_paid_date = last_paid_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLast_payment_date() {
        return last_payment_date;
    }

    public void setLast_payment_date(String last_payment_date) {
        this.last_payment_date = last_payment_date;
    }

    public String getCompleted_date() {
        return completed_date;
    }

    public void setCompleted_date(String completed_date) {
        this.completed_date = completed_date;
    }

    public String getLast_payment_status() {
        return last_payment_status;
    }

    public void setLast_payment_status(String last_payment_status) {
        this.last_payment_status = last_payment_status;
    }

    public String getTrip_date_staus() {
        return trip_date_staus;
    }

    public void setTrip_date_staus(String trip_date_staus) {
        this.trip_date_staus = trip_date_staus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPaidby() {
        return paidby;
    }

    public void setPaidby(String paidby) {
        this.paidby = paidby;
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

    public ArrayList<InstallmentListModel> getInstallmentArrayList() {
        return installmentArrayList;
    }

    public void setInstallmentArrayList(ArrayList<InstallmentListModel> installmentArrayList) {
        this.installmentArrayList = installmentArrayList;
    }

    public ArrayList<FullPaymentListModel> getFullpaymentArrayList() {
        return fullpaymentArrayList;
    }

    public void setFullpaymentArrayList(ArrayList<FullPaymentListModel> fullpaymentArrayList) {
        this.fullpaymentArrayList = fullpaymentArrayList;
    }

    public String getPayment_date_status() {
        return payment_date_status;
    }

    public void setPayment_date_status(String payment_date_status) {
        this.payment_date_status = payment_date_status;
    }

    public boolean isEmiAvailable() {
        return isEmiAvailable;
    }

    public void setEmiAvailable(boolean emiAvailable) {
        isEmiAvailable = emiAvailable;
    }

    public boolean isIsfullPaid() {
        return isfullPaid;
    }

    public void setIsfullPaid(boolean isfullPaid) {
        this.isfullPaid = isfullPaid;
    }
}
