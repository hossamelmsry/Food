package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class AccounterModel {
    private String orderId ,date,time,phoneNumber,payment;

    public AccounterModel() { }

    public AccounterModel(String phoneNumber, String orderId, String date, String time,String payment) {
        this.phoneNumber = phoneNumber;
        this.orderId = orderId;
        this.date = date;
        this.time = time;
        this.payment = payment;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPhoneNumber() { return phoneNumber; }

    public String getPayment() {
        return payment;
    }
}