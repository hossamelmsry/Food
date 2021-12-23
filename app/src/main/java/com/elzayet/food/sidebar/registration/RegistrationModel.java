package com.elzayet.food.sidebar.registration;

import androidx.annotation.Keep;

@Keep
public class RegistrationModel {
    private int plateImage;

    public RegistrationModel() { }

    public RegistrationModel(int plateImage) {
        this.plateImage = plateImage;
    }

    public int getPlateImage() {
        return plateImage;
    }

}