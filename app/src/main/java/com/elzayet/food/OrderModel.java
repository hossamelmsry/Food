package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class OrderModel {
    private String orderId ,date,time,phoneNumber,orderStatus,orderPrice,payment;

    public OrderModel() { }

    public OrderModel(String orderId, String orderPrice, String date,String time, String orderStatus, String payment, String xxxxxx) {
        this.orderId     = orderId;
        this.orderPrice  = orderPrice;
        this.date        = date;
        this.time        = time;
        this.orderStatus = orderStatus;
        this.payment     = payment;
    }

    public OrderModel(String phoneNumber, String orderId, String orderPrice, String date,String time, String orderStatus) {
        this.orderId     = orderId;
        this.orderPrice  = orderPrice;
        this.date        = date;
        this.time        = time;
        this.orderStatus = orderStatus;
        this.phoneNumber = phoneNumber;
    }

    public OrderModel(String orderId, String orderStatus, String date, String time) {
        this.orderId    = orderId;
        this.date       = date;
        this.time       = time;
        this.orderStatus= orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPhoneNumber() { return phoneNumber; }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderPrice() { return orderPrice; }

    public String getDate() { return date; }

    public String getTime() { return time; }

    public String getPayment() {
        return payment;
    }
}
