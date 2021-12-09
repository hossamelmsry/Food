package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class CartModel {
    private String productId,productQuantity,topping,totalPrice,productSize;

    public CartModel() { }

    public CartModel(String productId) {
        this.productId = productId;
    }

    public CartModel(String productId, String productQuantity, String topping, String totalPrice) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.topping = topping;
        this.totalPrice = totalPrice;
    }

    public CartModel(String productId, String productQuantity, String productSize, String topping, String totalPrice) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.topping = topping;
        this.totalPrice = totalPrice;
        this.productSize = productSize;
    }

    public String getProductQuantity() { return productQuantity; }

    public String getProductId() { return productId; }

    public String getTopping() { return topping; }

    public String getTotalPrice() { return totalPrice; }

    public String getProductSize() {  return productSize;   }
}