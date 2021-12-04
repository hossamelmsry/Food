package com.elzayet.food.tools;

import android.app.Application;


public class NotificationFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        FirebaseMessaging.getInstance().subscribeToTopic("Offers");
    }
}