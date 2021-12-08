package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class CartModel {
    private String productId,productQuantity,topping,totalPrice;

    public CartModel() { }

    public CartModel(String productId, String productQuantity, String totalPrice) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
        this.topping = "بدون اضافات";
    }

    public CartModel(String productId, String productQuantity, String topping, String totalPrice) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.topping = topping;
        this.totalPrice = totalPrice;
    }

    public String getProductQuantity() { return productQuantity; }

    public String getProductId() { return productId; }

    public String getTopping() { return topping; }

    public String getTotalPrice() { return totalPrice; }
}