package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class ShareModel {
    private String productId ;

    public ShareModel() { }

    public ShareModel(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

}
