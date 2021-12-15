package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class OrderModel {
    private String orderId ,date,time,phoneNumber,orderStatus;

    public OrderModel() { }

    public OrderModel(String phoneNumber, String orderId, String date, String time,String orderStatus) {
        this.phoneNumber = phoneNumber;
        this.orderId = orderId;
        this.date = date;
        this.time = time;
        this.orderStatus = orderStatus;
    }

    public OrderModel(String orderId, String date, String time,String orderStatus) {
        this.orderId = orderId;
        this.date = date;
        this.time = time;
        this.orderStatus = orderStatus;
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

    public String getOrderStatus() {
        return orderStatus;
    }
}
