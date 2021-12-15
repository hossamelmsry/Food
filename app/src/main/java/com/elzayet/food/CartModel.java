package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class CartModel {
    private String productId,productQuantity,productTopping,productPrice,productSize;

    public CartModel() { }

    public CartModel(String productId, String productQuantity, String productSize, String productTopping, String productPrice) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.productSize = productSize;
        this.productTopping = productTopping;
        this.productPrice = productPrice;
    }

    public String getProductQuantity() { return productQuantity; }

    public String getProductId() { return productId; }

    public String getProductSize() {  return productSize;   }

    public String getProductTopping() { return productTopping; }

    public String getProductPrice() { return productPrice; }
}