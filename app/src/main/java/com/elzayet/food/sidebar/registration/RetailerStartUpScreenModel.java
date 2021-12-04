package com.elzayet.food.sidebar.registration;

import androidx.annotation.Keep;

@Keep
public class RetailerStartUpScreenModel {
    private int plateImage;

    public RetailerStartUpScreenModel() { }

    public RetailerStartUpScreenModel(int plateImage) {
        this.plateImage = plateImage;
    }

    public int getPlateImage() {
        return plateImage;
    }

    public void setPlateImage(int plateImage) {
        this.plateImage = plateImage;
    }
}