package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class ProductModel {
    private String menuName,productId,productImage,productName,productDescription,smallSize,mediumSize,largeSize;

    public ProductModel() { }

    public String getMenuName() {
        return menuName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getSmallSize() { return smallSize; }

    public String getMediumSize() { return mediumSize; }

    public String getLargeSize() { return largeSize; }
}
