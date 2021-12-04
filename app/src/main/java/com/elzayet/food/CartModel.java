package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class CartModel {
    private String productId ;
    private String productQuantity ;

    public CartModel() { }

    public CartModel(String productId, String productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public String getProductQuantity() { return productQuantity; }

    public String getProductId() { return productId; }
}