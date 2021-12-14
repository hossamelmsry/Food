package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class CartModel {
    private String productId,productQuantity,orderTopping,orderPrice,productSize;

    public CartModel() { }

    public CartModel(String productId) {
        this.productId = productId;
    }

//    public CartModel(String productId, String productQuantity, String orderTopping, String orderPrice) {
//        this.productId = productId;
//        this.productQuantity = productQuantity;
//        this.orderTopping = orderTopping;
//        this.orderPrice = orderPrice;
//    }

    public CartModel(String productId, String productQuantity, String productSize, String orderTopping, String orderPrice) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.orderTopping = orderTopping;
        this.orderPrice = orderPrice;
        this.productSize = productSize;
    }

    public String getProductQuantity() { return productQuantity; }

    public String getProductId() { return productId; }

    public String getProductSize() {  return productSize;   }

    public String getOrderTopping() {
        return orderTopping;
    }

    public String getOrderPrice() {
        return orderPrice;
    }
}