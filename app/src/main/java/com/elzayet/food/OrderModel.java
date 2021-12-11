package com.elzayet.food;

public class OrderModel {
    private String orderId, orderPrice, orderTopping, productId ,date,time;

    public OrderModel() { }

    public OrderModel(String orderId, String orderPrice, String orderTopping, String productId, String date, String time) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
        this.orderTopping = orderTopping;
        this.productId = productId;
        this.date = date;
        this.time = time;

    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getOrderTopping() {
        return orderTopping;
    }

    public String getProductId() {
        return productId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
