package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class UserModel {
    //registration
    private String userPassword, userPinCode, userEmail, userName, phoneNumber, userRefellar, signupDate;

    public UserModel() { }

    public UserModel(String phoneNumber, String userName,String userPassword, String userPinCode, String userRefellar, String signupDate) {
        this.userPassword = userPassword;
        this.userPinCode = userPinCode;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userRefellar = userRefellar;
        this.signupDate = signupDate;
    }

    public String getSignupDate() {
        return signupDate;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPinCode() {
        return userPinCode;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserRefellar() {
        return userRefellar;
    }
}