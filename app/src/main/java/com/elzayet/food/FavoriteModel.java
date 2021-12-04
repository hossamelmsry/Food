package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class FavoriteModel {
    private String productId ;


    public FavoriteModel() { }

    public FavoriteModel(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

}
