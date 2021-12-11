package com.elzayet.food;

public class ArchiveModel {
    private String id, msg,date,time;
    private String orderId, orderPrice, orderTopping, productId, productQuantity, productSize;
    public ArchiveModel() { }

    public ArchiveModel(String id, String msg, String date, String time) {
        this.id = id;
        this.msg = msg;
        this.date = date;
        this.time = time;
    }


    public ArchiveModel(String orderId, String orderPrice, String orderTopping, String productId, String productQuantity, String productSize, String date, String time) {
        this.orderId         = orderId;
        this.orderPrice      = orderPrice;
        this.orderTopping    = orderTopping;
        this.productId       = productId;
        this.productQuantity = productQuantity;
        this.productSize     = productSize;
        this.date            = date;
        this.time            = time;
    }

    public String getId() { return id; }

    public String getMsg() { return msg; }

    public String getDate() { return date; }

    public String getTime() { return time; }

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

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getProductSize() {
        return productSize;
    }
}
